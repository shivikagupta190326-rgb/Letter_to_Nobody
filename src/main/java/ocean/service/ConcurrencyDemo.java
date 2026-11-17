package ocean.service;

import javax.swing.SwingUtilities;
import java.util.function.Consumer;

/**
 * A small classroom example of threads, synchronization, and deadlock prevention.
 * Both worker threads acquire the two locks in the same order, so they cannot
 * each hold one lock while waiting forever for the other.
 */
public class ConcurrencyDemo {
    private int sharedCount = 0;
    private final Object firstLock = new Object();
    private final Object secondLock = new Object();

    // synchronized lets only one thread update sharedCount at a time.
    private synchronized void addOne() {
        sharedCount++;
    }

    public void start(Consumer<String> showLine, Runnable finished) {
        sharedCount = 0;
        showLine.accept("Starting two worker threads...");

        Thread waveOne = new Thread(() -> work("Wave 1", showLine), "Wave-1");
        Thread waveTwo = new Thread(() -> work("Wave 2", showLine), "Wave-2");
        waveOne.start();
        waveTwo.start();

        // A third thread waits for both workers without freezing Swing.
        new Thread(() -> {
            try {
                waveOne.join();
                waveTwo.join();
                showLine.accept("Both threads finished. Shared count = " + sharedCount);
                finished.run();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                showLine.accept("The demo was interrupted.");
                finished.run();
            }
        }, "Demo-Waiter").start();
    }

    private void work(String name, Consumer<String> showLine) {
        for (int i = 0; i < 1000; i++) {
            addOne();
        }

        // Deadlock prevention: always lock firstLock, then secondLock.
        synchronized (firstLock) {
            synchronized (secondLock) {
                showLine.accept(name + " finished 1,000 updates and used the locks safely.");
            }
        }
    }

    /** Swing components must be changed on Swing's event-dispatch thread. */
    public static Consumer<String> swingLogger(javax.swing.JTextArea output) {
        return line -> SwingUtilities.invokeLater(() -> output.append(line + "\n"));
    }
}
