
public class BenchmarkController {
	private String mq_type, workload_type;
	
	private void setTypes(String mq_type, String workload_type) {
		this.mq_type = mq_type;
		this.workload_type = workload_type;
	}
	
	public BenchmarkController(String mq_type, String workload_type) {
		this.setTypes(mq_type, workload_type);
	}

	public boolean launchBenchmark() {
		// we now have the queue type and workload type, pass that to the corresponding class
		
		WorkloadSpec workload_spec = WorkloadSpecFactory.generateSpec(this.workload_type);
		workload_spec.workloadReport();
		
		switch(this.mq_type) {
			case "RabbitMQ": return RabbitController.runLoad(workload_spec);
							 
							
			case "Kafka": return KafkaController.runLoad(workload_spec);
			 			  
			 
			case "ZeroMQ":  return ZeroController.runLoad(workload_spec);
			
			default: return false;
		}
	}
}
