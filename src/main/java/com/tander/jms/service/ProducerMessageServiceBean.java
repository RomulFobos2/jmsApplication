package com.tander.jms.service;

import com.tander.jms.connector.ActiveMQConnectorBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.JmsUtils;

import javax.jms.*;

@Getter
@Setter
@AllArgsConstructor
public class ProducerMessageServiceBean {
    private static final Logger logger = LoggerFactory.getLogger(ProducerMessageServiceBean.class);

    ActiveMQConnectorBean activeMQConnectorBean;

    String queueName;
    Boolean isTransaction;
    Boolean isPersistent;
    Integer sessionMode;
    Boolean isQueue;

    public void sendMessageToBroker(String message) {
        logger.info("Try send message (Persistent: " + isPersistent + ")...");
        Session session = null;
        MessageProducer messageProducer = null;
        try {
            session = activeMQConnectorBean.createSession(isTransaction, sessionMode);
            Destination destination = DestinationService.getDestination(session, isQueue, queueName);
            TextMessage textMessage = session.createTextMessage(message);
            messageProducer = session.createProducer(destination);
            messageProducer.setDeliveryMode(isPersistent ? 2 : 1);
            if (isTransaction) {
                sendTransactionMessage(session, messageProducer, textMessage);
            } else {
                sendNonTransactionMessage(session, messageProducer, textMessage);
            }
        } catch (JMSException e) {
            logger.error("Error when creating attributes for sending a message: ", e);
        } finally {
            JmsUtils.closeMessageProducer(messageProducer);
            JmsUtils.closeSession(session);
        }
    }

    public void sendTransactionMessage(Session session, MessageProducer messageProducer, TextMessage textMessage) {
        try {
            messageProducer.send(textMessage);
            logger.info("Message send successfully.");
            session.commit();
        } catch (JMSException e) {
            logger.error("Error while send message: ", e);
            try {
                session.rollback();
            } catch (JMSException ex) {
                logger.error("Error while rollback: ", e);
            }
        }
    }

    public void sendNonTransactionMessage(Session session, MessageProducer messageProducer, TextMessage textMessage) {
        try {
            messageProducer.send(textMessage);
            logger.info("Message send successfully.");
        } catch (JMSException e) {
            logger.error("Error while send message: ", e);
        }
    }
}