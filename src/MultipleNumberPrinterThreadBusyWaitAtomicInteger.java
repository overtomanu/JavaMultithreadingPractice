import java.util.concurrent.atomic.AtomicInteger;

public class MultipleNumberPrinterThreadBusyWaitAtomicInteger
{
	public static class NumberPrinterThread implements Runnable
	{
		private int		max, modNumber, totalThreads;
		private AtomicInteger	currentNumber;

		public NumberPrinterThread(int modNumber, int totalThreads, int max, AtomicInteger currentNumber)
		{
			this.modNumber = modNumber;
			this.max = max;
			this.currentNumber = currentNumber;
			this.totalThreads = totalThreads;
		}

		@Override
		public void run()
		{
			System.out.println(Thread.currentThread().getName() + " started running");
			int numberToPrint = modNumber;
			while (numberToPrint <= max)
			{
				while (currentNumber.get() != numberToPrint)
				{
					// busy wait
					/*System.out
					.println(Thread.currentThread().getName() + " busy comparing "
							+ currentNumber.get() + " and " + numberToPrint);
					Thread.yield();*/
				}
				System.out.println(Thread.currentThread().getName() + " printing number");
				System.out.println(String.valueOf(numberToPrint));
				numberToPrint += totalThreads;
				currentNumber.incrementAndGet();
				System.out.println(
						Thread.currentThread().getName() + " incremented number");
			}
			System.out.println(Thread.currentThread().getName() + " done!!");
		}
	}

	public static void main(String[] args)
	{
		AtomicInteger currentNumber = new AtomicInteger();
		currentNumber.set(1);
		int max = 1000, totalThreads = 10;
		for (int i = 1; i <= totalThreads; i++)
		{
			new Thread(new NumberPrinterThread(i, totalThreads, max, currentNumber))
					.start();
		}
	}
}
