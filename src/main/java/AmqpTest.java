import org.apache.qpid.jms.JmsConnectionFactory;

import javax.jms.*;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

public class AmqpTest {
    public static void main(String[] args) throws Exception{
        String url = "amqps://169.254.154.214:5672";
        String queueName = "";
        JmsConnectionFactory jmsConnectionFactory = new JmsConnectionFactory(url);
        jmsConnectionFactory.setSslContext(getSslContext());
        JMSContext jmsContext = jmsConnectionFactory.createContext(Session.AUTO_ACKNOWLEDGE);
        JMSProducer jmsProducer = jmsContext.createProducer();
        Destination destination = jmsContext.createQueue(queueName);
        jmsContext.start();
        Message testMessage = jmsContext.createTextMessage("test1");
        System.out.println(">> Sending a message");
        jmsProducer.send(destination,testMessage);
        System.out.println(">> Sent the message");
        jmsContext.close();

    }

    public static SSLContext getSslContext() throws Exception{
        String keystoreFile = "";
        String trustStoreFile = "";
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(keystoreFile),"".toCharArray());
        keyManagerFactory.init(keyStore,"".toCharArray());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(trustStoreFile),"".toCharArray());
        trustManagerFactory.init(trustStore);
        sslContext.init(keyManagerFactory.getKeyManagers(),trustManagerFactory.getTrustManagers(),new SecureRandom());
        return sslContext;
    }
}
