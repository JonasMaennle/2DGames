package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import object.player.Player;

public class GameServer implements Serializable {

	private static final long serialVersionUID = 5067442190466312833L;

	private final static int PORT = 55123;

	private ServerSocket serverSock;
	private Socket clientSocket;
	private int connectionCounter;
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
			this.state = new GameState();
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
				System.out.println("miip");
			try {
				// add data from received state to current
				while ((o = in_stream.readObject()) != null) {
					Player p = (Player) o;

					// System.out.println(o);
					if (state.list.size() <= connectionNumber)
						state.list.add(connectionNumber, p);
					else if (state.list.size() > connectionNumber)
						state.list.set(connectionNumber, p);
					
					state.sessionID = connectionNumber;
					
					sendMessageBack();
				}
			} catch (Exception e) {
				//e.printStackTrace();
				System.out.println("Client disconnected:   " + connectionNumber + ":   " + sock.getInetAddress());
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