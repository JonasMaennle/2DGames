package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
	
	ObjectInputStream in_stream;
	ObjectOutputStream os_stream;
	Socket sock;
	
	public Connection(Socket sock) {
		this.sock = sock;
		try {
			in_stream = new ObjectInputStream(sock.getInputStream());
			os_stream = new ObjectOutputStream(sock.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Socket getSock() {
		return sock;
	}
	public void setSock(Socket sock) {
		this.sock = sock;
	}
	public ObjectInputStream getIn_stream() {
		return in_stream;
	}

	public void setIn_stream(ObjectInputStream in_stream) {
		this.in_stream = in_stream;
	}

	public ObjectOutputStream getOs_stream() {
		return os_stream;
	}

	public void setOs_stream(ObjectOutputStream os_stream) {
		this.os_stream = os_stream;
	}
}
