package fr.dr.parser;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.*;

public class Main {

    private static ActiveMQConnectionFactory connectionFactory;
    private static Connection connection;
    private static Session session;
    private static Destination destination;
    private static boolean transacted = false;

    public static void main(String[] args) throws Exception {
        BrokerService broker = new BrokerService();
        broker.setUseJmx(true);
        broker.addConnector("tcp://localhost:61616");
        broker.start();

        setUp();
        createProducerAndSendAMessage();
        System.out.println("Simulating a huge network delay :)");
        Thread.sleep(4000);
        createConsumerAndReceiveAMessage();

        //TODO: Find out how to get rid of the exceptions thrown when stopping the broker
        broker.stop();
    }

    private static void setUp() throws JMSException {
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                ActiveMQConnection.DEFAULT_BROKER_URL);
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);

        destination = session.createQueue("org.apache.camel:context=mt-01.int.sirhen.in.ac-aix-marseille.fr/filtrageGDACamelContext,type=routes,name=\"route-filtrage-gda-CDM\"");
    }

    private static void createProducerAndSendAMessage() throws JMSException {
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        TextMessage message = session.createTextMessage("Hello World!");
        System.out.println("Sending message: " + message.getText());
        producer.send(message);
    }

    private static void createConsumerAndReceiveAMessage() throws JMSException, InterruptedException {
        connection = connectionFactory.createConnection();
        connection.start();
        MessageConsumer consumer = session.createConsumer(destination);
        MyConsumer myConsumer = new MyConsumer();
        connection.setExceptionListener(myConsumer);
        consumer.setMessageListener(myConsumer);
    }

    private static class MyConsumer implements MessageListener, ExceptionListener {

        synchronized public void onException(JMSException ex) {
            System.out.println("JMS Exception occured.  Shutting down client.");
            System.exit(1);
        }

        public void onMessage(Message message) {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("Received message: " + textMessage.getText());
                } catch (JMSException ex) {
                    System.out.println("Error reading message: " + ex);
                }
            } else {
                System.out.println("Received: " + message);
            }
        }
    }
}


