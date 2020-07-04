public class PrintEvenOddumbersWithSynchronized
{
	public static class EvenPrinterThread implements Runnable
	{
		private Boolean[]	isCurrentPrintNumberEven;
		private int			max;

		public EvenPrinterThread(Boolean[] isCurrentPrintNumberEven, int max)
		{
			this.isCurrentPrintNumberEven = isCurrentPrintNumberEven;
			this.max = max;
		}

		@Override
		public void run()
		{
			int i = 0;
			while (i < max/2)
			{
				synchronized (isCurrentPrintNumberEven)
				{
					if (isCurrentPrintNumberEven[0])
					{
						System.out.print((2 * i+2) + ",");
						isCurrentPrintNumberEven[0] = !isCurrentPrintNumberEven[0];
						i++;
					}
				}
			}
		}
	}

	public static class OddPrinterThread implements Runnable
	{
		private Boolean[]	isCurrentPrintNumberEven;
		private int			max;

		public OddPrinterThread(Boolean[] isCurrentPrintNumberEven, int max)
		{
			this.isCurrentPrintNumberEven = isCurrentPrintNumberEven;
			this.max = max;
		}

		@Override
		public void run()
		{
			int i = 0;
			while (i < max/2)
			{
				synchronized (isCurrentPrintNumberEven)
				{
					if (!isCurrentPrintNumberEven[0])
					{
						System.out.print(2 * i + 1 + ",");
						isCurrentPrintNumberEven[0] = !isCurrentPrintNumberEven[0];
						i++;
					}
				}
			}
		}
	}

	public static void main(String[] args)
	{
		Boolean isCurrentPrintNumberEven[] = new Boolean[]
		{
			Boolean.FALSE
		};
		new Thread(new EvenPrinterThread(isCurrentPrintNumberEven, 100)).start();
		new Thread(new OddPrinterThread(isCurrentPrintNumberEven, 100)).start();
	}
}
