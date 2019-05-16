package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import framework.core.Handler;
import object.player.Player;


public class GameClient implements Runnable{
	
	private final static int PORT = 55123;
	private static String TARGET_IP = "localhost"; //  PI 192.168.2.109
	
	private Socket sock;
	private ObjectOutputStream os_stream;
	private ObjectInputStream in_stream;
	private Handler handler;
	private GameState state;
	
	private boolean running = true;
	private long t1,t2;
	
	public GameClient(){
		state = new GameState();
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
			if(t1 - t2 > 10) {
				sendData();
				t2 = t1;
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
	private void sendData(){
		try {
			if(handler != null) {
				//System.out.println(handler.getPlayer().getX());
				os_stream.writeObject(handler.getPlayer());
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
				while(running) {
					GameState tmp = (GameState)in_stream.readObject();
					//System.out.println(test.list.size());
					// remove own player
					tmp.list.remove(tmp.sessionID);
					ArrayList<Player> tmpList = state.list;

					if(tmp.list.size() != state.list.size()+1){
						for(Player p : tmp.list){
							Player tmpPlayer = new Player((int)p.getX(), (int)p.getY(), handler);
							state.list.add(tmpPlayer);
						}
					}else{
						state.list = tmpList;
					}

					
					// test
					state.list.add(new Player(200, 400, handler));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}
}
