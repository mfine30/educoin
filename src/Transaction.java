import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextPane;

public class Transaction {
	
	Person sender;
	Person receiver; 
	double amount;
	String serialNumber;
	boolean isApproved;
	boolean rejected;
	JTextPane pane;
	Color color;
	World window;
	
	public Transaction(Person from, Person to, double amount, World window) {
		this.sender = from;
		this.receiver = to;
		this.amount = amount;
		serialNumber = Person.getMD5(this.toString());
		color = Color.YELLOW;
		this.window = window;
		this.isApproved = false;
		this.rejected = false;
		createBlock();
	}
	
	public String getSerial() {
		return serialNumber;
	}
	
	/*
	 * Create a new block and add it to the GUI for visual feedback
	 */
	public void createBlock() {
		pane = new JTextPane();
		pane.setText(serialNumber + "\n" + sender.getName() + "\n" + receiver.getName() + "\n" + amount);
		pane.setBackground(color);
		JPanel panel = window.getBottomPanel();
		panel.add(pane);
		panel.revalidate();
		panel.repaint();
	}
	
	public void reject() {
		rejected = true;
		isApproved = false;
		repaint(Color.RED);
	}
	
	public void approve() {
		isApproved = true;
		repaint(Color.GREEN);				
	}
	
	public void repaint(Color c) {
		color = c;
		pane.setBackground(color);
		JPanel panel = window.getBottomPanel();
		panel.revalidate();
		panel.repaint();
	}
}
