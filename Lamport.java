
public class Lamport {
	int pid;
	int clock;
	
	public Lamport(int pid) {
		this.pid = pid;
		this.clock = 0;
	}
	
	int getTime()
	{
		return this.clock;
	}
	
	void incClk()
	{
		this.clock++;
	}
	
	void send(Lamport recv)
	{
		incClk();
		int sendTime = getTime();
		receive(this.pid, sendTime);
	}
	
	void receive(int pid2, int sendTime)
	{
		this.clock = Math.max(this.clock, sendTime) + 1;
		 System.out.printf("Process %d received message from %d with time %d. Adjusting clock to %d.%n", this.pid, pid2, sendTime, clock);
	}

}
