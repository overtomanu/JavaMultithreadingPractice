import java.io.PrintWriter;
import java.io.StringWriter;

public class MultipleNumberPrinterThreadUsingWaitNotifyOnLockObject
{
	public static class NumberPrinterThread implements Runnable
	{

		private Object	lock;
		private int		max, modNumber, totalThreads;
		private int[]	currentNumber;

		public NumberPrinterThread(Object lock, int modNumber, int totalThreads, int max, int[] currentNumber)
		{
			this.modNumber = modNumber;
			this.max = max;
			this.currentNumber = currentNumber;
			this.lock = lock;
			this.totalThreads = totalThreads;
		}

		@Override
		public void run()
		{
			int numberToPrint = modNumber;
			while (numberToPrint <= max)
			{
				synchronized (lock)
				{
					while (currentNumber[0] != numberToPrint)
					{
						try
						{
							System.out.println(Thread.currentThread().getName()+" waiting");
							lock.wait();
							System.out.println(Thread.currentThread().getName()+" waked up");
							//Uncomment for single notify
							/*if(currentNumber[0] != numberToPrint) {
								lock.notify();
							}*/
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
					//Uncomment below for single notify implementation and comment out notifyAll
					//lock.notify();
					System.out.println(Thread.currentThread().getName()+" calling notifyAll");
					lock.notifyAll();
				}
			}
			System.out.println(Thread.currentThread().getName()+" done!!");
		}
	}

	public static void main(String[] args)
	{
		Object lock = new Object();
		int currentNumber[] = new int[]
		{
			1
		}, max = 121, totalThreads = 7;
		for (int i = 1; i <= totalThreads; i++)
		{
			new Thread(new NumberPrinterThread(lock, i, totalThreads, max, currentNumber)).start();
		}
	}
}
