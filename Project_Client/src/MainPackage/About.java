package MainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class About extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					About frame = new About();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public About() {
		setResizable(false);
		setBackground(Color.WHITE);
		setTitle("Rreth Ne");
		setType(Type.UTILITY);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 426, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		JLabel lblNewLabel = new JLabel(
				"<html>\r\n<h3>Projekt<br>\r\nSistem i shp\u00EBrndar\u00EB p\u00EBr llogaritjen "
						+ "e statistikave t\u00EB notave n\u00EB arsimin e mes\u00EBm</h3>\r\n<h4>Sistemet "
						+ "e Shp\u00EBrndara, Grupi 2, 2019</h4>\r\n<p>Anetaret e grupit:</p>\r\n<ul>\r\n"
						+ "<li>Agon Hoxha</li>\r\n<li>Arbenita Maloku</li>\r\n<li>Artira Ferati</li>\r\n"
						+ "<li>Ardi Rexhepi</li>\r\n<li>Fjolla Haxhimehmeti</li>\r\n<li>Amire G\u00EBrguri</li>\r\n"
						+ "<li>Ardison Mulliqaj</li>\r\n<li>Arb\u00EBr Berisha</li>\r\n</ul>\r\n</html>");
		lblNewLabel.setBackground(Color.WHITE);
		contentPane.add(lblNewLabel, BorderLayout.CENTER);
	}

}
