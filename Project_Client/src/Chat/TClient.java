package Chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class TClient implements Runnable {
	private Socket socket = null;
	private DataOutputStream dos = null;
	private TClientThread client = null;
	private JFrame frmChat;
	final JTextArea messages;
	JTextField newField;
	final JButton btn;
	static String ip;
	JScrollPane scrollable;
	JScrollBar vertical;

	public TClient() {
		frmChat = new JFrame();
		frmChat.setType(Type.UTILITY);
		frmChat.getContentPane().setBackground(Color.WHITE);
		frmChat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmChat.setTitle("Text Chat");
		messages = new JTextArea(20, 22);
		messages.setFont(new Font("Calibri", Font.PLAIN, 13));
		messages.setText(
				"K\u00EBt\u00EB chat mund t'e p\u00EBrdorni p\u00EBr t\u00EB komunikuar me p\u00EBrdorues t\u00EB tjer\u00EB t\u00EB aplikacionit.\r\n\r\n");
		messages.setLineWrap(true);
		scrollable = new JScrollPane(messages);
		messages.setWrapStyleWord(true);
		scrollable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		try {
			socket = new Socket("192.168.43.230", 12398);// replace with TServerIP
			start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		btn = new JButton("Send");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					sendMessage();
				} catch (IOException e) {
					messages.append("\nError sending message.");
				}
			}
		});

		frmChat.getContentPane().setLayout(new BorderLayout(10, 10));
		newField = new JTextField();
		newField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btn.doClick();
			}
		});
		frmChat.getContentPane().add(newField, BorderLayout.CENTER);
		frmChat.getContentPane().add(scrollable, BorderLayout.NORTH);
		frmChat.getContentPane().add(btn, BorderLayout.SOUTH);
		frmChat.pack();
		newField.grabFocus();
		messages.setEditable(false);
		messages.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				newField.grabFocus();
			}
		});
		frmChat.setVisible(true);
	}

	public void start() throws IOException {
		dos = new DataOutputStream(socket.getOutputStream());
		if (client == null)
			client = new TClientThread(this, socket);
	}

	private void sendMessage() throws IOException {
		String message = newField.getText().trim();
		if (!message.equals("")) {
			dos.writeUTF(message);
			dos.flush();
			newField.setText("");
		}
	}

	public static void main(String[] args) throws IOException {
		new TClient();
	}

	@Override
	public void run() {
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		if (client != null) {
			client.stop();
			client = null;
		}
		try {
			if (dos != null)
				dos.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			client.close();
		}
	}

	public void handle(String msg) {
		if (msg != null) {
			messages.append(msg + "\n");
			vertical = scrollable.getVerticalScrollBar();
			vertical.setValue(vertical.getMaximum());
		}
	}
}