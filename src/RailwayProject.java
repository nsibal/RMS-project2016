import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Panel;

import javax.swing.JButton;
import javax.swing.JTextField;

public class RailwayProject extends JFrame {

	private JPanel contentPane;
	private JComboBox sourceStn;
	private JLabel sourceStnCode;
	private JComboBox destinationStn;
	private JLabel destinationStnCode;
	private JLabel to;
	private JLabel status;
	private JButton checkTrains;
	private JTextField date;
	private JButton selectDate;
	
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	
	private String url = "jdbc:mysql://localhost:3306/RailwayData?useSSL=false";
	private String user = "root";
	private String password = "tns8268";
	
	private TrainSearch trainSearch;
	private Cal cal;
	
	private JButton feedback;
	private JDialog dialog;
	private JPanel panelD;
	private JLabel nameLbl;
	private JTextField name;
	private JLabel phnLbl;
	private JTextField phn;
	private JLabel emailLbl;
	private JTextField email;
	private JTextArea feed;
	private JScrollPane scroll;
	private JButton send;
	
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {*/
					RailwayProject frame = new RailwayProject();
					frame.setVisible(true);
				/*} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
	}
	
	public void disableAll() {
		sourceStn.setEnabled(false);
		sourceStnCode.setEnabled(false);
		destinationStn.setEnabled(false);
		destinationStnCode.setEnabled(false);
		to.setEnabled(false);
		checkTrains.setEnabled(false);
		date.setEnabled(false);
		selectDate.setEnabled(false);
		feedback.setEnabled(false);
	}
	
	public void checkIfSameStations() {
		if(sourceStn.getSelectedItem().toString().equals(destinationStn.getSelectedItem().toString()) ||
				sourceStn.getSelectedItem().toString().equals("") ||
				destinationStn.getSelectedItem().toString().equals("") ||
				date.getText().equals("")) {
			checkTrains.setEnabled(false);
		}
		else { 
			checkTrains.setEnabled(true);
		}	
	}
	
	public void askToExit() {
		int choice = JOptionPane.showConfirmDialog(this, "Do you want to exit?", "WARNING", JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.YES_OPTION) {
			try {
				cal.dispose();
				trainSearch.dispose();
				resultSet.close();
				statement.close();
				connection.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			} finally {
				this.dispose();
			}
		}
	}
	
	public void showFrame(Boolean bool) {
		this.setVisible(bool);
	}
	
	public void feedback() {
		dialog = new JDialog();
		
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dialog.dispose();
				showFrame(true);
			}
		});
		
		dialog.setVisible(true);
		showFrame(false);
		
		dialog.setSize(800, 800);
		dialog.setLocationRelativeTo(null);
		dialog.setResizable(false);
		dialog.setTitle("FEEDBACK");
		
		panelD = new JPanel();
		panelD.setLayout(null);
		
		nameLbl = new JLabel("Name:");
		nameLbl.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		nameLbl.setHorizontalAlignment(SwingConstants.CENTER);
		nameLbl.setBounds(30, 100, 100, 50);
		panelD.add(nameLbl);
		
		name = new JTextField();
		name.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		name.setBounds(180, 100, 590, 50);
		panelD.add(name);
		
		phnLbl = new JLabel("Phone:");
		phnLbl.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		phnLbl.setHorizontalAlignment(SwingConstants.CENTER);
		phnLbl.setBounds(30, 200, 100, 50);
		panelD.add(phnLbl);
		
		phn = new JTextField();
		phn.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		phn.setBounds(180, 200, 590, 50);
		panelD.add(phn);
		
		emailLbl = new JLabel("Email:");
		emailLbl.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		emailLbl.setHorizontalAlignment(SwingConstants.CENTER);
		emailLbl.setBounds(30, 300, 100, 50);
		panelD.add(emailLbl);
		
		email = new JTextField();
		email.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		email.setBounds(180, 300, 590, 50);
		panelD.add(email);
		
		feed = new JTextArea();
		feed.setLineWrap(true);
		feed.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		
		scroll = new JScrollPane(feed);
		scroll.setBounds(30, 400, 740, 200);
		
		panelD.add(scroll);
		
		send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(null, "Feedback sent.");
				dialog.dispose();
				showFrame(true);
			}
		});
		send.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		send.setBounds(340, 630, 120, 50);
		panelD.add(send);
		
		dialog.add(panelD);
	}

	/**
	 * Create the frame.
	 */
	public RailwayProject() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				askToExit();
			}
		});
		setSize(800, 800);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("RAILWAY");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		sourceStn = new JComboBox();
		sourceStn.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		sourceStn.setToolTipText("Select source location");
		sourceStn.setEditable(false);
		sourceStn.setBounds(30, 85, 320, 50);
		sourceStn.addItem("");
		contentPane.add(sourceStn);
		
		sourceStnCode = new JLabel("");
		sourceStnCode.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		sourceStnCode.setHorizontalAlignment(SwingConstants.CENTER);
		sourceStnCode.setBounds(30, 150, 320, 50);
		contentPane.add(sourceStnCode);
		
		destinationStn = new JComboBox();
		destinationStn.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		destinationStn.setToolTipText("Select destination location");
		destinationStn.setEditable(false);
		destinationStn.setBounds(450, 85, 320, 50);
		destinationStn.addItem("");
		contentPane.add(destinationStn);
		
		destinationStnCode = new JLabel("");
		destinationStnCode.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		destinationStnCode.setHorizontalAlignment(SwingConstants.CENTER);
		destinationStnCode.setBounds(450, 150, 320, 50);
		contentPane.add(destinationStnCode);
		
		to = new JLabel("- to -");
		to.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		to.setHorizontalAlignment(SwingConstants.CENTER);
		to.setBounds(375, 85, 50, 50);
		contentPane.add(to);
		
		status = new JLabel("");
		status.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		status.setHorizontalAlignment(SwingConstants.CENTER);
		status.setBounds(30, 720, 740, 50);
		contentPane.add(status);
		
		checkTrains = new JButton("Check Trains");
		checkTrains.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trainSearch = new TrainSearch(sourceStnCode.getText(), destinationStnCode.getText(), date.getText());
				trainSearch.setVisible(true);
			}
		});
		checkTrains.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		checkTrains.setBounds(340, 425, 120, 50);
		contentPane.add(checkTrains);
		
		feedback = new JButton("Feedback");
		feedback.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				feedback();
			}
		});
		feedback.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		feedback.setBounds(340, 525, 120, 50);
		contentPane.add(feedback);
		
		date = new JTextField();
		date.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				checkIfSameStations();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				checkIfSameStations();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				checkIfSameStations();
			}
		});
		date.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		date.setHorizontalAlignment(SwingConstants.CENTER);
		date.setEditable(false);
		date.setBounds(300, 250, 150, 50);
		contentPane.add(date);
		
		selectDate = new JButton("...");
		selectDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cal = new Cal(date);
				cal.setVisible(true);
			}
		});
		selectDate.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		selectDate.setBounds(450, 250, 50, 50);
		contentPane.add(selectDate);
		
		try {
			connection = DriverManager.getConnection(url, user, password);
			statement = connection.createStatement();
			
			String query = "SELECT StationName FROM StationData;";
			
			resultSet = statement.executeQuery(query);
			
			while (resultSet.next()) {
				
				sourceStn.addItem(resultSet.getString("StationName"));
				destinationStn.addItem(resultSet.getString("StationName"));
				
			}
			
			sourceStn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent event) {
					
					Object selected = sourceStn.getSelectedItem();
					
					String query = "SELECT StationCode FROM StationData WHERE StationName=\'"+selected.toString()+"\';";
					
					try {
						resultSet = statement.executeQuery(query);
						if(resultSet.next()) {
							sourceStnCode.setText(resultSet.getString("StationCode"));
						}
						else {
							sourceStnCode.setText(null);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					checkIfSameStations();
				}
			});
			
			destinationStn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent event) {
					
					Object selected = destinationStn.getSelectedItem();
					
					String query = "SELECT StationCode FROM StationData WHERE StationName=\'"+selected.toString()+"\';";
					
					try {
						resultSet = statement.executeQuery(query);
						if(resultSet.next()) {
							destinationStnCode.setText(resultSet.getString("StationCode"));
						}
						else {
							destinationStnCode.setText(null);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					checkIfSameStations();
				}
			});
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			status.setText("DATABASE CONNECTION ERROR");
			disableAll();
		}
		
		if(sourceStn.getSelectedItem().toString().equals(destinationStn.getSelectedItem().toString())) {
			checkTrains.setEnabled(false);
		}
		else {
			checkTrains.setEnabled(true);
		}
		
	}
}
