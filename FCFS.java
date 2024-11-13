import java.util.*;

public class FCFS {
	static void schedule(Process[] processes)
	{
		Arrays.sort(processes, Comparator.comparingInt(p -> p.at));
		
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
		
		System.out.println("FCFS Schduling Result");
		System.out.println("PID\tArrival Time\tBurst Time\tWaiting Time\tTurnaround Time");
		for (Process p : processes) {
			System.out.println(p.pid + "\t" + p.at + "\t\t" + p.bt + "\t\t" + p.wt + "\t\t" + p.tat);
		}
	}
}
