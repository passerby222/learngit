package com.miaoshaproject.mq;

import com.alibaba.fastjson.JSON;
import com.miaoshaproject.dao.StockLogDOMapper;
import com.miaoshaproject.dataobject.StockLogDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.IOrderService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName MqProducer
 * Description TODO
 *
 * @author 13299
 * Date 2020/8/15 14:35
 * @version 1.0
 **/
@Component
public class MqProducer {
    private DefaultMQProducer producer;

    private TransactionMQProducer transactionMQProducer;
    @Value("${mq.nameserver.addr}")
    private String nameAddr;
    @Value("${mq.topicname}")
    private String topicName;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private StockLogDOMapper stockLogDOMapper;

    @PostConstruct
    public void init() throws MQClientException {
        //做mqproducer的初始化
        producer = new DefaultMQProducer("producer_group");
        producer.setNamesrvAddr(nameAddr);
        producer.start();

        transactionMQProducer = new TransactionMQProducer("transaction_producer_group");
        transactionMQProducer.setNamesrvAddr(nameAddr);
        transactionMQProducer.start();
        transactionMQProducer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object args) {
                //真正要做的事, 即创建订单
                Integer userId = (Integer)((Map)args).get("userId");
                Integer promoId = (Integer)((Map)args).get("promoId");
                Integer itemId = (Integer)((Map)args).get("itemId");
                Integer amount = (Integer)((Map)args).get("amount");
                String stockLogId = (String)((Map)args).get("stockLogId");


                try {
                    orderService.createOrder(userId, promoId, itemId, amount, stockLogId);
                } catch (BusinessException e) {
                    e.printStackTrace();
                    //设置对应的stockLog为回滚状态
                    StockLogDO stockLogDO = stockLogDOMapper.selectByPrimaryKey(stockLogId);
                    stockLogDO.setStatus(3);
                    stockLogDOMapper.updateByPrimaryKeySelective(stockLogDO);
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            /**
             * 上面的方法执行了很久，或者执行失败，就会回调该函数
             * @param msg
             * @return
             */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                //根据redis内是否扣减库存成功，来判断要返回COMMIT，ROLLBACK还是继续UNKNOWN
                String jsonString = new String(msg.getBody());
                Map<String, Object> map = JSON.parseObject(jsonString, Map.class);
                Integer itemId = (Integer)map.get("itemId");
                Integer amount = (Integer)map.get(("amount"));
                String stockLogId = (String)map.get(("stockLogId"));
                StockLogDO stockLogDO = stockLogDOMapper.selectByPrimaryKey(stockLogId);
                if (stockLogDO == null){
                    return LocalTransactionState.UNKNOW;
                }
                if(stockLogDO.getStatus() == 2){
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                else if(stockLogDO.getStatus() == 1){
                    return LocalTransactionState.UNKNOW;
                }
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        });
    }

    /**
     * 同步扣减库存消息
     * @param itemId
     * @param amount
     * @return
     */
    public boolean asyncReduceStock(Integer itemId, Integer amount){
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("itemId", itemId);
        bodyMap.put("amount", amount);
        Message message = new Message(topicName, "increase",
                JSON.toJSON(bodyMap).toString().getBytes(Charset.forName("UTF-8")));
        try {
            producer.send(message);
        } catch (MQClientException e) {
            e.printStackTrace();
            return false;
        } catch (RemotingException e) {
            e.printStackTrace();
            return false;
        } catch (MQBrokerException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 事务型同步库存扣减消息
     * @param itemId
     * @param amount
     * @return
     */
    public boolean transactionAsyncReduceStock(Integer userId, Integer promoId, Integer itemId, Integer amount, String stockLogId){
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("itemId", itemId);
        bodyMap.put("amount", amount);
        bodyMap.put("stockLogId", stockLogId);

        Map<String, Object> argsMap = new HashMap<>();
        argsMap.put("userId", userId);
        argsMap.put("promoId", promoId);
        argsMap.put("itemId", itemId);
        argsMap.put("amount", amount);
        argsMap.put("stockLogId", stockLogId);

        Message message = new Message(topicName, "increase",
                JSON.toJSON(bodyMap).toString().getBytes(Charset.forName("UTF-8")));
        TransactionSendResult sendResult = null;
        try {
            //二阶段提交的概念，这条消息发出去之后，message broker可以收到对应的消息，但是它的状态不是可被消费状态。而是Prepare的状态。
            //在prepare状态下，消息不会被消费者看到。而是在客户端执行executeLocalTransaction方法
            sendResult = transactionMQProducer.sendMessageInTransaction(message, argsMap);
        } catch (MQClientException e) {
            e.printStackTrace();
            return false;
        }
        if(sendResult.getLocalTransactionState() == LocalTransactionState.ROLLBACK_MESSAGE){
            return false;
        }
        else if(sendResult.getLocalTransactionState() == LocalTransactionState.COMMIT_MESSAGE){
            return true;
        }
        else{
            return false;
        }
    }
}
