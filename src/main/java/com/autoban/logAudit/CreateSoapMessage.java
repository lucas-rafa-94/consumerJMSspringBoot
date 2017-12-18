package com.autoban.logAudit;

import com.autoban.errorHandler.LogErroCollection;
import com.autoban.helper.StringUtil;
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

public class CreateSoapMessage    {

    private static final Log log = LogFactory.getFactory().getInstance(com.autoban.errorHandler.CreateSoapMessage.class);

        public static String returnMessage(LogAuditCollection logAuditCollection) {
            String message = null;
            ByteArrayOutputStream stream = null;

            try {

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.newDocument();
                Element rootElement = doc.createElementNS("http://xmlns.oracle.com/pcbpel/adapter/db/top/InsereDBLogAudit", "LogAuditCollection");
                doc.appendChild(rootElement);
                rootElement.appendChild(getLogAudit(doc, logAuditCollection));
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

        private static Node getLogAudit(Document doc, LogAuditCollection logAuditCollection) {
            Element logAuditCollectionElement = doc.createElement("LogAudit");

            logAuditCollectionElement.appendChild(getLogErroElements(doc, logAuditCollectionElement, "sequencial", logAuditCollection.getSequencial()));
            logAuditCollectionElement.appendChild(getLogErroElements(doc, logAuditCollectionElement, "idTipofluxo", logAuditCollection.getIdTipoFluxo()));
            logAuditCollectionElement.appendChild(getLogErroElements(doc, logAuditCollectionElement, "dhmLog", StringUtil.toDateNow()));
            logAuditCollectionElement.appendChild(getLogErroElements(doc, logAuditCollectionElement, "idOsa", logAuditCollection.getIdOsa()));
            logAuditCollectionElement.appendChild(getLogErroElements(doc, logAuditCollectionElement, "idConcessionaria", logAuditCollection.getIdConcessionaria()));
            logAuditCollectionElement.appendChild(getLogErroElements(doc, logAuditCollectionElement, "serie", logAuditCollection.getSerie()));
            logAuditCollectionElement.appendChild(getLogErroElements(doc, logAuditCollectionElement, "idPassofluxo", logAuditCollection.getIdPassofluxo()));

            return logAuditCollectionElement;
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

