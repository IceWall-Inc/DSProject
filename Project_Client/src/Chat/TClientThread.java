package Chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class TClientThread extends Thread {
	private Socket socket = null;
	private TClient client = null;
	private DataInputStream input = null;

	public TClientThread(TClient client, Socket socket) {
		this.client = client;
		this.socket = socket;
		open(); // KRIJON INPUTSTREAM PER PERDORIM
		start(); // HANDLES KOMUNIKIMIN
	}

	public void open() {
		try {
			input = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("Error getting InputStream.");
			client.stop(); // PA INPUT STREAM, LIDHJA MBYLLET
		}
	}

	public void close() {
		try {
			if (input != null)
				input.close();
		} catch (Exception e) {
			System.out.println("Error closing input stream.");
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				client.handle(input.readUTF());
			} catch (Exception e) {
				System.out.println("Listening error.");
				client.stop(); // NE RAST TE MUNGESES TE PERGJIGJJES, LIDHJA MBYLLET
			}
		}
	}
}
