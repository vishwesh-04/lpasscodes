import java.util.*;

public class SJF {
	static void schedule(Process[] processes)
	{
		Arrays.sort(processes, Comparator.comparingInt(p -> p.at));
		
		int n = processes.length;
		int currTime = 0;
		int completed = 0;
		int[] ct = new int[n];
		
		while(completed < n)
		{
			int minrt = Integer.MAX_VALUE;
			int processIndex = -1;
			
			for(int i=0;i<n;i++)
			{
				if(processes[i].at <= currTime && processes[i].rt > 0 && processes[i].rt < minrt)
				{
					minrt = processes[i].rt;
					processIndex = i;
				}
			}
			
			if(processIndex == -1)
			{
				currTime++;
				continue;
			}
			
			processes[processIndex].rt--;
			currTime++;
			
			if(processes[processIndex].rt == 0)
			{
				completed++;
				ct[processIndex] = currTime;
				processes[processIndex].tat = ct[processIndex] - processes[processIndex].at;
				processes[processIndex].wt = processes[processIndex].tat - processes[processIndex].bt;
			}
		}
		
		System.out.println("SJF (Preemptive) Scheduling Results:");
        System.out.println("PID\tArrival Time\tBurst Time\tWaiting Time\tTurnaround Time");
        for (Process p : processes) {
        	System.out.println(p.pid + "\t" + p.at + "\t\t" + p.bt + "\t\t" + p.wt + "\t\t" + p.tat);
        }
	}
}
