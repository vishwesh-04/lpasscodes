package Mock_II.DS3;

import java.util.ArrayList;
import java.util.List;

class BullyProcess {
    private int id;
    private boolean isCoordinator;

    public BullyProcess(int id) {
        this.id = id;
        this.isCoordinator = false;
    }

    public int getId() {
        return id;
    }

    public boolean isCoordinator() {
        return isCoordinator;
    }

    public void setCoordinator(boolean isCoordinator) {
        this.isCoordinator = isCoordinator;
    }

    public void startElection(List<BullyProcess> processes) {
        System.out.println("Process " + id + " started an election.");
        for (BullyProcess p : processes) {
            if (p.getId() > id) {
                System.out.println("Process " + id + " sent election message to " + p.getId());
                return;
            }
        }
        // If no higher ID process responded, become the coordinator
        setCoordinator(true);
        System.out.println("Process " + id + " is the new coordinator.");
    }

    public void receiveElection() {
        System.out.println("Process " + id + " received an election message.");
    }
}

public class BullyElection {
    public static void main(String[] args) {
        int numProcesses = 5;
        List<BullyProcess> processes = new ArrayList<>();

        for (int i = 1; i <= numProcesses; i++) {
            processes.add(new BullyProcess(i));
        }

        // Simulate a failure of the current coordinator (e.g., process 5)
        processes.get(0).startElection(processes);
    }
}
