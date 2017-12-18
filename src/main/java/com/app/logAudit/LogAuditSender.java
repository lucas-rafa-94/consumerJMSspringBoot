package com.app.logAudit;


import com.app.bean.LogAuditCollection;
import com.app.logErro.ErrorHandlerSender;
import com.app.helper.GetProperties;
import com.app.bean.JmsServerConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Hashtable;

public class LogAuditSender {

        public final static String AUDIT_QUEUE = "local.queue.logAudit";
        public final static String CONNECTION_FACTORY = "jms.ConnectionFactory_LogAudit";
        private static final Log log = LogFactory.getFactory().getInstance(ErrorHandlerSender.class);
        private static InitialContext ctx = null;
        private static Hashtable env = null;
        private static final HashMap<String, JmsServerConnection> jmsServerProperites = GetProperties.getServerProperties();

        public static void sendToQueue(LogAuditCollection logAuditCollection) {

            ConnectionFactory connectionFactory = null;
            Connection connection = null;
            Session session = null;
            MessageProducer messageProducer = null;

            JmsServerConnection jmsServerConnection = jmsServerProperites.get("Local");

            try {
                if (env == null) {
                    env = new Hashtable();
                    env.put(Context.INITIAL_CONTEXT_FACTORY, jmsServerConnection.getContextFactory());
                    env.put(Context.PROVIDER_URL, jmsServerConnection.getUrl());
                    env.put("weblogic.jndi.connectTimeout", new Long(5000));
                    env.put("weblogic.jndi.responseReadTimeout", new Long(5000));

                }

                if (ctx == null) {
                    ctx = new InitialContext(env);
                }

                String mensagem = com.app.logAudit.CreateSoapMessage.returnMessage(logAuditCollection);

                if (mensagem != null) {
                    connectionFactory = (ConnectionFactory) ctx.lookup(CONNECTION_FACTORY);
                    connection = connectionFactory.createConnection();
                    connection.start();
                    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                    Destination queue = (Queue) ctx.lookup(AUDIT_QUEUE);
                    messageProducer = session.createProducer(queue);
                    TextMessage textMessage = session.createTextMessage();
                    textMessage.setText(mensagem);
                    messageProducer.send(textMessage);
                }

            } catch (JMSException | NamingException e) {
                log.error("Send To LogAudit", e);
            } finally {
                try {
                    if (messageProducer != null) {
                        messageProducer.close();
                    }
                } catch (JMSException e) {
                    log.error("Send To LogAudit", e);
                }

                try {
                    if (session != null) {
                        session.close();
                    }
                } catch (JMSException e) {
                    log.error("Send To LogAudit", e);
                }
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (JMSException e) {
                    log.error("Send To LogAudit", e);

                }

            }

        }
}


