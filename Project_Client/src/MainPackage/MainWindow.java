package MainPackage;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import Chat.TClient;
import Chat.VClient;
import net.proteanit.sql.DbUtils;

public class MainWindow {
	private JFrame frmStatistika;
	private JTextField txtProfID;
	private JTable tblNotat;
	private JTextField txtEmri;
	private JTextField txtMbiemri;
	private JTable tblStats;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmStatistika.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainWindow() {
		initialize();
	}

	private void initialize() {
		frmStatistika = new JFrame();
		frmStatistika.getContentPane().setBackground(Color.WHITE);
		frmStatistika.setBackground(Color.WHITE);
		frmStatistika.setTitle("Statistika");
		frmStatistika.setResizable(false);
		frmStatistika.setBounds(100, 100, 428, 350);
		frmStatistika.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStatistika.getContentPane().setLayout(null);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setBounds(0, 0, 422, 300);
		frmStatistika.getContentPane().add(tabbedPane);
		Panel pnlHyrje = new Panel();
		pnlHyrje.setBackground(Color.WHITE);
		tabbedPane.addTab("Profesori & Lendet", null, pnlHyrje, null);
		tabbedPane.setBackgroundAt(0, Color.WHITE);
		pnlHyrje.setLayout(null);
		txtProfID = new JTextField();
		txtProfID.setBounds(162, 8, 116, 23);
		pnlHyrje.add(txtProfID);
		txtProfID.setFont(new Font("Calibri", Font.PLAIN, 14));
		txtProfID.setColumns(10);
		JLabel lblProfesori = new JLabel("Profesor ID");
		lblProfesori.setBounds(10, 11, 76, 17);
		pnlHyrje.add(lblProfesori);
		lblProfesori.setFont(new Font("Calibri", Font.PLAIN, 14));
		JButton btnStats = new JButton("Statistikat");
		btnStats.setBackground(Color.WHITE);
		btnStats.setFont(new Font("Calibri", Font.PLAIN, 14));
		btnStats.setBounds(162, 114, 116, 23);
		pnlHyrje.add(btnStats);
		JLabel lblLenda = new JLabel("Lenda");
		lblLenda.setBounds(10, 38, 76, 23);
		pnlHyrje.add(lblLenda);
		lblLenda.setFont(new Font("Calibri", Font.PLAIN, 14));
		JLabel lblViti = new JLabel("Viti");
		lblViti.setBounds(10, 72, 76, 23);
		pnlHyrje.add(lblViti);
		lblViti.setFont(new Font("Calibri", Font.PLAIN, 14));
		JCheckBox check9 = new JCheckBox("2019");
		check9.setBackground(Color.WHITE);
		check9.setSelected(true);
		check9.setFont(new Font("Calibri", Font.PLAIN, 14));
		check9.setBounds(162, 72, 67, 23);
		pnlHyrje.add(check9);
		JCheckBox check8 = new JCheckBox("2018");
		check8.setBackground(Color.WHITE);
		check8.setFont(new Font("Calibri", Font.PLAIN, 14));
		check8.setBounds(231, 72, 67, 23);
		pnlHyrje.add(check8);
		JComboBox<String> boxLenda = new JComboBox<String>();
		boxLenda.setBackground(Color.WHITE);
		boxLenda.setBounds(162, 38, 116, 23);
		pnlHyrje.add(boxLenda);
		JButton btnLigjeron = new JButton("Ligjeron");
		btnLigjeron.setBackground(Color.WHITE);
		btnLigjeron.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String professorID = txtProfID.getText();
				String komanda = "SELECT name FROM teaches WHERE professorID=\"" + professorID + "\";";
				boxLenda.removeAllItems();
				try {
					ResultSet rs = dbEkzekutim(komanda);
					boxLenda.addItem("Zgjedhe");
					while (rs.next())
						boxLenda.addItem(rs.getString(1));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnLigjeron.setFont(new Font("Calibri", Font.PLAIN, 14));
		btnLigjeron.setBounds(288, 8, 102, 23);
		pnlHyrje.add(btnLigjeron);
		JLabel lblID = new JLabel("?");
		lblID.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				tabbedPane.setSelectedIndex(3);
			}
		});
		lblID.setFont(new Font("Calibri", Font.BOLD, 14));
		lblID.setBounds(83, 12, 20, 14);
		pnlHyrje.add(lblID);
		Panel pnlNotat = new Panel();
		pnlNotat.setBackground(Color.WHITE);
		tabbedPane.addTab("Notat", null, pnlNotat, null);
		pnlNotat.setLayout(null);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 400, 251);
		pnlNotat.add(scrollPane);
		tblNotat = new JTable();
		scrollPane.setViewportView(tblNotat);
		tblNotat.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tblNotat.setFillsViewportHeight(true);
		Panel pnlStats = new Panel();
		pnlStats.setBackground(Color.WHITE);
		tabbedPane.addTab("Statistikat", null, pnlStats, null);
		pnlStats.setLayout(null);
		JComboBox<String> boxStats = new JComboBox<String>();
		boxStats.setBackground(Color.WHITE);
		boxStats.setBounds(10, 11, 277, 20);
		pnlStats.add(boxStats);
		JButton btnGjej = new JButton("Gjej");
		btnGjej.setBackground(Color.WHITE);
		btnGjej.setBounds(301, 10, 89, 23);
		pnlStats.add(btnGjej);
		tblStats = new JTable();
		tblStats.setBounds(10, 10, 378, 81);
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane_1.setBounds(10, 42, 380, 209);
		pnlStats.add(scrollPane_1);
		Panel pnlID = new Panel();
		pnlID.setBackground(Color.WHITE);
		tabbedPane.addTab("Gjej ID", null, pnlID, null);
		pnlID.setLayout(null);
		JLabel lblEmri = new JLabel("Emri:");
		lblEmri.setFont(new Font("Calibri", Font.PLAIN, 16));
		lblEmri.setBounds(10, 31, 66, 14);
		pnlID.add(lblEmri);
		txtEmri = new JTextField();
		txtEmri.setFont(new Font("Calibri", Font.PLAIN, 16));
		txtEmri.setColumns(10);
		txtEmri.setBounds(86, 28, 106, 20);
		pnlID.add(txtEmri);
		txtMbiemri = new JTextField();
		txtMbiemri.setFont(new Font("Calibri", Font.PLAIN, 16));
		txtMbiemri.setColumns(10);
		txtMbiemri.setBounds(86, 71, 106, 20);
		pnlID.add(txtMbiemri);
		JLabel lblMbiemri = new JLabel("Mbiemri:");
		lblMbiemri.setFont(new Font("Calibri", Font.PLAIN, 16));
		lblMbiemri.setBounds(10, 74, 66, 14);
		pnlID.add(lblMbiemri);
		JButton btnID = new JButton("Gjeje ID");
		btnID.setBackground(Color.WHITE);
		btnID.setFont(new Font("Calibri", Font.PLAIN, 14));
		btnID.setBounds(86, 129, 106, 23);
		pnlID.add(btnID);
		JLabel lblIntro = new JLabel(
				"<html><h4>ID nga emri dhe mbiemri</h4>Per te gjetur ID, mjafton te shkruhet emri i tij/saj.<br>"
						+ "Do gjendet nga databaza dhe njekohesisht do gjendet "
						+ "lista e lendeve qe ky person i ligjeron." + "</html>");
		lblIntro.setBounds(212, 11, 188, 122);
		pnlID.add(lblIntro);
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);
		frmStatistika.setJMenuBar(menuBar);
		JMenu mnChat = new JMenu("Chat");
		mnChat.setBackground(Color.WHITE);
		menuBar.add(mnChat);
		JMenuItem mntmTextChat = new JMenuItem("Text Chat");
		mntmTextChat.setBackground(Color.WHITE);
		mntmTextChat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				TClient client = new TClient();
			}
		});
		mnChat.add(mntmTextChat);
		JMenuItem mntmVoiceChat = new JMenuItem("Voice Chat");
		mntmVoiceChat.setBackground(Color.WHITE);
		mntmVoiceChat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VClient voice = new VClient();
				voice.setVisible(true);
			}
		});
		mnChat.add(mntmVoiceChat);
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setBackground(Color.WHITE);
		menuBar.add(mnHelp);
		scrollPane_1.setViewportView(tblStats);
		tblStats.setFillsViewportHeight(true);
		JMenuItem mntmIdNgaEmri = new JMenuItem("ID nga Emri");
		mntmIdNgaEmri.setBackground(Color.WHITE);
		mntmIdNgaEmri.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tabbedPane.setSelectedIndex(3);
			}
		});
		mnHelp.add(mntmIdNgaEmri);
		boxStats.addItem("Top 10 Studentet");
		boxStats.addItem("Kalueshmeria e lendeve");
		boxStats.addItem("Mesatarja e profesoreve ne vlersim");
		boxStats.addItem("Mesatarja e lendes");
		boxStats.addItem("Mesatarja e paraleles");
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				About about = new About();
				about.setVisible(true);
			}
		});

		boxStats.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				btnGjej.doClick();
			}
		});
		mntmAbout.setBackground(Color.WHITE);
		mnHelp.add(mntmAbout);
		btnID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = "SELECT professorID FROM professor WHERE fname=\"" + txtEmri.getText()
						+ "\" and lname=\"" + txtMbiemri.getText() + "\";";
				ResultSet rs = dbEkzekutim(command);
				try {
					if (!rs.next())
						JOptionPane.showMessageDialog(null, "Nuk ka profesor me kete emer.");
					else {
						txtProfID.setText(String.valueOf(rs.getInt(1)));
						tabbedPane.setSelectedIndex(0);
						btnLigjeron.doClick();
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Problem ne kontakt me databaze.");
				}
			}
		});
		btnGjej.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String aksioni = "";
				ResultSet rs = null;
				switch (boxStats.getSelectedIndex()) {
				case 0:
					aksioni = "SELECT Studenti, COUNT(Nota) Provime, AVG(Nota) Mesatarja "
							+ "FROM (SELECT studentID Studenti, grade Nota, subjectID Lenda "
							+ "FROM grades WHERE grade>1 ) T GROUP BY Studenti ORDER BY Provime DESC, Mesatarja DESC LIMIT 10;";
					break;
				case 1:
					aksioni = "SELECT X.subjectID 'Kodi i Lendes',  t.name 'Emri i lendes', "
							+ "COUNT(X.studentID)/Y.Zed*100 'Kalueshmeria' FROM grades X, ("
							+ "SELECT subjectID, COUNT(studentID) Zed "
							+ "FROM grades GROUP BY subjectID) Y, teaches t WHERE X.grade>1 "
							+ "AND X.subjectID=Y.subjectID AND X.subjectID=t.subjectID GROUP BY X.subjectID";
					break;
				case 2:
					aksioni = "SELECT t.professorID ProfesorID, p.fname Emri, p.lname Mbiemri, AVG(g.grade) Mesatarja "
							+ "FROM grades g, teaches t, professor p WHERE g.subjectID=t.subjectID AND t.professorID=p.professorID "
							+ "GROUP BY ProfesorID ORDER BY Emri, Mbiemri;";
					break;
				case 3:
					aksioni = "SELECT g.subjectID 'Kodi i lendes', t.professorID Profesori, t.name Lenda, AVG(g.grade) Mesatarja "
							+ "FROM grades g, teaches t WHERE g.subjectID=t.subjectID GROUP BY g.subjectID;";
					break;
				case 4:
					aksioni = "SELECT c.classID Kodi, c.desc Paralelja, c.year Viti, AVG(g.grade) Mesatarja "
							+ "FROM class c, student s, grades g WHERE c.classID=s.classID AND s.studentID=g.studentID "
							+ "GROUP BY Kodi ORDER BY Mesatarja DESC";
					break;
				default:
					aksioni = "";
					break;
				}
				rs = dbEkzekutim(aksioni);
				tblStats.setModel(DbUtils.resultSetToTableModel(rs));
			}
		});
		btnStats.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String command = "SELECT g.studentID Studenti, g.grade Nota, g.onDT Data " + "FROM teaches t, grades g "
						+ "WHERE t.subjectID=g.subjectID AND t.name=\"" + boxLenda.getSelectedItem().toString().trim()
						+ "\" AND t.professorID=\"" + txtProfID.getText() + "\"";
				if (check8.isSelected() && check9.isSelected())
					command += " and onDT >= '2017/12/31' and onDT <= '2019/12/31'";
				else if (check9.isSelected() && !check8.isSelected())
					command += " and onDT >= '2018/12/31' and onDT <= '2019/12/31'";
				else if (check8.isSelected() && !check9.isSelected())
					command += " and onDT >= '2017/12/31' and onDT <= '2018/12/31'";
				else
					command += " and onDT ='1970/01/01'";
				command += "ORDER BY onDT DESC, g.grade DESC";
				ResultSet rs = dbEkzekutim(command);
				tblNotat.setModel(DbUtils.resultSetToTableModel(rs));
				tabbedPane.setSelectedIndex(1);
			}
		});
	}

	public static ResultSet dbEkzekutim(String command) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://192.168.43.230:3306/school_stats", "root",
					"da1k9ess"); // replace with MySQL Server IP
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			return rs;
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(null, "Problem ne lidhje me databaze.");
		}
		return null;
	}
}