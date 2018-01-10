package com.app.conectCarConsumer;


import com.app.helper.SendMessageFactory;
import com.app.helper.StringUtil;
import com.app.bean.LogAuditCollection;
import com.app.logAudit.LogAuditSender;

import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ConsumerConectCarRI implements MessageListener {
    public void onMessage(Message message) {
        if (message instanceof BytesMessage) {
            try {
                LogAuditCollection logAuditCollection = new LogAuditCollection();
                logAuditCollection.setIdConcessionaria("1024");
                logAuditCollection.setIdOsa("3");
                logAuditCollection.setIdTipoFluxo("6");
                logAuditCollection.setIdPassofluxo("0");
                logAuditCollection.setDhmLog(StringUtil.toDateNow());
                logAuditCollection.setSerie("0");
                logAuditCollection.setDsMensagem(message.toString());
                logAuditCollection.setSequencial("0");
                LogAuditSender.sendToQueue(logAuditCollection);
                SendMessageFactory.sendToQueue((BytesMessage) message, "RequisitaImagemLocalConc1024OSA3");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}