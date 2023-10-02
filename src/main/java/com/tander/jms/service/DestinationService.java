package com.tander.jms.service;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

public class DestinationService {

    public static Destination getDestination(Session session, Boolean isQueue, String name) throws JMSException {
        return isQueue ? session.createQueue(name) : session.createTopic(name);
    }
}
