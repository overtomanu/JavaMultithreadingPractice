public class MultipleNumberPrinterThreadBusyWaitOnVolatileInt
{
	// if below volatile keyword is removed, program most likely becomes stuck 
	public static volatile int currentNumber = 1;
	public static class NumberPrinterThread implements Runnable
	{
		private int		max, modNumber, totalThreads;

		public NumberPrinterThread(int modNumber, int totalThreads, int max)
		{
			this.modNumber = modNumber;
			this.max = max;
			this.totalThreads = totalThreads;
		}

		@Override
		public void run()
		{
			System.out.println(Thread.currentThread().getName() + " started running");
			int numberToPrint = modNumber;
			while (numberToPrint <= max)
			{
				while (currentNumber != numberToPrint)
				{
					// busy wait
					// if below 2 statements are commented out, program gets stuck
					/*System.out
					.println(Thread.currentThread().getName() + " busy comparing "
							+ currentNumber + " and " + numberToPrint);*/
					//Thread.yield();
				}
				System.out.println(Thread.currentThread().getName() + " printing number");
				System.out.println(String.valueOf(numberToPrint));
				numberToPrint += totalThreads;
				//incremented value may not become visible for other threads
				currentNumber++;
				System.out.println(
						Thread.currentThread().getName() + " incremented number");
			}
			System.out.println(Thread.currentThread().getName() + " done!!");
		}
	}

	public static void main(String[] args)
	{
		int max = 1000, totalThreads = 10;
		for (int i = 1; i <= totalThreads; i++)
		{
			new Thread(new NumberPrinterThread(i, totalThreads, max))
					.start();
		}
	}
}
