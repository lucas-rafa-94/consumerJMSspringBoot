package com.app.helper;

import com.app.bean.JmsConnectionFactory;
import com.app.bean.JmsServerConnection;
import com.app.conectCarConsumer.ConsumerConectCarFC;
import com.app.conectCarConsumer.ConsumerConectCarPP;
import com.app.conectCarConsumer.ConsumerConectCarRI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.jms.client.HornetQConnection;
import org.hornetq.jms.client.HornetQConnectionFactory;
import org.hornetq.jms.client.HornetQSession;
import org.hornetq.jms.client.HornetQTopic;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

public class ConsumerImpl {

    private static HornetQConnection connection = null;
    private static HornetQSession session = null;
    private static TopicSubscriber topicSubscriber = null;
    private static HornetQTopic topic = null;

    private static final Log log = LogFactory.getFactory().getInstance(SendMessageFactory.class);

    private static final String CONECTCAR_EXTERNAL_CLIENT = "conectcar-external-client";

    public static void startConnectionFactory(String fluxo, JmsConnectionFactory jmsConnectionFactory, JmsServerConnection jmsServerConnection){

        HornetQConnectionFactory connectionFactory = connectionFactory(jmsServerConnection);

        try {

            log.info("Conectando " + fluxo + " | fila: " + jmsConnectionFactory.getJmsQueue());

            connection = (HornetQConnection) connectionFactory.createConnection(jmsConnectionFactory.getUsername(), jmsConnectionFactory.getPassword());
            connection.setClientID(fluxo + "clientId");
            connection.start();

            session = (HornetQSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            topic = (HornetQTopic) session.createTopic(jmsConnectionFactory.getJmsQueue());
            topicSubscriber = session.createDurableSubscriber(topic, CONECTCAR_EXTERNAL_CLIENT);

            if (fluxo.equals("PassagemProcessadaRemoteConc1024OSA3")) {
                topicSubscriber.setMessageListener(new ConsumerConectCarPP());
            } else if (fluxo.equals("RequisitaImagemRemoteConc1024OSA3")) {
                topicSubscriber.setMessageListener(new ConsumerConectCarRI());
            } else {
                topicSubscriber.setMessageListener(new ConsumerConectCarFC());
            }
            log.info("Conectado com sucesso  " + fluxo + " | fila: " + jmsConnectionFactory.getJmsQueue());

        } catch (javax.jms.IllegalStateException e){
                ConsumerImpl consumer = new ConsumerImpl();
                log.info("Conexao " + jmsConnectionFactory.getJmsQueue() + " ja em uso! Re-tentativa de conexao em 9000 milisegundos ");
                try {
                    Thread.sleep(9000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            consumer.startConnectionFactory(fluxo, jmsConnectionFactory, jmsServerConnection);

        } catch (Exception e){
            ConsumerImpl consumer = new ConsumerImpl();
            log.error("Erro na Conexao " + jmsConnectionFactory.getJmsQueue() + " motivo erro " + e.getMessage());
            log.error("Conexao  " + jmsConnectionFactory.getJmsQueue() + " sera re-tentada em 9000 milisegundos!");
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e1) {
                    log.error("Erro ao fechar sessao " + jmsConnectionFactory.getJmsQueue() + " motivo erro " + e1.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e2) {
                    log.error("Erro ao fechar conexao " + jmsConnectionFactory.getJmsQueue() + " motivo erro " + e2.getMessage());
                }
            }
            if (topicSubscriber != null) {
                try {
                    topicSubscriber.close();
                } catch (JMSException e3) {
                    log.error("Erro ao fechar conexao " + jmsConnectionFactory.getJmsQueue() + " motivo erro " + e3.getMessage());
                }
            }
            try {
                Thread.sleep(9000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            consumer.startConnectionFactory(fluxo, jmsConnectionFactory, jmsServerConnection);
        }
    }

    public static void stopConnection(){

        if (session != null) {
            try {
                session.close();
            } catch (JMSException e1) {
                log.error("Erro ao fechar sessao  motivo erro " + e1.getMessage());
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e2) {
                log.error("Erro ao fechar conexao motivo erro " + e2.getMessage());
            }
        }
        if (topicSubscriber != null) {
            try {
                topicSubscriber.close();
            } catch (JMSException e3) {
                log.error("Erro ao fechar conexao  motivo erro " + e3.getMessage());
            }
        }

    }

    public static HornetQConnectionFactory connectionFactory(JmsServerConnection jmsServerConnection) {

        Map<String, Object> connectionParams = new HashMap<String, Object>();
        TransportConfiguration transportConfiguration = null;
        HornetQConnectionFactory connectionFactory = null;

        connectionParams.put(org.hornetq.core.remoting.impl.netty.TransportConstants.PORT_PROP_NAME, jmsServerConnection.getUrl().split(":")[1]);
        connectionParams.put(org.hornetq.core.remoting.impl.netty.TransportConstants.HOST_PROP_NAME, jmsServerConnection.getUrl().split(":")[0]);

        try {
            transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getName(), connectionParams);
            connectionFactory = HornetQJMSClient.createConnectionFactoryWithHA(JMSFactoryType.CF, transportConfiguration);
        }catch (Exception e){
            ConsumerImpl consumer = new ConsumerImpl();
            log.error("Erro na Conexao " + jmsServerConnection.getUrl() + " motivo erro " + e.getMessage());
            e.printStackTrace();
            log.info("Conexao " + jmsServerConnection.getUrl() + " com problemas! Re-tentativa de conexao em 9000 milisegundos ");
            try {
                Thread.sleep(9000);
            } catch (InterruptedException e1) {
                log.error("Erro na Conexao " + jmsServerConnection.getUrl() + " motivo erro " + e.getMessage());
                log.error("Aplicaçao sera fechada!");
            }
            consumer.connectionFactory(jmsServerConnection);
        }
        return connectionFactory;
    }
}