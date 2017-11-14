import java.util.Random;

// -1 for msg_size implies random message length is to be used else 1024
public class WorkloadSpec {

	public String name;
	public final int num_msg = 4096;
	public String behavior;
	public int msg_size;
	public int interval;
	public int num_threads;
	public int batch_size;
	public float num_batches;
	public String[] payload = new String[num_msg];
	
	// iterative cotr
	public WorkloadSpec(String name, String behavior, int msg_size, int interval) {
		this.name = name;
		this.behavior = behavior;
		this.msg_size = msg_size;
		this.interval = interval;
		this.num_threads = 1;
		
		// if single message stream mode, we have only one batch of num_msg messages
		if(this.name.equals("SM_I_F") || this.name.equals("SM_I_V"))
			this.batch_size = this.num_msg;
		// if batch mode, randomise ( still iterative )
		else 
			this.batch_size = new Random().nextInt(1000) + 100;
		
		this.num_batches = this.num_msg / this.batch_size;

		this.payload = this.generatePayload();
	}
	
	// concurrent cotr
	public WorkloadSpec(String name, String behavior, int numThreads, int msg_size, int interval) {
		this.name = name;
		this.behavior = behavior;
		this.num_threads = numThreads;
		this.msg_size = msg_size;
		this.interval = interval;

		// if single message stream mode, we have only one batch of num_msg messages
		if(this.name.equals("SM_C_F") || this.name.equals("SM_C_V"))
			this.batch_size = this.num_msg;
		// if batch mode, randomise ( now concurrent )
		else 
			this.batch_size = new Random().nextInt(1000) + 100;
		
		this.num_batches = this.num_msg / this.batch_size;
	
		this.payload = this.generatePayload();
	}

	private String[] generatePayload() {
		// random size for 4096 messages
		if(this.msg_size == -1) {
			for(int i = 0; i < this.num_msg; i++) {
				this.payload[i] = this.generateMessage(msg_size);
			}
		}
		// else 1024b x 4096 messages
		else {
			String fixed_size_msg = this.generateMessage(msg_size);
			for(int i = 0; i < this.num_msg; i++) {
				this.payload[i] = fixed_size_msg;
			}
		}

		return this.payload;
	}
	

	private String generateMessage(int msg_size) {
		
		if(msg_size == -1)
			msg_size = new Random().nextInt(512); 

		StringBuilder sb = new StringBuilder(msg_size);

		int numchars = msg_size / 2;

		for(int i = 0; i < numchars; i++) {
			sb.append( (char) ( new Random().nextInt(26) + 65) );
		}
		
		return sb.toString();
	}

	public void workloadReport() {
		System.out.println("Name: " + this.name);
		System.out.println("Behavior: " + this.behavior);
		System.out.println("# Messages: " + this.num_msg);
		
		if(this.msg_size == -1 ) System.out.println("Message size: Random");
		else System.out.println("Message size: " + this.msg_size);
		
		System.out.println("Interval: Time betweeen messages: " + this.interval );
		System.out.println("Number of threads: " + this.num_threads);
		System.out.println("Batch size: " + this.batch_size);
		System.out.println("# Batches: " + this.num_batches);
		System.out.println("Payload Generated - 4096 messages");
		
		for(int i = 0; i < num_msg; i++) {
			System.out.print("Size of msg: " + this.payload[i].length() + ' ');
		}
	}
}
