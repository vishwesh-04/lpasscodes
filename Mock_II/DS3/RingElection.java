package Mock_II.DS3;

import java.util.ArrayList;
import java.util.List;

class Process {
    private int id;
    private boolean isCoordinator;

    public Process(int id) {
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

    public void sendElection(List<Process> processes) {
        int maxId = id;
        for (Process p : processes) {
            if (p.getId() > id) {
                maxId = Math.max(maxId, p.getId());
            }
        }
        if (maxId == id) {
            setCoordinator(true);
            System.out.println("Process " + id + " is the new coordinator.");
        } else {
            System.out.println("Process " + id + " sent election message to " + maxId);
        }
    }
}

public class RingElection {
    public static void main(String[] args) {
        int numProcesses = 5;
        List<Process> processes = new ArrayList<>();

        for (int i = 1; i <= numProcesses; i++) {
            processes.add(new Process(i));
        }

        // Start the election from process 1
        processes.get(0).sendElection(processes);
    }
}
