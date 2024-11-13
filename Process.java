
public class Process {
	
	int pid, at, bt, rt, prior, wt, tat;
	
	Process(int pid, int at, int bt, int prior)
	{
		this.pid = pid;
		this.at = at;
		this.bt = bt;
		this.rt = bt;
		this.prior = prior;
		this.wt = 0;
		this.tat = 0;
	}

}
