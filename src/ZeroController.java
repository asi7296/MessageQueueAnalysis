import java.util.concurrent.TimeUnit;
import java.util.Arrays;

public class ZeroController {

	private static boolean connect() {
		System.out.println("Connection to msg q server here");
		return true;
	}

	public static boolean runLoad(WorkloadSpec workload_spec) {
		if( ! ZeroController.connect() ) {
			System.out.println("Error: could not connect to queue");
			return false;
		}

		System.out.println("Zero Controller init with workload: " + workload_spec.name);

		// non batch mode
		if(workload_spec.num_batches == 1) {
			System.out.println("1 batch");
			ZeroController.singleMessageStreamRunner(workload_spec);
		}

		// batch mode
		else {
			System.out.println(workload_spec.num_batches + " Batches");
			ZeroController.batchRunner(workload_spec);
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
				singleMessageStreamConc[] threadarr = new singleMessageStreamConc[workload_spec.num_threads];	
				int pos = workload_spec.num_msg / workload_spec.num_threads;
				
				String[][] payload_split = new String[workload_spec.num_threads][];
				payload_split[0] = Arrays.copyOfRange(workload_spec.payload, 0, pos);
				payload_split[1] = Arrays.copyOfRange(workload_spec.payload, pos, 2*pos);
				payload_split[2] = Arrays.copyOfRange(workload_spec.payload, 2*pos, 3*pos);
				payload_split[3] = Arrays.copyOfRange(workload_spec.payload, 3*pos, 4*pos); 
				
				for(int i = 0; i < workload_spec.num_threads; i++) {
					threadarr[i] = new singleMessageStreamConc(payload_split[i]);
					threadarr[i].start();	
				}
				
				break;

			default:

		}
	}

	private static void batchRunner(WorkloadSpec workload_spec) {
		// figure this out
	}

	
	
	private static class singleMessageStreamConc extends Thread {
		public singleMessageStreamConc(String[] thread_payload) {
			System.out.println(thread_payload.length);
		}
		public void run() {
			System.out.println("thread");
		}
	}	

}
