package com.app.conectCarConsumer;

import com.app.bean.JmsConnectionFactory;
import com.app.bean.JmsServerConnection;
import com.app.helper.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

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
                logger.info("Initializing Aplicacao Consumer...................");
                jmsServerConnection = jmsServerProperites.get("OSA3");
                username = jmsProperites.get("PassagemProcessadaRemoteOSA31024").getUsername();
                password = jmsProperites.get("PassagemProcessadaRemoteOSA31024").getPassword();
                jmsConnectionFactoryContext = jmsProperites.get("PassagemProcessadaRemoteOSA31024").getJmsFactory();
                connectionFactory = consumer.connectionFactory(jmsServerConnection, username, password, jmsConnectionFactoryContext);

                Runnable passagemProcessadaConsumer = () -> {
                    jmsConnectionFactory = jmsProperites.get("PassagemProcessadaRemoteOSA31024");
                    consumer.startConnectionFactory("PassagemProcessadaRemoteOSA31024", jmsConnectionFactory, connectionFactory);
                };
                Runnable requisitaImagemConsumer = () -> {
                    jmsConnectionFactory = jmsProperites.get("RequisitaImagemRemoteOSA31024");
                    consumer.startConnectionFactory("RequisitaImagemRemoteOSA31024", jmsConnectionFactory, connectionFactory);
                };
                Runnable falhaConsumer = () -> {
                    jmsConnectionFactory = jmsProperites.get("FalhaComunicacaoRemoteOSA31024");
                    consumer.startConnectionFactory("FalhaComunicacaoRemoteOSA31024", jmsConnectionFactory, connectionFactory);
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
