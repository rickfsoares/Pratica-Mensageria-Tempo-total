package br.edy.ifpb;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * Consumidor
 */
public class Consumidor {

    public static void main(String[] args) throws Exception {
        //String QUEUE_NAME = "milhao";
        //String QUEUE_NAME = "nduravel-npersistente";
        //String QUEUE_NAME = "duravel-npersistente";
        String QUEUE_NAME = "duravel-persistente";
        ConnectionFactory connFactory = new ConnectionFactory();
        connFactory.setHost("localhost");

        connFactory.setUsername("mqadmin");
        connFactory.setPassword("Admin123XX_");

        try (
            Connection conn = connFactory.newConnection();
            Channel channel = conn.createChannel();
            ) {

                channel.queueDeclare(QUEUE_NAME, true, false, false, null);

                DeliverCallback callback = (consumerTag, delivery) -> {
                    try {
                        String msg = new String(delivery.getBody());

                        String[] parts = msg.split("-");
                        String numberIteration = parts[0];
                        long tmp = Long.parseLong(parts[1]);

                        long currentTmp = System.currentTimeMillis();

                        long diff = currentTmp - tmp;

                        System.out.println("Received msg: " + numberIteration + " - " + diff);
                    } finally {
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    }

                };

                channel.basicConsume(QUEUE_NAME, false, callback, consumerTag -> {});
                System.out.println("Consumidor iniciado e aguardando mensagens...");

                // Manter o programa rodando indefinidamente
                synchronized (Consumidor.class) {
                    Consumidor.class.wait();
                }
            }

        }
}
