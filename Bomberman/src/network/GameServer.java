package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import object.Obstacle;

public class GameServer implements Serializable {

	private static final long serialVersionUID = 5067442190466312833L;

	private final static int PORT = 55123;

	private ServerSocket serverSock;
	private Socket clientSocket;
	private int connectionCounter;
	private boolean running = true;
	private static HashMap<Integer, NetworkPlayer> playerMap = new HashMap<>();
	private static CopyOnWriteArrayList<Obstacle> obstacleList = new CopyOnWriteArrayList<Obstacle>();
	private long t1, t2;


	public static void main(String[] args) {
		GameServer server = new GameServer();
		server.run(PORT);
	}
	
	public GameServer() {

	}

	// setup connection
	public void run(int port) {
		System.out.println("Server gestartet...");
		fillMapWithObstacles();
		
		try {
			serverSock = new ServerSocket(port);
			connectionCounter = -1;
			// start thread

			while (running) {
				// wartet bis sich Client verbindet
				clientSocket = serverSock.accept();
				connectionCounter++;
				System.out.println("Client connected:   " + clientSocket.getInetAddress() + "\t Connection:  "
						+ connectionCounter);

				Thread t = new Thread(new ClientHandler(clientSocket, connectionCounter));
				t.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void fillMapWithObstacles() {
		Random rand = new Random();
		for(int i = 2; i < 21; i++) {
			for(int j = 2; j < 21; j++) {
				
				if(rand.nextInt(100) < 70) {
					obstacleList.add(new Obstacle(i * 32, j * 32, 32, 32));
				}
			}
		}
	}

	// Handles ALL connections
	private class ClientHandler implements Runnable {

		private boolean sendobstacles;
		int connectionNumber;
		Socket sock;
		ObjectInputStream in_stream;
		ObjectOutputStream os_stream;

		public ClientHandler(Socket clientSocket, int counter) {
			connectionNumber = counter;
			this.sendobstacles = false;
			try {
				sock = clientSocket;
				in_stream = new ObjectInputStream(sock.getInputStream());
				os_stream = new ObjectOutputStream(sock.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// receive message from client
		public void run() {
			Object o;
			if (in_stream == null)
				System.out.println("Error in GameServer");
			try {
				// add data from received state to current
				// loop for ever
				while ((o = in_stream.readObject()) != null) {
					
					// add new player to hashmap
					if(o instanceof NetworkPlayer) {
						NetworkPlayer p = (NetworkPlayer) o;
						
						if(!playerMap.containsKey(connectionNumber)) {
							playerMap.put(connectionNumber, p);
						}else { // player already exits
							playerMap.replace(connectionNumber, p);
						}
					}

					
					t1 = System.currentTimeMillis();

					if(t1 - t2 > 2) {
						sendMessageBack();
						t2 = t1;
					}
					
				}
			} catch (Exception e) {

				NetworkPlayer p = playerMap.get(connectionNumber);
				playerMap.remove(connectionNumber, p);
				System.out.println("player " + connectionNumber + " removed");
				
				System.out.println("Client disconnected:   " + connectionNumber + ":   " + sock.getInetAddress());
			}
		}

		// send message to client
		private void sendMessageBack() {
			try {
				if(!sendobstacles) {
					sendobstacles = true;
					os_stream.writeObject(obstacleList);
				}
				
				os_stream.writeObject(playerMap);
				os_stream.flush();
				os_stream.reset();
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}
}