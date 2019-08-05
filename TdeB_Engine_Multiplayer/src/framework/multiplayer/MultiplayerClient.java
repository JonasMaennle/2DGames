package framework.multiplayer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;

import framework.core.Handler;
import framework.entity.MessageType;
import framework.helper.Collection;

public class MultiplayerClient implements Runnable{
	private final static int PORT = 55124;
	private static String TARGET_IP = "192.168.2.104"; // outside-> game.raspberry_jonas.selfhost.eu, inside -> 192.168.2.104
	
	private Socket sock;
	private ObjectOutputStream os_stream;
	private ObjectInputStream in_stream;
	private Handler handler;
	private Message message;
	
	private boolean running = true;
	private boolean firstMessage;
	private PlayerExtension localPlayer;
	private Random rand;
	
	private long t1, t2;

	public MultiplayerClient(Handler handler){
		this.handler = handler;
		this.firstMessage = false;
		this.rand = new Random();
	}
	
	public void run(){
		
		// setup connection
		setupConnection(TARGET_IP, PORT);
		
		Thread readerThread = new Thread(new StateReceiver());
		readerThread.start();
		
		// send current entry
		//System.out.println("Send data...");
		while(running) {
			
			// initial message
			if(!firstMessage) {
				firstMessage = true;
				
				int id = rand.nextInt(100000000);
				Collection.PLAYER_ID = id;
				localPlayer = new PlayerExtension((int)handler.getPlayer().getX(), (int)handler.getPlayer().getY(), handler, id);
				
				message = new Message(MessageType.CONNECT);
				message.setDeliveryObject(localPlayer);
			}else {
				// create new message
				message = new Message(MessageType.POSITION);
				
				// set all attributes to dummy object
				localPlayer.setX(handler.getPlayer().getX());
				localPlayer.setY(handler.getPlayer().getY());
				
				localPlayer.setDirection(handler.getPlayer().getDirection());
				
				// add dummy to message
				message.setDeliveryObject(localPlayer);
			}
			
			if(!firstMessage) {
				sendData(message);	
			}else {
				t1 = System.currentTimeMillis();
				if(t1 - t2 > 1) { // test with lower values
					t2 = t1;
					//System.out.println("send");
					sendData(message);	
				}
			}
		}
	}
	
	private void setupConnection(String ip, int port){
		try {
			sock = new Socket(ip, port);		
			os_stream = new ObjectOutputStream(sock.getOutputStream());
			in_stream = new ObjectInputStream(sock.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// send message to server
	private void sendData(Message m){
		try {
			if(handler != null) {
				os_stream.writeObject(m);
				os_stream.flush();
				os_stream.reset();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	// receive current gamestate from server
	public class StateReceiver implements Runnable{

		public void run() {
			try {
				// read from server
				while(running) {
					@SuppressWarnings("unchecked")
					HashMap<Integer, PlayerExtension> tmpMap = (HashMap<Integer, PlayerExtension>) in_stream.readObject();
					
					for(Integer key : tmpMap.keySet()) {
						
						updatePlayers(key, tmpMap.get(key));
					}
				}
			} catch (Exception e) {
				System.out.println("No data from Server");
			}
		}	
	}
	
	private void updatePlayers(int key, PlayerExtension playerEx) {
		boolean found = false;
		for(PlayerExtension p : handler.getPl()) {
			if(p.getPlayerID() == key) {
				found = true;
				// update attributes
				p.setX(playerEx.getX());
				p.setY(playerEx.getY());
				p.setDirection(playerEx.getDirection());
				//System.out.println(p.getX() + "     "  + p.getY());
			}
		}
		
		if(!found)
			handler.getPl().add(playerEx);
	}
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}