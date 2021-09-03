package com.miaoshaproject.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * ClassName WebServerConfiguration
 * Description TODO
 * 当Spring容器内没有TomcatEmbeddedServletContainerFactory这个bean时，会把此bean加载进Spring容器内
 * @author 13299
 * Date 2020/8/9 21:36
 * @version 1.0
 **/
@Component
public class WebServerConfiguration implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    /**
     * 使用对应工厂类提供给我们的接口定制化我们的tomcat connector
     * @param factory
     */
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        ((TomcatServletWebServerFactory)factory).addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();

                //定制化keepalivetimeout
                protocol.setKeepAliveTimeout(30000);
                protocol.setMaxKeepAliveRequests(10000);
            }
        });
    }
}
