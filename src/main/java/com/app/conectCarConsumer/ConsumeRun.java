package com.app.conectCarConsumer;

import com.app.bean.JmsConnectionFactory;
import com.app.bean.JmsServerConnection;
import com.app.helper.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.ConnectionFactory;
import java.util.HashMap;


@Component
public class ConsumeRun{

    private static final Log logger = LogFactory.getFactory().getInstance(ConsumeRun.class);
    private static JmsConnectionFactory jmsConnectionFactory = null;
    private static JmsServerConnection jmsServerConnection = null;
    private static HashMap<String, JmsServerConnection> jmsServerProperites = GetProperties.getServerProperties();
    private static HashMap<String, JmsConnectionFactory> jmsProperites = GetProperties.getProperties();
    private static ConnectionFactory connectionFactory = null;
    private static String username = null;
    private static String password = null;
    private static String jmsConnectionFactoryContext = null;
    private static ConsumerImpl consumer = new ConsumerImpl();


   public void run(){
        {
            try {
                logger.info("Initializing...................");
                jmsServerConnection = jmsServerProperites.get("OSA3");
                username = jmsProperites.get("PassagemProcessadaRemoteOSA31012").getUsername();
                password = jmsProperites.get("PassagemProcessadaRemoteOSA31012").getPassword();
                jmsConnectionFactoryContext = jmsProperites.get("PassagemProcessadaRemoteOSA31012").getJmsFactory();
                connectionFactory = consumer.connectionFactory(jmsServerConnection, username, password, jmsConnectionFactoryContext);

                Runnable passagemProcessadaConsumer = () -> {
                    jmsConnectionFactory = jmsProperites.get("PassagemProcessadaRemoteOSA31012");
                    consumer.startConnectionFactory("PassagemProcessadaRemoteOSA31012", jmsConnectionFactory, connectionFactory);
                };
                Runnable requisitaImagemConsumer = () -> {
                    jmsConnectionFactory = jmsProperites.get("RequisitaImagemRemoteOSA31012");
                    consumer.startConnectionFactory("RequisitaImagemRemoteOSA31012", jmsConnectionFactory, connectionFactory);
                };
                Runnable falhaConsumer = () -> {
                    jmsConnectionFactory = jmsProperites.get("FalhaComunicacaoRemoteOSA31012");
                    consumer.startConnectionFactory("FalhaComunicacaoRemoteOSA31012", jmsConnectionFactory, connectionFactory);
                };

                passagemProcessadaConsumer.run();
                requisitaImagemConsumer.run();
                falhaConsumer.run();
            } catch (Exception e) {
                logger.error("Erro ao inicializacao aplicacao - Motivo " + e.getMessage());
            }
        }
    }

    @PreDestroy
    public void onExit() {
        logger.info("Fechando todas as conexoes");
        consumer.stopConnection();
    }
}
