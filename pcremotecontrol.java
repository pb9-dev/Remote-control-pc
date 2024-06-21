
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;                                                                      
import java.net.ServerSocket;
import java.net.Socket;
 
public class pcremotecontrol {
	
	private static ServerSocket server = null;
	private static Socket client = null;
	private static BufferedReader in = null;
	private static String line;
	private static boolean isConnected=true;
	private static Robot robot;
	private static final int SERVER_PORT = 8998;
 
	public static void main(String[] args) {
		boolean leftpressed=false;
		boolean rightpressed=false;
 
	    try{
	    		robot = new Robot();
			server = new ServerSocket(SERVER_PORT); //server socket on port 8998
			client = server.accept(); 
			in = new BufferedReader(new InputStreamReader(client.getInputStream())); 
		}catch (IOException e) {
			System.out.println("Error in opening socket");
			System.exit(-1);
		}catch (AWTException e) {
			System.out.println("Error during creation of robot instance");
			System.exit(-1);
		}
			
		//read input from client until it is connected
	    while(isConnected){
	        try{
			line = in.readLine(); 
			System.out.println(line); 
			
			//if someone clicks on next
			if(line.equalsIgnoreCase("next")){
				//Simulate press and release of key 'n'
				robot.keyPress(KeyEvent.VK_N);
				robot.keyRelease(KeyEvent.VK_N);
			}
			//if someone clicks on previous
			else if(line.equalsIgnoreCase("previous")){
				//Simulate press and release of key 'p'
				System.out.println("In p");
				robot.keyPress(KeyEvent.VK_P);
				robot.keyRelease(KeyEvent.VK_P);		        	
			}
			//if someone clicks on play/pause
			else if(line.equalsIgnoreCase("play")){
				System.out.println("In pl");
				//Simulate press and release of spacebar
				robot.keyPress(KeyEvent.VK_SPACE);
				robot.keyRelease(KeyEvent.VK_SPACE);
			}
			
			else if(line.contains(",")){
				float movex=Float.parseFloat(line.split(",")[0]);
				float movey=Float.parseFloat(line.split(",")[1]);
				Point point = MouseInfo.getPointerInfo().getLocation(); 
				float nowx=point.x;
				float nowy=point.y;
				robot.mouseMove((int)(nowx+movex),(int)(nowy+movey));
			}

			else if(line.contains("left_click")){
				
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			}

			else if(line.equalsIgnoreCase("exit")){
				isConnected=false;

				server.close();
				client.close();
			}
	        } catch (IOException e) {
				System.out.println("Read failed");
				System.exit(-1);
	        }
      	}
	}
}