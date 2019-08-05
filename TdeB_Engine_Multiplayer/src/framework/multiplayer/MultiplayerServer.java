package framework.multiplayer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import framework.entity.MessageType;

public class MultiplayerServer {

	private final static int PORT = 55124;

	private ServerSocket serverSock;
	private Socket clientSocket;
	private int connectionCounter;
	private boolean running = true;
	private HashMap<Integer, PlayerExtension> playerMap;

	public static void main(String[] args) {
		MultiplayerServer server = new MultiplayerServer();
		server.run(PORT);
	}
	
	public MultiplayerServer() {
		this.playerMap = new HashMap<>();
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
				System.out.println("Client connected:   " + clientSocket.getInetAddress() + "\t Connection:  " + connectionCounter);

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
		int playerID;

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
				System.out.println("Error in ScoreServer");
			try {
				// add data from received state to current
				// loop for ever
				while ((o = in_stream.readObject()) != null) {
					
					Message m = (Message) o;
					MessageType type = m.getType();
					
					switch (type) {
					case CONNECT:
						System.out.println("User has connected");
						PlayerExtension tmp = (PlayerExtension) m.getDeliveryObject();
						playerID = tmp.getPlayerID();
						playerMap.put(tmp.getPlayerID(), tmp);
						break;
					case POSITION:
						PlayerExtension tmp2 = (PlayerExtension) m.getDeliveryObject();	
						playerMap.replace(tmp2.getPlayerID(), tmp2);
						System.out.println(System.currentTimeMillis() - m.getTimeCreated());
						// send playerMap
						sendMessageBack();
						break;
					case DEFAULT:
						break;
					default:
						break;
					}
					// send message after receiving entry
					//sendMessageBack();

				}
			} catch (Exception e) {		
				// remove player if disconnect
				playerMap.remove(playerID);
				System.out.println("Client disconnected:   " + connectionNumber + ":   " + sock.getInetAddress());
			}
		}

		// send message to client
		private void sendMessageBack() {
			try {
				//System.out.println("server send");
				os_stream.writeObject(playerMap);
				os_stream.flush();
				os_stream.reset();
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}
}