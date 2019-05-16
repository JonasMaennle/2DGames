package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import object.player.Player;

public class GameServer implements Serializable {

	private static final long serialVersionUID = 5067442190466312833L;

	private final static int PORT = 55123;

	private ServerSocket serverSock;
	private Socket clientSocket;
	private int connectionCounter;
	private CopyOnWriteArrayList<Connection> connList;
	private boolean running = true;
	private GameState state;

	public static void main(String[] args) {
		GameServer server = new GameServer();
		server.run(PORT);
	}

	// setup connection
	public void run(int port) {
		System.out.println("Server gestartet...");
		try {
			serverSock = new ServerSocket(port);
			connectionCounter = -1;
			connList = new CopyOnWriteArrayList<>();
			this.state = new GameState();
			// start thread

			while (true) {
				// wartet bis sich Client verbindet
				clientSocket = serverSock.accept();
				connList.add(new Connection(clientSocket));
				connectionCounter++;
				System.out.println("Client connected:   " + clientSocket.getInetAddress() + "\t Connection:  " + connectionCounter);
				Thread t = new Thread(new ClientHandler(clientSocket));
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
		
		public ClientHandler(Socket clientSocket){
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
				try {
					// add data from received state to current
					while((o = in_stream.readObject()) != null) {
						Player p = (Player) o;

//							currentState.setPlayerToArray(count, p);
//							currentState.setSession(count);
						// send data to client
						sendMessageBack();
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Client disconnected:   "  + connectionNumber + ":   " + sock.getInetAddress());
				}
			
		}

		// send message to client
		private void sendMessageBack() {
			try {
				os_stream.writeObject(state);
				os_stream.flush();
				os_stream.reset();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}