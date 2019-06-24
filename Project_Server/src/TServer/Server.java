package TServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable, Subject {
	private ServerThread observers[] = new ServerThread[10];
	private ServerSocket listener = null;
	private Thread thread = null;
	private int clientCount = 0;
	int LISTENING_PORT = 12398;

	public Server() {
		try {
			listener = new ServerSocket(LISTENING_PORT);
			start();
		} catch (IOException e) {
			System.out.println("Can not bind to port " + LISTENING_PORT + ".\nPossibly server already running.");
		}
	}

	private void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	private int findClient(int ID) {
		for (int i = 0; i < clientCount; i++)
			if (observers[i].getID() == ID)
				return i;
		return -1;
	}

	@Override
	public void run() {
		while (thread != null) {
			try {
				addThread(listener.accept());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public synchronized void remove(int ID) throws Exception {
		int pos = findClient(ID);
		if (pos >= 0) {
			ServerThread threadToTerminate = observers[pos];
			if (pos < clientCount - 1) {
				for (int i = pos + 1; i < clientCount; i++) {
					observers[i - 1] = observers[i];
				}
			}
			clientCount--;
			try {
				threadToTerminate.close();
			} catch (IOException e) {
				System.out.println("Error closing thread.");
				threadToTerminate.stop();
			}
		}
	}

	private void addThread(Socket socket) {
		if (clientCount < observers.length) {
			observers[clientCount] = new ServerThread(this, socket);
			try {
				observers[clientCount].open();
				observers[clientCount].start();
				clientCount++;
			} catch (IOException e) {
				System.out.println("Error opening thread.");
			}
		}
	}

	public synchronized void handle(int ID, String input) {
		for (int i = 0; i < clientCount; i++)
			observers[i].send(ID + ": " + input);
	}

	public static void main(String[] args) throws IOException {
		new Server();
	}

	@Override
	public void registerObserver(Observer o) {
	}

	@Override
	public void removeObserver(Observer o) {
	}

	@Override
	public void notifyAllObservers(String message) {
	}
}