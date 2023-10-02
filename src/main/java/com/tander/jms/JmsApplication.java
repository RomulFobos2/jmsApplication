package com.tander.jms;

import com.tander.jms.connector.ActiveMQConnectorBean;
import com.tander.jms.service.ConsumerMessageServiceBean;
import com.tander.jms.service.MessageServiceBean;
import com.tander.jms.service.ProducerMessageServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StopWatch;

import javax.jms.JMSException;

@SpringBootApplication
public class JmsApplication {
    private static final Logger logger = LoggerFactory.getLogger(JmsApplication.class);

    public static void main(String[] args) throws JMSException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        MessageServiceBean messageServiceBean = context.getBean(MessageServiceBean.class);

        ProducerMessageServiceBean producerMessageServiceBean = context.getBean(ProducerMessageServiceBean.class);
        producerMessageServiceBean.sendMessageToBroker(messageServiceBean.createTextMessage());

        ConsumerMessageServiceBean consumerMessageServiceBean = context.getBean(ConsumerMessageServiceBean.class);
        consumerMessageServiceBean.readQueue();

        ActiveMQConnectorBean activeMQConnectorBean = context.getBean(ActiveMQConnectorBean.class);
        activeMQConnectorBean.getConnection().close();

        stopWatch.stop();
        logger.info("Lead time: " + stopWatch.getTotalTimeMillis() + " ms");
    }
}
