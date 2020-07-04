import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultipleNumberPrinterThreadUsingMultipleWaitQueuesOnLockCondition
{
	public static class NumberPrinterThread implements Runnable
	{

		private Lock		lock;
		private int			max, modNumber, totalThreads;
		private int[]		currentNumber;
		private Condition	conditions[];

		public NumberPrinterThread(Lock lock, Condition conditions[], int modNumber, int totalThreads, int max, int[] currentNumber)
		{
			this.modNumber = modNumber;
			this.max = max;
			this.currentNumber = currentNumber;
			this.lock = lock;
			this.totalThreads = totalThreads;
			this.conditions = conditions;
		}

		@Override
		public void run()
		{
			int numberToPrint = modNumber;
			while (numberToPrint <= max)
			{
				try
				{
					lock.lock();
					while (currentNumber[0] != numberToPrint)
					{
						try
						{
							System.out.println(
									Thread.currentThread().getName() + " waiting on condition");
							conditions[modNumber-1].await();
							System.out.println(
									Thread.currentThread().getName() + " waked up");
						}
						catch (InterruptedException e)
						{
							StringWriter sw = new StringWriter();
							e.printStackTrace(new PrintWriter(sw));
							System.out.println("Thread: " + modNumber + " interuppted\n"
									+ sw.toString());
						}
					}
					System.out.println(String.valueOf(numberToPrint));
					numberToPrint += totalThreads;
					currentNumber[0]++;
					System.out.println(
							Thread.currentThread().getName() + " calling signal");
					conditions[modNumber==totalThreads?0:modNumber].signal();
				}
				finally
				{
					lock.unlock();
				}
			}
			System.out.println(Thread.currentThread().getName()+" done!!");
		}
	}

	public static void main(String[] args)
	{
		Lock lock = new ReentrantLock();
		int currentNumber[] = new int[]
		{
			1
		}, max = 1000, totalThreads = 5;
		
		Condition conditions[] = new Condition[totalThreads];
		
		for (int i = 0; i < totalThreads; i++)
		{
			conditions[i]=lock.newCondition();
		}
		for (int i = 1; i <= totalThreads; i++)
		{
			new Thread(new NumberPrinterThread(lock, conditions, i, totalThreads, max, currentNumber))
					.start();
		}
	}
}
