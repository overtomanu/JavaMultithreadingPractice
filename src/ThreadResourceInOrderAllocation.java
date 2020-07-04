import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class ThreadResourceInOrderAllocation
{
	static class ThreadTask implements Runnable
	{
		private int resLeftToAcquire;

		public ThreadTask(int resLeftToAcquire)
		{
			this.resLeftToAcquire = resLeftToAcquire;
		}

		@Override
		public void run()
		{
			while (resLeftToAcquire > 0)
			{
				Resource res;
				try
				{
					res = Resource.acquireResource();
					if (res != null)
					{
						System.out.println("Thread " + Thread.currentThread().getName()
								+ " acquired resource " + res.resName);
						resLeftToAcquire--;
					}
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	static class Resource
	{
		public static List<Resource>				unallocatedRes	= new ArrayList<>();
		public static LinkedHashMap<String, Integer>	threadResRequirement;
		private String								resName;

		public Resource(String resName)
		{
			this.resName = resName;
		}

		public static synchronized Resource acquireResource() throws Exception
		{
			if (Thread.currentThread().getName().equals(threadResRequirement.entrySet().iterator().next().getKey()))
			{
				if (unallocatedRes.size() > 0)
				{
					Integer count = threadResRequirement
							.get(threadResRequirement.entrySet().iterator().next().getKey());
					count--;
					if (count > 0)
					{
						threadResRequirement.put(threadResRequirement.entrySet().iterator().next().getKey(), count);
					}
					else
					{
						threadResRequirement.remove(threadResRequirement.entrySet().iterator().next().getKey());
					}
					return unallocatedRes.remove(0);
				}
				else
				{
					throw new Exception("Not enough resources");
				}
			}
			return null;
		}

		public String getResName()
		{
			return resName;
		}

	}

	public static void main(String[] args)
	{
		LinkedHashMap<String, Integer> threadResourceRequirements = new LinkedHashMap<String, Integer>() {{
				put("T1", 3);
				put("T2", 4);
				put("T3", 5);
				put("T4", 6);
			}
		};
		List<Resource> resList = new ArrayList<>();
		List<Thread> tList = new ArrayList<>();
		int j=1;
		for(Map.Entry<String, Integer> entry:threadResourceRequirements.entrySet()) {
			tList.add(new Thread(new ThreadTask(entry.getValue()), entry.getKey()));
			for(int i=0;i<entry.getValue();i++) {
				resList.add(new Resource("r"+j++));
			}
		}
		Resource.threadResRequirement=threadResourceRequirements;
		Resource.unallocatedRes=resList;
		
		for(Thread t:tList) {
			t.start();
		}
	}

}
