import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class MQTest0 {
	public static void main(String[] args) throws IOException, TimeoutException {
		
		Renderer renderer = new Renderer();	
		if( ! renderer.getRenderStatus() )
				if( ! renderer.renderMainWindow() ) {
					System.out.println("Something went wrong while trying to render the screen, exitting now ...");
				}
		}
}

/*ConnectionFactory connectionFactory = new ConnectionFactory();
connectionFactory.setHost("localhost");
connectionFactory.setUsername("guest");
connectionFactory.setPassword("guest");

Connection conn = connectionFactory.newConnection();
Channel channel = conn.createChannel();

String queuename = "TestQueue0";
String msg = "This is message 0";
channel.basicPublish("", queuename, null, msg.getBytes());
System.out.println("Msg inserted in queue");

channel.close();
conn.close();
// test
*/	
