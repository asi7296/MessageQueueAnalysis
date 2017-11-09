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
		return true;
	}

}