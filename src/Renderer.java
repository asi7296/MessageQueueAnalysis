import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Renderer {
	
	private boolean mainWinowRendered = false;
	private int screen_h, screen_w;
	private final String[] workload_types = {"SM_I_F", "SM_I_V", "SM_C_F", "SM_C_V", "BM_I_F", "BM_I_V", "BM_C_F", "BM_C_V"};
	private final String[] mq_types = {"RabbitMQ", "Kafka", "ZeroMQ" };
	
	
	public boolean getRenderStatus() {
		return mainWinowRendered;
	}
	
	public void setRenderStatus(boolean status) {
		this.mainWinowRendered = status;
	}
	
	
	public boolean renderMainWindow() {
		Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
		this.screen_h = (int) (screen_size.getHeight() / 2);
		this.screen_w = (int) (screen_size.getWidth() / 2);
		
		JFrame frame = new JFrame("Message Queue Benchmark - CSPA 2017");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(this.screen_w, this.screen_h);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		/*GRID LAYOUT 3x3
		 * choose queue: <queue dropdown>
		 * choose workload: <workload dropdown>
		 * go	help
		 */
		frame.setLayout(new GridLayout(3, 3)); 
		
		JLabel ch_queue_lb = new JLabel("Choose message queueing service: ");
		frame.add(ch_queue_lb);	
		JComboBox<String> mq_selector = new JComboBox<String>(this.mq_types);
		frame.add(mq_selector);
		
		JLabel ch_workload_lb = new JLabel("Choose workload: ");
		frame.add(ch_workload_lb);
		JComboBox<String> workload_selector = new JComboBox<String>(this.workload_types);
		frame.add(workload_selector);
		
		JButton run_benchmark_btn = new JButton("Run Workload");
		frame.add(run_benchmark_btn);
		JButton help_btn = new JButton("Help");
		frame.add(help_btn);
		
		
		// add action listener for the run_benchmark and help button
		run_benchmark_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BenchmarkController bctrl = new BenchmarkController(mq_selector.getSelectedItem().toString(), workload_selector.getSelectedItem().toString());
				if(! bctrl.launchBenchmark() ) {
					JOptionPane.showMessageDialog(null, "Fatal : Failed to start benchmark ... ");
					System.exit(-1);
				}
			}
		});
		help_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Visit: www.github.com/asi7296/MessageQueueAnalysis");
			}
		});
		
		frame.setVisible(true);
		this.setRenderStatus(true);
		
		return this.getRenderStatus();
	}
}
