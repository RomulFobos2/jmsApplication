package com.tander.jms.connector;

import lombok.Getter;
import lombok.Setter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

@Getter
@Setter
public class ActiveMQConnectorBean {
    private static final Logger logger = LoggerFactory.getLogger(ActiveMQConnectorBean.class);
    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;

    public ActiveMQConnectorBean(ActiveMQConnectionFactory connectionFactory) throws JMSException {
        this.connectionFactory = connectionFactory;
        connection = connectionFactory.createConnection();
    }

    public Session createSession(boolean isTransaction, int sessionMode){
        Session session = null;
        try {
            session = connection.createSession(isTransaction, sessionMode);
            if(session == null) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            logger.error("Error create session: ", e);
            System.exit(-1);
        }
        SessionMode enumMode = SessionMode.values()[sessionMode];
        logger.info("Info about session: Transaction - " + isTransaction + ", SessionMode - " + enumMode.name());
        return session;
    }

    public enum SessionMode {
        SESSION_TRANSACTED,
        AUTO_ACKNOWLEDGE,
        CLIENT_ACKNOWLEDGE,
        DUPS_OK_ACKNOWLEDGE
    }
}
