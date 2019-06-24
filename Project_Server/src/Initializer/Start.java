package Initializer;

import TServer.Server;
import VServer.VoiceStation;

public class Start {
	public static void main(String[] args) throws Exception {
		Server server = new Server();
		VoiceStation voice = new VoiceStation();
		Thread tServer = new Thread(server);
		tServer.start();
		Thread vServer = new Thread(voice);
		vServer.start();
	}
}