import it.sauronsoftware.cron4j.Scheduler;

public class CronScheduler {

	public static void main(String[] args) {
		// Creates a Scheduler instance.
		Scheduler s = new Scheduler();
		// Schedule a once-a-minute task.
		s.schedule("* * * * *", new Runnable() {
			public void run() {
				System.out.println("Another minute ticked away...");
			}
		});
		// Starts the scheduler.
		s.start();
		// Will run for ten minutes.
		try {
			Thread.sleep(1000L * 60L * 10L);
		} catch (InterruptedException e) {
			;
		}
		// Stops the scheduler.
		s.stop();
	}

}
