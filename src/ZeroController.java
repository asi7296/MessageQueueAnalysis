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
				System.out.println("iterative single");
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
				// each thread sends 4096 messages, 2s in between each message
				// create num_thread workers, each send messages in payload iteratively, wait for 2s inbetween each
				singleMessageStreamConc[] threadarr = new singleMessageStreamConc[workload_spec.num_threads];	
				/*int pos = workload_spec.num_msg / workload_spec.num_threads;
				
				String[][] payload_split = new String[workload_spec.num_threads][];
				payload_split[0] = Arrays.copyOfRange(workload_spec.payload, 0, pos);
				payload_split[1] = Arrays.copyOfRange(workload_spec.payload, pos, 2*pos);
				payload_split[2] = Arrays.copyOfRange(workload_spec.payload, 2*pos, 3*pos);
				payload_split[3] = Arrays.copyOfRange(workload_spec.payload, 3*pos, 4*pos); */
				
				for(int i = 0; i < workload_spec.num_threads; i++) {
					//threadarr[i] = new singleMessageStreamConc(payload_split[i]);
					threadarr[i] = new singleMessageStreamConc(workload_spec.payload, workload_spec.num_msg, workload_spec.interval);
					threadarr[i].start();	
				}
				
				break;

			default:

		}
	}

	private static void batchRunner(WorkloadSpec workload_spec) {
		switch(workload_spec.behavior) {
			case "iterbatch":
				System.out.println("Batch iterative");
				// burst each batch, wait for 10s between batches
				for(int i = 0; i < workload_spec.num_batches; i++) {
					for(int j = 0; j < workload_spec.batch_size; j++) {
						System.out.println("Batch " + i + " message: " + j);
					}
					try {
						TimeUnit.SECONDS.sleep(10);
					}
					catch(InterruptedException e) {
						System.out.println(e);
					}
				}
				break;

			case "concbatch":
				batchMessageStreamConc[] threadarr = new batchMessageStreamConc[workload_spec.num_threads];	
				for(int i = 0; i < workload_spec.num_threads; i++) {
					System.out.println("Creating threads: " + workload_spec.num_threads);
					//threadarr[i] = new singleMessageStreamConc(payload_split[i]);
					threadarr[i] = new batchMessageStreamConc(workload_spec.payload, workload_spec.num_batches, workload_spec.batch_size);
					threadarr[i].start();	
				}
				break;
		}
	}

	
	
	private static class singleMessageStreamConc extends Thread {
		private String[] thread_payload;
		private int num_msg = 0;
		private int interval = 0;
		public singleMessageStreamConc(String[] thread_payload, int num_msg, int interval) {
			System.out.println(thread_payload.length);
			this.thread_payload = thread_payload;
			this.num_msg = num_msg;
			this.interval = interval;
		}
		public void run() {
			for(int i = 0; i < this.num_msg; i++) {
				System.out.println("Sending: " + this.thread_payload[i]);
					
				try {
					TimeUnit.SECONDS.sleep(this.interval);
				}
				catch(InterruptedException e) {
					System.out.println(e);
				}
			}
		}
	}	

	private static class batchMessageStreamConc extends Thread {
		private String[] thread_payload;
		private float num_batches = 0;
		private int batch_size = 0;
		public batchMessageStreamConc(String[] thread_payload, float num_batches, int batch_size) {
			System.out.println(thread_payload.length);
			this.thread_payload = thread_payload;
			this.num_batches = num_batches;
			this.batch_size = batch_size;
		}
		public void run() {
			System.out.println("thread");
			for(int i = 0; i < this.num_batches; i++) {
					for(int j = 0; j < this.batch_size; j++) {
						System.out.println("Thread: " + this.getId() + "Batch " + i + " message: " + thread_payload[j]);
					}
					try {
						TimeUnit.SECONDS.sleep(10);
					}
					catch(InterruptedException e) {
						System.out.println(e);
					}
				}
		}
	}

}
