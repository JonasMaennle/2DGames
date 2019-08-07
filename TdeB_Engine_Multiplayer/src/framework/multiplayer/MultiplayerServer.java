package framework.multiplayer;

import static framework.helper.Collection.HEIGHT;
import static framework.helper.Collection.SET_FULL_SCREEN;
import static framework.helper.Collection.WIDTH;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import framework.core.Handler;
import framework.entity.MessageType;
import framework.path.Graph;
import object.Spawner;
import object.enemy.Enemy_Basic;

public class MultiplayerServer {

	private final static int PORT = 55124;

	private ServerSocket serverSock;
	private Socket clientSocket;
	private int connectionCounter;
	private boolean running = true;
	private HashMap<Integer, PlayerExtension> playerMap;
	private CopyOnWriteArrayList<ClientHandler> clientList;

	private CopyOnWriteArrayList<Enemy_Basic> enemyList;
	private MultiplayerWaveManager waveManager;

	private WaveHandler waveHandler;

	// receive from first player
	public Handler handler;
	public Graph graph;
	private CopyOnWriteArrayList<Spawner> spawnPoints;

	public static void main(String[] args) {
		MultiplayerServer server = new MultiplayerServer();
		server.run(PORT);
	}

	public MultiplayerServer() {
//		this.stateManager = new StateManager();
//		StateManager.gameMode = GameState.MULTIPLAYER;
//		StateManager.gameState = GameState.MULTIPLAYER;
//		this.stateManager.loadLevel();

		this.playerMap = new HashMap<>();
		this.clientList = new CopyOnWriteArrayList<>();
		this.enemyList = new CopyOnWriteArrayList<>();
		this.waveManager = new MultiplayerWaveManager(this);
		this.spawnPoints = new CopyOnWriteArrayList<>();

		this.waveHandler = new WaveHandler();
		Thread t = new Thread(waveHandler);
		t.start();
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

				ClientHandler client = new ClientHandler(clientSocket, connectionCounter);
				clientList.add(client);
				Thread t = new Thread(client);
				t.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CopyOnWriteArrayList<ClientHandler> getClientList() {
		return clientList;
	}

	public void setClientList(CopyOnWriteArrayList<ClientHandler> clientList) {
		this.clientList = clientList;
	}

	public CopyOnWriteArrayList<Enemy_Basic> getEnemyList() {
		return enemyList;
	}

	public void setEnemyList(CopyOnWriteArrayList<Enemy_Basic> enemyList) {
		this.enemyList = enemyList;
	}

	public CopyOnWriteArrayList<Spawner> getSpawnPoints() {
		return spawnPoints;
	}

	public void setSpawnPoints(CopyOnWriteArrayList<Spawner> spawnPoints) {
		this.spawnPoints = spawnPoints;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
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
						System.out.println("User joined the game!");

						if (handler == null) {
							handler = m.getHandler();
							spawnPoints = handler.spawnPoints;
						}

						if (graph == null)
							graph = m.getGraph();

						PlayerExtension tmp = (PlayerExtension) m.getDeliveryObject();
						playerID = tmp.getPlayerID();
						playerMap.put(tmp.getPlayerID(), tmp);
						break;
					case POSITION:
						PlayerExtension tmp2 = (PlayerExtension) m.getDeliveryObject();
						playerMap.replace(tmp2.getPlayerID(), tmp2);
						// System.out.println(System.currentTimeMillis() - m.getTimeCreated());
						// send playerMap
						Message message = new Message(MessageType.POSITION);
						message.setDeliveryObject(playerMap);
						sendMessageBack(message);
						break;
					case DEFAULT:
						break;
					default:
						break;
					}
					// send message after receiving entry
					// sendMessageBack();

				}
			} catch (Exception e) {
				// remove player if disconnect
				playerMap.remove(playerID);
				clientList.remove(this);
				System.out.println("Client disconnected:   " + connectionNumber + ":   " + sock.getInetAddress());
				// e.printStackTrace();
			}
		}

		// send message to client
		public void sendMessageBack(Message m) {
			try {
				// System.out.println("server send");
				os_stream.writeObject(m);
				os_stream.flush();
				os_stream.reset();
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	private class WaveHandler implements Runnable {

		private boolean updateing;

		public WaveHandler() {
			this.updateing = true;
			
		}

		@Override
		public void run() {
			beginSession();


				while(!Display.isCloseRequested() && updateing){
					update();
					render();
					Display.update();
					Display.sync(60);
				}	
				running = false;
				Display.destroy();	
				System.exit(0);
		}

		public void update() {
			waveManager.update();
		}

		public void render() {

		}
		
		private void beginSession(){	

			Display.setTitle("Server Application");
			
			//Display.setLocation((Display.getDisplayMode().getWidth()-WIDTH) / 2, 0);
			try {
				@SuppressWarnings("unused")
				DisplayMode displayMode;
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				
				for (int i = 0; i < modes.length; i++)
		         {
		             if (modes[i].getWidth() == WIDTH
		             && modes[i].getHeight() == HEIGHT
		             && modes[i].isFullscreenCapable())
		               {
		                    displayMode = modes[i];
		               }
		         }
				
				if(!SET_FULL_SCREEN)Display.setDisplayMode(new DisplayMode(300, 200));
				
				Display.setFullscreen(SET_FULL_SCREEN);
				Display.create(new PixelFormat(0, 16, 1));
				
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
			
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
			glMatrixMode(GL_MODELVIEW);
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			Display.setVSyncEnabled(true); // <- geilo
		}
	}
}