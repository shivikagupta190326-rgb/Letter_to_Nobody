package ocean.service;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

/** Bounded producer-consumer example using wait/notifyAll guarded by one monitor. */
public class ProducerConsumerDemo {
    private final Queue<String> buffer = new LinkedList<>();
    private final int capacity = 3;

    private synchronized void put(String item) throws InterruptedException {
        while (buffer.size() == capacity) {
            wait(); // Release this object's monitor until a consumer makes room.
        }
        buffer.add(item);
        notifyAll(); // Wake consumers waiting for a bottle.
    }

    private synchronized String take() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait(); // Guarded block: always re-check the condition after waking.
        }
        String item = buffer.remove();
        notifyAll(); // Wake producers waiting for free space.
        return item;
    }

    public void start(Consumer<String> log, Runnable finished) {
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    String bottle = "Bottle " + i;
                    put(bottle);
                    log.accept("Producer released " + bottle);
                }
                put("DONE");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.accept("Producer stopped.");
            }
        }, "Bottle-Producer");

        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    String bottle = take();
                    if ("DONE".equals(bottle)) break;
                    log.accept("Consumer discovered " + bottle);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.accept("Consumer stopped.");
            } finally {
                finished.run();
            }
        }, "Bottle-Consumer");
        producer.start();
        consumer.start();
    }
}
