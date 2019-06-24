package VServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import Initializer.ReInit;

@SuppressWarnings("unused")
public class VoiceStation implements Runnable {
	ServerSocket MyService = null;
	Socket clientSocket = null;
	BufferedInputStream input = null;
	TargetDataLine targetDataLine = null;
	BufferedOutputStream out = null;
	ByteArrayOutputStream byteArrayOutputStream = null;
	AudioFormat audioFormat = null;
	SourceDataLine sourceDataLine = null;
	byte tempBuffer[] = new byte[10000];

	public VoiceStation() throws LineUnavailableException {
		try {
			audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start();
			MyService = new ServerSocket(500);
			clientSocket = MyService.accept();
			captureAudio();
			input = new BufferedInputStream(clientSocket.getInputStream());
			out = new BufferedOutputStream(clientSocket.getOutputStream());
			while (input.read(tempBuffer) != -1)
				sourceDataLine.write(tempBuffer, 0, 10000); // QKADO QE MERR NGA INPUTSTREAM, E VENDOS NE SOURCEDATALINE
		} catch (IOException e) {
			Runnable init = new ReInit();
			Thread ri = new Thread(init);
			ri.start(); // DESHTIMI SHKAKTON RESTART TE SERVERIT AD INFINITUM
		}
	}

	private AudioFormat getAudioFormat() { // PERCAKTIMI I AUDIO FORMATIT
		float sampleRate = 8000.0F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian); // COPY-PASTE
	}

	public static void main(String s[]) throws LineUnavailableException {
		@SuppressWarnings("unused")
		VoiceStation s2 = new VoiceStation();
	}

	private void captureAudio() {
		try {
			Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
			System.out.println("Available mixers:"); // VEQ PER DEV _ ME TREGU PAJISJET QE MUNDEN ME U PERDOR PER VOICE
														// COMM.
			for (int cnt = 0; cnt < mixerInfo.length; cnt++) // EDHE EMRIN QE E PERODRIN
				System.out.println(mixerInfo[cnt].getName()); // PER ME U IDENTIFIKU TE SISTEMI
			audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			Mixer mixer = AudioSystem.getMixer(mixerInfo[3]); // ZGJEDH MIXER 3 - TEK ADMIN PC, ESHTE MIKROFONI
			targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
			targetDataLine.open(audioFormat); // PERCAKTON SE QFAR FORMATI DO PERDORET PER LINJEN
			targetDataLine.start(); // HAP LINJEN PER KOMUNIKIM
			Thread captureThread = new CaptureThread();
			captureThread.start(); // FILLON CAPTURING SI THREAD NE VETVETE
		} catch (Exception e) {
			Runnable init = new ReInit();
			Thread ri = new Thread(init);
			ri.start(); // DESHTIMI SHKAKTON RESTART TE SERVERIT AD INFINITUM
		}
	}

	class CaptureThread extends Thread {
		byte tempBuffer[] = new byte[10000];

		@Override
		public void run() {
			try {
				while (true) {
					@SuppressWarnings("unused")
					int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
					out.write(tempBuffer); // VENDOS QKADO QE MERR NE BUFFER, NE OUTPUTSTREAM
				}
			} catch (Exception e) {
				Runnable init = new ReInit();
				Thread ri = new Thread(init);
				ri.start(); // DESHTIMI SHKAKTON RESTART TE SERVERIT AD INFINITUM
			}
		}
	}

	@Override
	public void run() {
		try {
			main(null); // NEVOJSHEM QE TE STARTOHET THREAD IDENTIK
		} catch (LineUnavailableException e) {
		}
	}
}
