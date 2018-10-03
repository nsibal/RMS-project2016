import java.util.GregorianCalendar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class Cal extends JWindow{
	
	private JPanel panel;
	private JComboBox yearList;
	private JComboBox monthList;
	private JLabel monthYearLabel;
	
	private GregorianCalendar calendar = new GregorianCalendar();
	private int realDay = calendar.get(GregorianCalendar.DAY_OF_MONTH);
	private int realMonth = calendar.get(GregorianCalendar.MONTH);
	private int realYear = calendar.get(GregorianCalendar.YEAR);
	
	private GregorianCalendar newCalendar;
	private int maxDays;
	private int firstDay;
	
	private String months[] = {"January","February","March","April","May","June",
			"July","August","September","October","November","December"};
	
	private String dayOfWeek[] = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	private JLabel weekDays[] = new JLabel[7];
	
	private JButton days[] = new JButton[42];
	
	private boolean isMonthLErealMonth;

	/*public static void main(String[] args) {
	
	}
	*/
	
	public void setMonthYearLabel() {
		monthYearLabel.setText(monthList.getSelectedItem().toString()+" "+yearList.getSelectedItem().toString());
	}
	
	public void isSelectedMonthLessThanCurrentMonth(String selectedMonth, int realMonth) {
		isMonthLErealMonth = false;
		for(int i = 0 ; i < months.length ; i++) {
			if(months[i].equals(selectedMonth)) {
				if(i < realMonth) {
					isMonthLErealMonth = true;
					break;
				}
			}
		}
	}
	
	public void createCalendar(int year, int month) {
		newCalendar = new GregorianCalendar(year, month, 1);
		firstDay = newCalendar.get(GregorianCalendar.DAY_OF_WEEK);
		maxDays = newCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		
		int date = firstDay;
		
		for(int i = 0; i < firstDay-1 ; i++) {
			days[i].setVisible(false);
		}
		
		for(int i = 1 ; i <= maxDays ; i++) {
			days[date-1].setVisible(true);
			days[date-1].setText(new Integer(i).toString());
			date++;
		}
		
		for(int i = date-1 ; i < days.length ; i++) {
			days[i].setVisible(false);
		}
		
		if(year == realYear && month == realMonth) {
			for(int i = 0 ; i < firstDay + realDay - 2 ; i++) {
				days[i].setVisible(false);
			}
		}
	}
	
	public void showCalendar() {
		int year = Integer.parseInt(yearList.getSelectedItem().toString());
		String selectedMonth = monthList.getSelectedItem().toString();
		int month = 0;
		for(int i = 0 ; i < months.length ; i++) {
			if(months[i].equals(selectedMonth)) {
				month = i;
				break;
			}
		}
		createCalendar(year, month);
	}
	
	public void setDate(JButton btn, JTextField date) {
		int month=0;
		for(int i = 0 ; i < months.length ; i++) {
			if(months[i].equals(monthList.getSelectedItem().toString())) {
				month = i+1;
				break;
			}
		}
		date.setText(yearList.getSelectedItem().toString()+"-"+month+"-"+btn.getText());
		this.dispose();
	}
	
	public Cal(JTextField date) {
		
		panel = new JPanel();
		setSize(800, 450);
		setLocationRelativeTo(null);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		int locationX = 20;
		for(int i = 0 ; i < weekDays.length ; i++) {
			weekDays[i] = new JLabel(dayOfWeek[i]);
			weekDays[i].setFont(new Font("Lucida Grande", Font.BOLD, 15));
			weekDays[i].setHorizontalAlignment(SwingConstants.CENTER);
			weekDays[i].setBounds(locationX, 20, 100, 30);
			panel.add(weekDays[i]);
			locationX = locationX + 110;
		}
		
		int locationY = 35;
		for(int i = 0 ; i < days.length ; i++) {
			days[i]= new JButton();
			if(i%7 == 0) {
				locationX = 20;
				locationY = locationY + 40;
			}
			days[i].setBounds(locationX, locationY, 100, 30);
			days[i].setFont(new Font("Lucida Grande", Font.PLAIN, 15));
			
			final Integer innerI = i;
			days[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					setDate(days[innerI], date);
				}
			});
			panel.add(days[i]);
			days[i].setVisible(false);
			locationX = locationX + 110;
		}
		
		monthYearLabel = new JLabel("");
		monthYearLabel.setFont(new Font("Lucida Grande", Font.BOLD, 18));
		monthYearLabel.setHorizontalAlignment(SwingConstants.CENTER);
		monthYearLabel.setBounds(182, 380, 424, 50);
		panel.add(monthYearLabel);
		
		monthList = new JComboBox();
		monthList.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		monthList.setBounds(20, 380, 150, 50);
		panel.add(monthList);
		
		yearList = new JComboBox();
		yearList.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		yearList.setBounds(630, 380, 150, 50);
		panel.add(yearList);
		for(int i = realYear ; i <= realYear+100 ; i++) {
			yearList.addItem(i);
		}
		
		monthList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isSelectedMonthLessThanCurrentMonth(monthList.getSelectedItem().toString(), realMonth);
				if(yearList.getSelectedItem().toString().equals(new Integer(realYear).toString()) && isMonthLErealMonth) {
					monthList.setSelectedItem(months[realMonth]);
				}
				setMonthYearLabel();
				showCalendar();
			}
		});
		for(String month : months) {
			monthList.addItem(month);
		}
		monthList.setSelectedItem(months[realMonth]);
			
		yearList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isSelectedMonthLessThanCurrentMonth(monthList.getSelectedItem().toString(), realMonth);
				if(yearList.getSelectedItem().toString().equals(new Integer(realYear).toString()) && isMonthLErealMonth) {
					monthList.setSelectedItem(months[realMonth]);
				}
				setMonthYearLabel();
				showCalendar();
			}
		});		
	}
}
