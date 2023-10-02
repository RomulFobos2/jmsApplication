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
public class ConsumerMessageServiceBean{
    private static final Logger logger = LoggerFactory.getLogger(ConsumerMessageServiceBean.class);

    ActiveMQConnectorBean activeMQConnectorBean;

    String queueName;
    Boolean isTransaction;
    Integer sessionMode;
    Boolean isQueue;

    public void readQueue() {
        logger.info("Try reading message...");
        if(!isQueue){
            logger.error("Program can't read massage from topic.");
            return;
        }
        Session session = null;
        MessageConsumer messageConsumer = null;
        try {
            session = activeMQConnectorBean.createSession(isTransaction, sessionMode);
            Destination destination = DestinationService.getDestination(session, isQueue, queueName);
            messageConsumer = session.createConsumer(destination);
            if (isTransaction) {
                readTransactionMessage(session, messageConsumer);
            } else {
                readNonTransactionMessage(session, messageConsumer);
            }
        } catch (JMSException e) {
            logger.error("Error when creating attributes for reading a message: ", e);
        }
        finally {
            JmsUtils.closeMessageConsumer(messageConsumer);
            JmsUtils.closeSession(session);
        }
    }

    public void readTransactionMessage(Session session, MessageConsumer messageConsumer) {
        try {
            activeMQConnectorBean.getConnection().start();
            Message message = messageConsumer.receive();
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                logger.info("Receive message: " + ((TextMessage) message).getText());
                session.commit();
            } else {
                logger.error("Unsupported message received.");
            }
        } catch (JMSException e) {
            logger.error("Error while read message: ", e);
            try {
                session.rollback();
            } catch (JMSException ex) {
                logger.error("Error while rollback: ", e);
            }
        }
    }

    public void readNonTransactionMessage(Session session, MessageConsumer messageConsumer) {
        try {
            activeMQConnectorBean.getConnection().start();
            Message message = messageConsumer.receive();
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                logger.info("Receive message: " + ((TextMessage) message).getText());
            } else {
                logger.error("Unsupported message received.");
            }
            if (sessionMode == Session.CLIENT_ACKNOWLEDGE) {
                message.acknowledge();
            }
        } catch (JMSException e) {
            logger.error("Error while read message: ", e);
        }
    }
}