import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JWindow;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

public class TrainSearch extends JWindow {
	
	private JPanel panel;
	private JButton closeBtn;
	
	private ArrayList columnNames;
	private ArrayList data;
	private ArrayList rows;
	private ArrayList subArray;
	
	private ResultSetMetaData resultSetMetaData;
	
	private int columns;
	
	private Vector columnNamesVector;
	private Vector dataVector;
	private Vector subVector;
	
	private JTable table;
	
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	
	private String url = "jdbc:mysql://localhost:3306/RailwayData?useSSL=false";
	private String user = "root";
	private String password = "tns8268";
	
	/*public static void main(String[] args) {
		
	}*/
	
	public void exit() {
		try {
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

	public TrainSearch(String sourceStnCode, String destinationStnCode, String date) {
		// TODO Auto-generated constructor stub
		
		panel = new JPanel();
		setSize(1600, 900);
		setLocationRelativeTo(null);
		panel.setLayout(new BorderLayout());
		setContentPane(panel);
		
		closeBtn = new JButton("OK");
		closeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				exit();
			}
		});
		closeBtn.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		panel.add(closeBtn, BorderLayout.SOUTH);
		
		columnNames = new ArrayList();
		data = new ArrayList();
		
		try {
			connection = DriverManager.getConnection(url, user, password);
			statement = connection.createStatement();
			
			String query = "SELECT TrainSchedule.TrainNo, TrainInfo.TrainName, TrainSchedule.TravelDate,"
					+" TrainSchedule.SrcStnCode, TrainSchedule.Departure, TrainSchedule.DestStnCode, TrainSchedule.Arrival,"
					+" TrainSchedule.Distance, concat('',TrainSchedule.TravelTime), TrainInfo.Classes FROM TrainSchedule,"
					+" TrainInfo WHERE TrainSchedule.SrcStnCode=\'"+sourceStnCode+"\' AND TrainSchedule.DestStnCode=\'"
					+destinationStnCode+"\' AND TrainSchedule.TravelDate=\'"
					+date+"\' AND TrainInfo.TrainNo=TrainSchedule.TrainNo;";
			
			resultSet = statement.executeQuery(query);
			
			resultSetMetaData = resultSet.getMetaData();
			columns = resultSetMetaData.getColumnCount();
			
			for(int i = 1 ; i <= columns ; i++) {
				columnNames.add(resultSetMetaData.getColumnName(i));
			}
			
			while(resultSet.next()) {
				rows = new ArrayList(columns);
				for(int i = 1 ; i <= columns ; i++) {
					rows.add(resultSet.getObject(i));
				}
				data.add(rows);
			}
			
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			 e1.printStackTrace();
		}
		
		columnNamesVector = new Vector();
		dataVector = new Vector();
		
		for(int i = 0 ; i < data.size() ; i++) {
			subArray = (ArrayList)data.get(i);
			subVector = new Vector();
			for(int j = 0 ; j < subArray.size() ; j++) {
				subVector.add(subArray.get(j));
			}
			dataVector.add(subVector);
		}
		
		for(int i = 0 ; i < columnNames.size() ; i++) {
			columnNamesVector.add(columnNames.get(i));
		}
		
		table = new JTable(dataVector, columnNamesVector) {
			public Class getColumnClass(int column) {
				for(int row = 0 ; row < getRowCount() ; row++) {
					Object object = getValueAt(row, column);
					if(object != null) {
						if(column == 4 || column == 6){
							return java.util.GregorianCalendar.class;
						}
						else {
							return object.getClass();
						}
					}
				}
				return Object.class;
			}
		};
		
		table.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		table.setBackground(new Color(192, 192, 192));
		table.getTableHeader().setFont(new Font("Lucida Grande", Font.BOLD, 18));
		panel.add(table.getTableHeader(), BorderLayout.NORTH);
		panel.add(table, BorderLayout.CENTER);
		
	}
}
