import java.util.concurrent.TimeUnit;

public class ZeroController {

	private static boolean connect() {
		System.out.println("Connection to msg q server here");
		return true;
	}

	public static boolean runLoad(WorkloadSpec workload_spec) {
		if( ! KafkaController.connect() ) {
			System.out.println("Error: could not connect to queue");
			return false;
		}

		System.out.println("Zero Controller init with workload: " + workload_spec.name);

		// non batch mode
		if(workload_spec.num_batches == 1) {
			System.out.println("1 batch");
			KafkaController.singleMessageStreamRunner(workload_spec);
		}

		// batch mode
		else {
			System.out.println(workload_spec.num_batches + " Batches");
			KafkaController.batchRunner(workload_spec);
		}

		return true;
	}

	private static void singleMessageStreamRunner(WorkloadSpec workload_spec) {
		switch(workload_spec.behavior) {
			
			case "iterative":
				//send messages per batch in payload iteratively, wait for 2s in between each
				for(int i = 0; i < workload_spec.num_msg; i++) {
					System.out.println("Sending: " + workload_spec.payload[i]);
					
					try {
						TimeUnit.SECONDS.sleep(workload_spec.interval);
					}
					catch(InterruptedException e) {
						System.out.println(e);
					}
				}
				break;

			
			case "concurrent":
				// create num_thread workers, each send messages in payload iteratively, wait for 2s inbetween each
				break;

			default:

		}
	}

	private static void batchRunner(WorkloadSpec workload_spec) {
		// figure this out
	}

}