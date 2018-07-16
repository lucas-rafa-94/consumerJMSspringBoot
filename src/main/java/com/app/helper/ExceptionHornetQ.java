package com.app.helper;

import com.app.bean.JmsConnectionFactory;
import com.app.bean.JmsServerConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import static com.app.helper.ConsumerImpl.stopConnection;

public class ExceptionHornetQ implements ExceptionListener {
    private static final Log log = LogFactory.getFactory().getInstance(ExceptionHornetQ.class);

    private final String fluxo;
    private final JmsConnectionFactory jmsConnectionFactory;
    private final JmsServerConnection jmsServerConnection;

    public ExceptionHornetQ( String fluxo, JmsConnectionFactory jmsConnectionFactory, JmsServerConnection jmsServerConnection) {

        this.fluxo = fluxo;
        this.jmsConnectionFactory = jmsConnectionFactory;
        this.jmsServerConnection = jmsServerConnection;
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public void onException(JMSException jmse) {

        ConsumerImpl consumerImpl= new ConsumerImpl();
        try {
            // Make sure connection is closed
            consumerImpl.stopConnection();

        } catch (Exception ex) {
            log.error("Exception making sure hornet connection " + fluxo + " is closed", ex);
        }
        consumerImpl.startConnectionFactory(fluxo, jmsConnectionFactory, jmsServerConnection);

    }
}