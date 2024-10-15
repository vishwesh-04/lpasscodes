package Mock_II.DS4;

public class LamportClock {
    private int counter;

    public LamportClock() {
        this.counter = 0;
    }

    public void tick() {
        counter++;
    }

    public void sendEvent() {
        tick();
        System.out.println("Sending event with counter: " + counter);
    }

    public void receiveEvent(int receivedCounter) {
        counter = Math.max(counter, receivedCounter) + 1;
        System.out.println("Received event, updated counter: " + counter);
    }

    public int getCounter() {
        return counter;
    }

    public static void main(String[] args) {
        LamportClock clock1 = new LamportClock();
        LamportClock clock2 = new LamportClock();

        // Simulate sending and receiving events
        clock1.sendEvent(); // Process 1 sends an event
        clock2.receiveEvent(clock1.getCounter()); // Process 2 receives the event
    }
}

