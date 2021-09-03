package com.miaoshaproject.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * ClassName JodaDateTimeJsonSerializer
 * Description TODO
 * 方便redis对日期格式数据的序列化
 * @author 13299
 * Date 2020/8/12 19:37
 * @version 1.0
 **/

public class JodaDateTimeJsonSerializer extends JsonSerializer<DateTime> {

    @Override
    public void serialize(DateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(dateTime.toString("yyyy-MM-dd HH:mm:ss"));
    }
}
