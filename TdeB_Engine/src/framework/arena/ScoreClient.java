package framework.arena;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import framework.core.Handler;

public class ScoreClient implements Runnable{
	private final static int PORT = 55123;
	private static String TARGET_IP = "192.168.2.104"; //raspberry_jonas.selfhost.eu, 192.168.2.104
	
	private Socket sock;
	private ObjectOutputStream os_stream;
	private ObjectInputStream in_stream;
	private Handler handler;
	private ScoreEntry myEntry;
	private ArrayList<ScoreEntry> scoreList;
	
	private boolean running = true;

	public ScoreClient(Handler handler){
		this.handler = handler;
		this.myEntry = new ScoreEntry(0, "name");
		this.scoreList = new ArrayList<>();
	}
	
	public void run(){
		
		// setup connection
		setupConnection(TARGET_IP, PORT);
		
		Thread readerThread = new Thread(new StateReceiver());
		readerThread.start();
		
		// send current entry
		//System.out.println("Send data...");

		sendData();	
		
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
				os_stream.writeObject(myEntry);
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
					scoreList = (ArrayList<ScoreEntry>) in_stream.readObject();
					running = false;
				}
			} catch (Exception e) {
				System.out.println("No data from Server");
			}
		}	
	}
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public ScoreEntry getMyEntry() {
		return myEntry;
	}

	public void setMyEntry(ScoreEntry myEntry) {
		this.myEntry = myEntry;
	}

	public ArrayList<ScoreEntry> getScoreList() {
		return scoreList;
	}

	public void setScoreList(ArrayList<ScoreEntry> scoreList) {
		this.scoreList = scoreList;
	}
}
