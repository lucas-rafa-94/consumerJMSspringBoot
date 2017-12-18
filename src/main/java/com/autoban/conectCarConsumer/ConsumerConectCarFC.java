package com.autoban.conectCarConsumer;

import com.autoban.helper.SendMessageFactory;
import com.autoban.helper.StringUtil;
import com.autoban.logAudit.LogAuditCollection;
import com.autoban.logAudit.LogAuditSender;


import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ConsumerConectCarFC implements MessageListener {
    public void onMessage(Message message) {
        if (message instanceof BytesMessage) {
            try {
                LogAuditCollection logAuditCollection = new LogAuditCollection();
                logAuditCollection.setIdConcessionaria("1012");
                logAuditCollection.setIdOsa("3");
                logAuditCollection.setIdTipoFluxo("5");
                logAuditCollection.setIdPassofluxo("0");
                logAuditCollection.setDhmLog(StringUtil.toDateNow());
                logAuditCollection.setSerie("0");
                logAuditCollection.setDsMensagem(((BytesMessage) message).readUTF());
                logAuditCollection.setSequencial("0");
                LogAuditSender.sendToQueue(logAuditCollection);
                SendMessageFactory.sendToQueue((BytesMessage) message, "FalhaComunicacaoLocalOSA31012");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
