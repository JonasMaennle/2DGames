package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import framework.core.Handler;


public class GameClient implements Runnable{
	
	private final static int PORT = 55123;
	private static String TARGET_IP = "game.raspberry_jonas.selfhost.eu"; //raspberry_jonas.selfhost.eu  192.168.2.104
	
	private Socket sock;
	private ObjectOutputStream os_stream;
	private ObjectInputStream in_stream;
	private Handler handler;
	private NetworkPlayer localPlayer;
	private HashMap<Integer, NetworkPlayer> playerMap;
	
	private boolean running = true;
	private long t1,t2;
	
	public GameClient(Handler handler){
		this.handler = handler;
		localPlayer = new NetworkPlayer(handler.getPlayer(), handler);
		playerMap = new HashMap<>();
	}
	
	public void run(){
		
		System.out.println("Client gestartet...");
		// setup connection
		setupConnection(TARGET_IP, PORT);
		
		Thread readerThread = new Thread(new StateReceiver());
		readerThread.start();
		
		// send current gamestate
		while(running){
			t1 = System.currentTimeMillis();
			if(t1 - t2 > 2) {
				sendData();
				t2 = t1;
			}
		}
	}
	
	private void setupConnection(String ip, int port){
		try {
			sock = new Socket(ip, port);
			os_stream = new ObjectOutputStream(sock.getOutputStream());
			os_stream.flush();
			
			System.out.println("1");
			in_stream = new ObjectInputStream(sock.getInputStream());
			System.out.println("2");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// send message to server
	private void sendData(){
		System.out.println("send");
		try {
			if(handler != null) {
				//System.out.println(handler.getPlayer().getX());
				localPlayer.setX(handler.getPlayer().getX());
				localPlayer.setY(handler.getPlayer().getY());
				
				os_stream.writeObject(localPlayer);
				os_stream.flush();
				os_stream.reset();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	// receive current gamestate from server
	public class StateReceiver implements Runnable{

		@SuppressWarnings("unchecked")
		public void run() {
			try {
				// read from server
				while(running) {
					playerMap = (HashMap<Integer, NetworkPlayer>) in_stream.readObject();
					
					for(NetworkPlayer player : playerMap.values()) {
						//System.out.println(player.getPlayerUniqueID());
						
						// test if player = localplayer
						if(player.getPlayerUniqueID() != localPlayer.getPlayerUniqueID()) {
							boolean trigger = false;
							long timestamp = System.currentTimeMillis();
							
							for(int i = 0; i < handler.getOtherPlayers().size(); i++) {
								
								if(handler.getOtherPlayers().get(i).getPlayerUniqueID() == player.getPlayerUniqueID()) {
									trigger = true;
									handler.getOtherPlayers().get(i).setX(player.getX());
									handler.getOtherPlayers().get(i).setY(player.getY());
									// all updated player receive new timestamp
									handler.getOtherPlayers().get(i).setTimeout(timestamp);
								}
							}
								
							if(!trigger) {
								//System.out.println("added");
								handler.addNetworkPlayer(player);
							}	
						}	
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}
