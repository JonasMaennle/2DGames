package framework.arena;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ScoreServer {

	private final static int PORT = 55123;

	private ServerSocket serverSock;
	private Socket clientSocket;
	private int connectionCounter;
	private boolean running = true;
	private ArrayList<ScoreEntry> scoreList;

	public static void main(String[] args) {
		ScoreServer server = new ScoreServer();
		server.run(PORT);
	}
	
	public ScoreServer() {
		this.scoreList = new ArrayList<>();
	}

	// setup connection
	public void run(int port) {
		System.out.println("Server gestartet...");
		
		readScoreFile();
		
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
	
	private void readScoreFile() {
		
		scoreList.clear();
		// read file -> write into list
		try {
			Scanner s = new Scanner(new File("/home/pi/score.txt"));
			String input;
			while(s.hasNextLine()) {
				input = s.nextLine();
				//System.out.println(input);
				String[] parts = input.split(";");
				scoreList.add(new ScoreEntry(Integer.parseInt(parts[0]), parts[1]));
			}
			s.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		}
		//System.out.println(scoreList.size());
	}
	
	private void writeScoreFile() {
		
		try {
			File file = new File("/home/pi/score.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for(ScoreEntry s : scoreList) {
				String data = (s.getWave() + ";" + s.getName() + "\n");
				writer.write(data);
			}
			
			writer.close();
		} catch (IOException e) {
			System.out.println("Filewriter error");
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
				System.out.println("Error in ScoreServer");
			try {
				// add data from received state to current
				// loop for ever
				System.out.println("wait for message");
				while ((o = in_stream.readObject()) != null) {
					readScoreFile();
					
					ScoreEntry e = (ScoreEntry) o;
					System.out.println(e.toString());
					scoreList.add(e);
					
					// send message after receiving entry
					sendMessageBack();
					
					// write new entry to file
					writeScoreFile();
				}
			} catch (Exception e) {		
				System.out.println("Client disconnected:   " + connectionNumber + ":   " + sock.getInetAddress());
			}
		}

		// send message to client
		private void sendMessageBack() {
			try {
				os_stream.writeObject(scoreList);
				os_stream.flush();
				os_stream.reset();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}