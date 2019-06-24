package Initializer;

import VServer.VoiceStation;

public class ReInit implements Runnable {
	@Override
	public void run() {
		try {
			VoiceStation nvs = new VoiceStation();
			Thread voice = new Thread(nvs);
			voice.start();
		} catch (Exception e) {
		}
	}
}