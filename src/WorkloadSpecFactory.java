
public class WorkloadSpecFactory {
	private final static String[] workload_types = {"SM_I_F", "SM_I_V", "SM_C_F", "SM_C_V", "BM_I_F", "BM_I_V", "BM_C_F", "BM_C_V"};
	
	public static WorkloadSpec generateSpec(String mq_type) {
		WorkloadSpec obj = null;
		// Single Message - Iterative COTR - name, behavior, msg_size, interval)
		// Single Message - Concurrent COTR - name, behavior, num_threads, msg_size, interval)
		
		// MAKE A DECISION - For batch message mode, do we want to provide a fixed batch size or random batch sizes?
		// Batch Message - Iterative COTR - name, behavior, batch_size, msg_size, interval
		// Batch Message - Concurrent COTR - name, behavior, num_threads, batch_size, msg_size, interval
		// DECIDED ON - Random Batch sizes,
		// from batch message COTRs, remove batch_size
		
		switch(mq_type) {
			case "SM_I_F": {
				// single message stream, iterative, 1024b messages, sent one by one every 2s
				obj = new WorkloadSpec(mq_type, "iterative", 1024, 2);
				break;
			}
			case "SM_I_V": {
				// single message stream, iterative, random length messages sent every 2s
				obj = new WorkloadSpec(mq_type, "iterative", -1, 2);
				break;
			}
			case "SM_C_F": {
				// single message stream, concurrent 4 threads sending 1024b messages every 2s
				obj = new WorkloadSpec(mq_type, "concurrent", 4, 1024, 2);
				break;
			}
			case "SM_C_V": {
				// single message stream, concurrent 4 threads sending random length messages every 2s
				obj = new WorkloadSpec(mq_type, "concurrent", 4, -1, 2);
				break;
			}
			case "BM_I_F": {
				// batch message stream - random size, batches sent iteratively, fixed length messages, every 2s
				obj = new WorkloadSpec(mq_type, "batch", 1024, 2);
				break;
			}
			case "BM_I_V": {
				obj = new WorkloadSpec(mq_type, "batch", -1, 2);
				break;
			}
			case "BM_C_F": {
				obj = new WorkloadSpec(mq_type, "batch", 4, 1024, 2);
				break;
			}
			case "BM_C_V": {
				obj = new WorkloadSpec(mq_type, "batch", 4, -1, 2);
				break;
			}
		}
		return obj;
	}

}
