package com.app.logErro;

import com.app.bean.LogErroCollection;
import com.app.helper.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CreateSoapMessage {

    private static final Log log = LogFactory.getFactory().getInstance(CreateSoapMessage.class);

    public static String returnMessage(LogErroCollection logErroCollection) {
        String message = null;
        ByteArrayOutputStream stream = null;

        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElementNS("http://xmlns.oracle.com/pcbpel/adapter/db/top/InsereTBLogErro", "LogErroCollection");
            doc.appendChild(rootElement);
            rootElement.appendChild(getLogErro(doc, logErroCollection));
            SOAPMessage soapMsg = createSOAPMessage(doc);
            stream = new ByteArrayOutputStream();
            soapMsg.saveChanges();
            soapMsg.writeTo(stream);
            message = new String(stream.toByteArray(), "utf-8");

        } catch (IOException | ParserConfigurationException | SOAPException | DOMException e) {
            log.error("returnMessage", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    log.error("returnMessage - stream.close()", ex);
                }
            }
        }

        return message;

    }

    private static Node getLogErro(Document doc, LogErroCollection logErroCollection) {
        Element logErroCollectionElement = doc.createElement("LogErro");

        logErroCollectionElement.appendChild(getLogErroElements(doc, logErroCollectionElement, "idTipofluxo", logErroCollection.getIdTipoFluxo()));
        logErroCollectionElement.appendChild(getLogErroElements(doc, logErroCollectionElement, "sequencial", logErroCollection.getSequencial()));
        logErroCollectionElement.appendChild(getLogErroElements(doc, logErroCollectionElement, "stacktrace", logErroCollection.getStacktrace()));
        logErroCollectionElement.appendChild(getLogErroElements(doc, logErroCollectionElement, "idOsa", logErroCollection.getIdOsa()));
        logErroCollectionElement.appendChild(getLogErroElements(doc, logErroCollectionElement, "idConcessionaria", logErroCollection.getIdConcessionaria()));
        logErroCollectionElement.appendChild(getLogErroElements(doc, logErroCollectionElement, "dhmLog", StringUtil.toDateNow()));
        logErroCollectionElement.appendChild(
                getLogErroElements(doc, logErroCollectionElement, "dsMensagem", logErroCollection.getDsMensagem()));
        logErroCollectionElement.appendChild(
                getLogErroElements(doc, logErroCollectionElement, "idPassofluxo", logErroCollection.getIdPassofluxo()));
        logErroCollectionElement
                .appendChild(getLogErroElements(doc, logErroCollectionElement, "serie", logErroCollection.getSerie()));
        logErroCollectionElement.appendChild(
                getLogErroElements(doc, logErroCollectionElement, "reprocessado", logErroCollection.getReprocessado()));

        return logErroCollectionElement;
    }

    private static Node getLogErroElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    private static SOAPMessage createSOAPMessage(Document doc) {

        SOAPMessage soapMsg = null;

        try {

            MessageFactory factory = MessageFactory.newInstance();
            soapMsg = factory.createMessage();
            SOAPPart part = soapMsg.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPHeader header = envelope.getHeader();
            SOAPBody body = envelope.getBody();

            body.addDocument(doc);

        } catch (SOAPException e) {
            log.error("createSOAPMessage", e);
        }

        return soapMsg;
    }

}
