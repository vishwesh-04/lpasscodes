import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NTP {
	private double localTime;
	private int pid;
	private Lock lock = new ReentrantLock();
	private Random random = new Random();
	
	NTP(int pid)
	{
		this.pid = pid;
		this.localTime = System.currentTimeMillis() / 1000.0;
	}
	
	double getTime()
	{
		return localTime;
	}
	
	void updateTime(double offset)
	{
		this.localTime += offset;
	}
	
	void sendMsg(NTP recv)
	{
		lock.lock();
		double sendTime = getTime();
		recv.receiveMsg(this.pid, sendTime);
		lock.unlock();
	}

	private void receiveMsg(int pid2, double sendTime) {
		lock.lock();
//		double recvTime = getTime();
		double error = (random.nextDouble() - 0.5) * 1.0;
		double recvTime = getTime() + error;
		double roundttrip = (recvTime - sendTime) / 2;
		double offset = (recvTime - sendTime) - roundttrip;
		System.out.printf("Process %d received message from %d at %.2f. Adjusting time by %.2f seconds.%n", this.pid, pid2, recvTime, offset);
		updateTime(offset);
		lock.unlock();
		
	}
}
