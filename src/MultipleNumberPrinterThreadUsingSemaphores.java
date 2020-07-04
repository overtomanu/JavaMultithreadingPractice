import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Semaphore;

public class MultipleNumberPrinterThreadUsingSemaphores
{
	public static class NumberPrinterThread implements Runnable
	{
		private int			max, modNumber, totalThreads;
		private Semaphore	semaphores[];

		public NumberPrinterThread(Semaphore semaphores[], int modNumber, int totalThreads, int max)
		{
			this.modNumber = modNumber;
			this.max = max;
			this.totalThreads = totalThreads;
			this.semaphores = semaphores;
		}

		@Override
		public void run()
		{
			int numberToPrint = modNumber;
			while (numberToPrint <= max)
			{
				System.out.println(Thread.currentThread().getName()
						+ " trying to acquire semaphore");
				try
				{
					semaphores[modNumber - 1].acquire();
				}
				catch (InterruptedException e)
				{
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					System.out.println(
							"Thread: " + modNumber + " interuppted\n" + sw.toString());
					continue;
				}
				System.out.println(
						Thread.currentThread().getName() + " semaphore acquired");
				System.out.println(String.valueOf(numberToPrint));
				numberToPrint += totalThreads;
				// currentNumber[0]++;
				System.out.println(
						Thread.currentThread().getName() + " releasing next semaphore");
				semaphores[modNumber == totalThreads ? 0 : modNumber].release();
			}
			System.out.println(Thread.currentThread().getName() + " done!!");
		}
	}

	public static void main(String[] args)
	{
		int max = 1000, totalThreads = 5;

		Semaphore semaphores[] = new Semaphore[totalThreads];

		for (int i = 0; i < totalThreads; i++)
		{
			semaphores[i] = new Semaphore(0);
		}
		for (int i = 1; i <= totalThreads; i++)
		{
			new Thread(new NumberPrinterThread(semaphores, i, totalThreads, max)).start();
		}
		try
		{
			Thread.sleep(3000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName()
				+ ": Main thread releasing first semaphore");
		semaphores[0].release();
		System.out.println(Thread.currentThread().getName()
				+ ": Main thread exit");
	}

}
