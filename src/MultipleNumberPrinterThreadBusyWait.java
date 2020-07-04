public class MultipleNumberPrinterThreadBusyWait
{
	public static class NumberPrinterThread implements Runnable
	{
		private int		max, modNumber, totalThreads;
		private int[]	currentNumber;

		public NumberPrinterThread(int modNumber, int totalThreads, int max, int[] currentNumber)
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
				while (currentNumber[0] != numberToPrint)
				{
					// busy wait
					// if below 2 statements are commented out, program gets stuck
					System.out
					.println(Thread.currentThread().getName() + " busy comparing "
							+ currentNumber[0] + " and " + numberToPrint);
					//Thread.yield();
				}
				System.out.println(Thread.currentThread().getName() + " printing number");
				System.out.println(String.valueOf(numberToPrint));
				numberToPrint += totalThreads;
				currentNumber[0]++;
				System.out.println(
						Thread.currentThread().getName() + " incremented number");
			}
			System.out.println(Thread.currentThread().getName() + " done!!");
		}
	}

	public static void main(String[] args)
	{
		final int currentNumber[] = new int[]
		{
			1
		};
		int max = 1000, totalThreads = 10;
		for (int i = 1; i <= totalThreads; i++)
		{
			new Thread(new NumberPrinterThread(i, totalThreads, max, currentNumber))
					.start();
		}
	}
}
