import java.util.*;

public class Priority {
	static void schedule(Process[] processes)
	{
		Arrays.sort(processes, (p1, p2) -> p1.at == p2.at ? p1.prior - p2.prior : p1.at - p2.at);
		
		int currTime = 0;
		for (Process process : processes) {
			if(currTime < process.at)
			{
				currTime = process.at;
			}
			
			process.wt = currTime - process.at;
			currTime += process.bt;
			process.tat = process.wt - process.bt;
		}
		
		System.out.println("Priority Scheduling (Non-Preemptive) Results:");
        System.out.println("PID\tArrival Time\tBurst Time\tPriority\tWaiting Time\tTurnaround Time");
        for (Process p : processes) {
            System.out.println(p.pid + "\t" + p.at + "\t\t" + p.bt + "\t\t" + p.prior + "\t\t" + p.wt + "\t\t" + p.tat);
        }
	}

}
