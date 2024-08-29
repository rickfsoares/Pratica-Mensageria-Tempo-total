package br.edy.ifpb;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.Date;
/**
 * Produtor
 */
public class Produtor {

   public static void main(String[] args) throws Exception {
        ConnectionFactory connFactory = new ConnectionFactory();

        connFactory.setHost("localhost");
        connFactory.setPort(5672);
        connFactory.setUsername("mqadmin");
        connFactory.setPassword("Admin123XX_");

        //String QUEUE_NAME = "milhao";
        //String QUEUE_NAME = "nduravel-npersistente";
        //String QUEUE_NAME = "duravel-npersistente";
        String QUEUE_NAME = "duravel-persistente";
        try (
                Connection conn = connFactory.newConnection();
                Channel channel = conn.createChannel();
            ) {
                channel.queueDeclare(QUEUE_NAME, true, false, false, null);

                for (int i = 1; i <= 1000000; i++) {
                    long timestamp = System.currentTimeMillis();
                    String msg =  i + "-" + timestamp;

                    channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());

                    System.out.println("Send msg: " + msg);
                }

            }
    } 
}
