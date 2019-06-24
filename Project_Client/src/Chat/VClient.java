package Chat;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class VClient extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					VClient frame = new VClient();
					frame.setVisible(true);
				} catch (Exception e) {
				}
			}
		});
	}

	public VClient() {
		setTitle("Voice Chat");
		setType(Type.UTILITY);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 346, 189);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				captureAudio();
			}
		});
		btnStart.setBounds(10, 11, 89, 23);
		contentPane.add(btnStart);
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sock.close();
				} catch (Exception e1) {
					dispose();
				}
			}
		});
		btnStop.setBounds(231, 11, 89, 23);
		contentPane.add(btnStop);
		JLabel lblvoiceChatsapoTe = new JLabel(
				"<html><h4>Chat me Z\u00EB</h4>Sapo t\u00EB presni Start, do t\u00EB lidheni me nj\u00EB "
						+ "p\u00EBrfaq\u00EBsues t\u00EB sistemit ton\u00EB. Kujdesuni q\u00EB t\u00EB p\u00EBrdoren "
						+ "nd\u00EBgjojse dhe jo mikrofon + altoparlant pasi q\u00EB mund t\u00EB ket\u00EB "
						+ "rezonanc\u00EB. P\u00EBr t\u00EB p\u00EBrfunduar, shtypni butonin Stop.</html>");
		lblvoiceChatsapoTe.setBackground(Color.WHITE);
		lblvoiceChatsapoTe.setBounds(10, 41, 310, 98);
		contentPane.add(lblvoiceChatsapoTe);
	}

	ByteArrayOutputStream byteArrayOutputStream;
	AudioFormat audioFormat;
	TargetDataLine targetDataLine;
	AudioInputStream audioInputStream;
	BufferedOutputStream out = null;
	BufferedInputStream in = null;
	Socket sock = null;
	SourceDataLine sourceDataLine;
	boolean stopCapture = false;

	private void captureAudio() {
		try {
			sock = new Socket("192.168.43.230", 500);// replace with VServerIP
			out = new BufferedOutputStream(sock.getOutputStream());
			in = new BufferedInputStream(sock.getInputStream());
			Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
			System.out.println("Available mixers:");
			for (int cnt = 0; cnt < mixerInfo.length; cnt++)
				System.out.println(mixerInfo[cnt].getName());
			audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			Mixer mixer = AudioSystem.getMixer(mixerInfo[3]);
			targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);
			targetDataLine.start();
			Thread captureThread = new CaptureThread();
			captureThread.start();
			DataLine.Info dataLineInfo1 = new DataLine.Info(SourceDataLine.class, audioFormat);
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo1);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start();
			Thread playThread = new PlayThread();
			playThread.start();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Krijimi i lidhjes deshtoj!");
		}
	}

	class CaptureThread extends Thread {
		byte tempBuffer[] = new byte[10000];

		@Override
		public void run() {
			byteArrayOutputStream = new ByteArrayOutputStream();
			stopCapture = false;
			try {
				while (!stopCapture) {
					int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
					out.write(tempBuffer);
					if (cnt > 0)
						byteArrayOutputStream.write(tempBuffer, 0, cnt);
				}
				byteArrayOutputStream.close();
			} catch (Exception e) {
				dispose();
			}
		}
	}

	private AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	class PlayThread extends Thread {
		byte tempBuffer[] = new byte[10000];

		@Override
		public void run() {
			try {
				while (in.read(tempBuffer) != -1)
					sourceDataLine.write(tempBuffer, 0, 10000);
			} catch (Exception e) {
				dispose();
			}
		}
	}
}
