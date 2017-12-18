package com.app.helper;

import com.app.bean.JmsConnectionFactory;
import com.app.bean.JmsServerConnection;
import com.app.conectCarConsumer.ConsumerConectCarFC;
import com.app.conectCarConsumer.ConsumerConectCarPP;
import com.app.conectCarConsumer.ConsumerConectCarRI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class ConsumerImpl {

    private static Properties env =null;
    public static Context context;
    private static Connection connection = null;
    private static Session session = null;
    private static TopicSubscriber topicSubscriber = null;

    private static final Log log = LogFactory.getFactory().getInstance(SendMessageFactory.class);

    private static final String CONECTCAR_EXTERNAL_CLIENT = "conectcar-external-client";

    public static void startConnectionFactory(String fluxo, JmsConnectionFactory jmsConnectionFactory, ConnectionFactory connectionFactory){

        try {

            log.info("Conectando " + fluxo + " | fila: " + jmsConnectionFactory.getJmsQueue());

            Destination destination = (Destination) context.lookup(jmsConnectionFactory.getJmsQueue());

            connection = connectionFactory.createConnection(System.getProperty("username", jmsConnectionFactory.getUsername()), System.getProperty("password", jmsConnectionFactory.getPassword()));
            connection.setClientID(fluxo + "autoban");

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            topicSubscriber = session.createDurableSubscriber((Topic) destination, CONECTCAR_EXTERNAL_CLIENT);

            if (fluxo.equals("PassagemProcessadaRemoteOSA31012")) {
                topicSubscriber.setMessageListener(new ConsumerConectCarPP());
            } else if (fluxo.equals("RequisitaImagemRemoteOSA31012")) {
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
            consumer.startConnectionFactory(fluxo, jmsConnectionFactory, connectionFactory);

        } catch (Exception e){
            log.error("Erro na Conexao " + jmsConnectionFactory.getJmsQueue() + " motivo erro " + e.getMessage());
            log.error("Conexao  " + jmsConnectionFactory.getJmsQueue() + " sera fechada!");
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

    public static ConnectionFactory connectionFactory(JmsServerConnection jmsServerConnection, String username, String password, String jmsConnectionFactoryContext) {

        ConnectionFactory connectionFactory = null;

        env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, jmsServerConnection.getContextFactory());
        env.put(Context.PROVIDER_URL, jmsServerConnection.getUrl());
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put("jboss.naming.client.ejb.context", true);
        env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        env.put("invocation.timeout", 3000);
        try {
            context = new InitialContext(env);
            connectionFactory =
                    (ConnectionFactory) context.lookup(jmsConnectionFactoryContext);
        }catch (Exception e){
            ConsumerImpl consumer = new ConsumerImpl();
            log.error("Erro na Conexao " + jmsServerConnection.getUrl() + " motivo erro " + e.getMessage());
            log.info("Conexao " + jmsServerConnection.getUrl() + " com problemas! Re-tentativa de conexao em 9000 milisegundos ");
            try {
                Thread.sleep(9000);
            } catch (InterruptedException e1) {
                log.error("Erro na Conexao " + jmsServerConnection.getUrl() + " motivo erro " + e.getMessage());
                log.error("Aplicaçao sera fechada!");
            }
            consumer.connectionFactory(jmsServerConnection, username, password, jmsConnectionFactoryContext );
        }
        return connectionFactory;
    }
}