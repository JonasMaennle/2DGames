package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class GameServer implements Serializable {

	private static final long serialVersionUID = 5067442190466312833L;

	private final static int PORT = 55123;

	private ServerSocket serverSock;
	private Socket clientSocket;
	private int connectionCounter;
	private boolean running = true;
	private static HashMap<Integer, NetworkPlayer> playerMap = new HashMap<>();
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

	// Handles ALL connections
	private class ClientHandler implements Runnable {

		int connectionNumber;
		Socket sock;
		ObjectInputStream in_stream;
		ObjectOutputStream os_stream;

		public ClientHandler(Socket clientSocket, int counter) {
			connectionNumber = counter;
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
					NetworkPlayer p = (NetworkPlayer) o;
					// add new player to hashmap
					if(!playerMap.containsKey(connectionNumber)) {
						playerMap.put(connectionNumber, p);
					}else { // player already exits
						playerMap.replace(connectionNumber, p);
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
				os_stream.writeObject(playerMap);
				os_stream.flush();
				os_stream.reset();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}