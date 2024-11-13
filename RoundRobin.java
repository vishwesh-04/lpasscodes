import java.util.*;

public class RoundRobin {
	static void schedule(Process[] processes, int quantum)
	{
		Queue<Process> queue = new LinkedList<Process>();
		int n = processes.length;
		int currTime = 0;
		int completed  = 0;
		int[] ct = new int[n];
		
		queue.addAll(Arrays.asList(processes));
		
		while(completed < n)
		{
			Process currProc = queue.poll();
			if(currProc.rt > 0)
			{
				if(currProc.rt <= quantum)
				{
					currTime += currProc.rt;
					currProc.rt = 0;
					ct[currProc.pid - 1] = currTime;
					currProc.tat = ct[currProc.pid-1] - currProc.at;
					currProc.wt = currProc.tat - currProc.bt;
					completed++;
				}
				else
				{
					currTime += quantum;
					currProc.rt -= quantum;
					queue.add(currProc);
				}
			}
		}
		
		System.out.println("Round Robin (Preemptive) Scheduling Results:");
        System.out.println("PID\tArrival Time\tBurst Time\tWaiting Time\tTurnaround Time");
        for (Process p : processes) {
            System.out.println(p.pid + "\t" + p.at + "\t\t" + p.bt + "\t\t" + p.wt + "\t\t" + p.tat);
        }
	}
}
