/**
 * A lock class, just has a simpler implementation than built-in java locks.
 */
public class BinarySemaphore {
    private boolean locked = false;

    public BinarySemaphore() {
        locked = false;
    }

    public BinarySemaphore(boolean initialValue) {
        locked = initialValue;
    }

    public synchronized void unlock() {
        locked = false;
        notify();
    }

    public synchronized void lock() throws InterruptedException {
        while(locked) wait();
        locked = true;
    }
}
