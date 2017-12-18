package com.app.conectCarConsumer;

import com.app.bean.JmsConnectionFactory;
import com.app.bean.JmsServerConnection;
import com.app.helper.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hornetq.jms.client.HornetQConnectionFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.HashMap;


@Component
public class ConsumeRun{

    private static final Log logger = LogFactory.getFactory().getInstance(ConsumeRun.class);
    private static JmsConnectionFactory jmsConnectionFactory = null;
    private static JmsServerConnection jmsServerConnection = null;
    private static HashMap<String, JmsServerConnection> jmsServerProperites = GetProperties.getServerProperties();
    private static HashMap<String, JmsConnectionFactory> jmsProperites = GetProperties.getProperties();
    private static HornetQConnectionFactory connectionFactory = null;
    private static String username = null;
    private static String password = null;
    private static String jmsConnectionFactoryContext = null;
    private static ConsumerImpl consumer = new ConsumerImpl();


   public void run(){
        {
            try {
                logger.info("Initializing Aplicacao Consumer...................");
                jmsServerConnection = jmsServerProperites.get("OSA3");
                username = jmsProperites.get("PassagemProcessadaRemoteConc1024OSA3").getUsername();
                password = jmsProperites.get("PassagemProcessadaRemoteConc1024OSA3").getPassword();
                jmsConnectionFactoryContext = jmsProperites.get("PassagemProcessadaRemoteConc1024OSA3").getJmsFactory();
                connectionFactory = consumer.connectionFactory(jmsServerConnection, username, password, jmsConnectionFactoryContext);

                Runnable passagemProcessadaConsumer = () -> {
                    jmsConnectionFactory = jmsProperites.get("PassagemProcessadaRemoteConc1024OSA3");
                    consumer.startConnectionFactory("PassagemProcessadaRemoteConc1024OSA3", jmsConnectionFactory, connectionFactory);
                };
                Runnable requisitaImagemConsumer = () -> {
                    jmsConnectionFactory = jmsProperites.get("RequisitaImagemRemoteConc1024OSA3");
                    consumer.startConnectionFactory("RequisitaImagemRemoteConc1024OSA3", jmsConnectionFactory, connectionFactory);
                };
                Runnable falhaConsumer = () -> {
                    jmsConnectionFactory = jmsProperites.get("FalhaComunicacaoRemoteConc1024OSA3");
                    consumer.startConnectionFactory("FalhaComunicacaoRemoteConc1024OSA3", jmsConnectionFactory, connectionFactory);
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
