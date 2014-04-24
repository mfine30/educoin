import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSeparator;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JList;

import java.awt.FlowLayout;

import javax.swing.JTextPane;


public class World {

	private JFrame frame;
	private JTextField zerosNeeded;
	private JTextField miningReward;
	private JTextField blocksTilHalf;
	private JTextField userName;
	private JTextField amount;
	
	
	JList<String> fromList;
	JList<String> toList;
	public static HashMap<String,Person> nameToPerson;	
	DefaultListModel<String> personModel;
	public static JTextArea history;
	private static JPanel bottomPanel;
	static World window;
	
	static int zeros = 2;
	static double reward = 25;
	static double halvedEach = 50;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new World();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public World() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("EduCoin");
		frame.setResizable(false);
		frame.setBounds(100, 100, 1016, 832);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLocation(5, 5);
		leftPanel.setSize(274, 445);
		frame.getContentPane().add(leftPanel);
		leftPanel.setLayout(null);
		
		JLabel lblFrom = new JLabel("From:");
		lblFrom.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblFrom.setBounds(6, 6, 61, 16);
		leftPanel.add(lblFrom);
		
		JLabel lblTo = new JLabel("To:");
		lblTo.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblTo.setBounds(132, 6, 61, 16);
		leftPanel.add(lblTo);
		
		JLabel lblAmount = new JLabel("Amount To Send:");
		lblAmount.setBounds(6, 352, 127, 16);
		leftPanel.add(lblAmount);
		
		amount = new JTextField();
		amount.setBounds(145, 346, 110, 28);
		leftPanel.add(amount);
		amount.setColumns(10);
		
		JButton send = new JButton("Send Coins");
		send.setBounds(6, 390, 117, 29);
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String from = fromList.getSelectedValue();
				String to = toList.getSelectedValue();
				double coinsToSend = Double.valueOf(amount.getText());
				
				Person user = nameToPerson.get(from);
				user.sendTransaction(to, coinsToSend, window);
			}

		});
		leftPanel.add(send);
		
		createPeople();
		
		fromList = new JList<String>();
		fromList.setModel(personModel);
		fromList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fromList.setBounds(16, 34, 77, 194);
		leftPanel.add(fromList);
		
		toList = new JList<String>();
		toList.setModel(personModel);
		toList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		toList.setBounds(142, 34, 77, 194);
		leftPanel.add(toList);
		
		JLabel lblTransactionHistory = new JLabel("Transaction History:");
		lblTransactionHistory.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblTransactionHistory.setBounds(291, 10, 165, 16);
		frame.getContentPane().add(lblTransactionHistory);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(291, 38, 413, 358);
		frame.getContentPane().add(scrollPane);
		
		history = new JTextArea();
		history.setLineWrap(true);
		history.setEditable(false);
		scrollPane.setViewportView(history);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBounds(724, 5, 286, 445);
		frame.getContentPane().add(rightPanel);
		rightPanel.setLayout(null);
		
		JLabel lblGlobalConstants = new JLabel("Global Constants:");
		lblGlobalConstants.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblGlobalConstants.setBounds(6, 6, 129, 16);
		rightPanel.add(lblGlobalConstants);
		
		JLabel lblNumberOfs = new JLabel("Number of 0's Needed to Mine:");
		lblNumberOfs.setBounds(6, 34, 201, 16);
		rightPanel.add(lblNumberOfs);
		
		zerosNeeded = new JTextField();
		zerosNeeded.setText("2");
		zerosNeeded.setBounds(215, 28, 47, 28);
		rightPanel.add(zerosNeeded);
		zerosNeeded.setColumns(10);
		
		JLabel lblMiningReward = new JLabel("Mining Reward:");
		lblMiningReward.setBounds(6, 62, 129, 16);
		rightPanel.add(lblMiningReward);
		
		miningReward = new JTextField();
		miningReward.setText("25");
		miningReward.setBounds(215, 56, 47, 28);
		rightPanel.add(miningReward);
		miningReward.setColumns(10);
		
		JLabel lblEachBlocks = new JLabel("Mining Reward Halved");
		lblEachBlocks.setBounds(6, 90, 174, 16);
		rightPanel.add(lblEachBlocks);
		
		blocksTilHalf = new JTextField();
		blocksTilHalf.setText("50");
		blocksTilHalf.setBounds(215, 84, 47, 28);
		rightPanel.add(blocksTilHalf);
		blocksTilHalf.setColumns(10);
		
		JButton updateBtn = new JButton("Update");
		updateBtn.setBounds(6, 132, 117, 29);
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				zeros = Integer.valueOf(zerosNeeded.getText());
				reward = Double.valueOf(miningReward.getText());
				halvedEach = Double.valueOf(blocksTilHalf.getText());
			}

		});
		rightPanel.add(updateBtn);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.GRAY);
		separator.setBounds(6, 189, 274, 12);
		rightPanel.add(separator);
		
		JLabel lblAddNewUser = new JLabel("Add New User:");
		lblAddNewUser.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblAddNewUser.setBounds(6, 212, 129, 16);
		rightPanel.add(lblAddNewUser);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(6, 240, 61, 16);
		rightPanel.add(lblName);
		
		userName = new JTextField();
		userName.setBounds(62, 234, 168, 28);
		rightPanel.add(userName);
		userName.setColumns(10);
		
		JButton addPersonButton = new JButton("Create Person");
		addPersonButton.setBounds(6, 279, 117, 29);
		addPersonButton.addActionListener(new ActionListener() {

			@Override
			//We add a new person to the DefaultListModel and HashMap
			public void actionPerformed(ActionEvent e) {
				String name = userName.getText();
				Person p = new Person(name);
				nameToPerson.put(name, p);

				personModel = new DefaultListModel<String>();
				ArrayList<String> people = new ArrayList<String>();

				for (String person : nameToPerson.keySet()) {
					person = Character.toUpperCase(person.charAt(0)) + person.substring(1);
					people.add(person);
				}
				
				p.copyBlockChain(nameToPerson.get("Alice"));
				
				Collections.sort(people);
				for (String person : people)
					personModel.addElement(person);
				
				fromList.setModel(personModel);
				toList.setModel(personModel);
				
			}
			
		});
		rightPanel.add(addPersonButton);
		
		bottomPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) bottomPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		bottomPanel.setBounds(6, 462, 1004, 266);
		frame.getContentPane().add(bottomPanel);
		
		JTextPane blockKey = new JTextPane();
		blockKey.setText("SerialNumber\nSender\nReceiver\nAmount");
		blockKey.setBackground(Color.WHITE);
		bottomPanel.add(blockKey);
		
		populateGenesisBlocks();
		
	}
	
	public void populateGenesisBlocks() {
		Person genesis = new Person("World");
		
		Transaction one = new Transaction(genesis,nameToPerson.get("Alice"),100,this);
		Transaction two = new Transaction(genesis, nameToPerson.get("Bob"),100,this);
		Transaction three = new Transaction(genesis, nameToPerson.get("David"),100,this);
		
		one.fromWorld();
		two.fromWorld();
		three.fromWorld();
		
		Person alice = nameToPerson.get("Alice");
		alice.blockChain.add(one);
		alice.blockChain.add(two);
		alice.blockChain.add(three);
		
		nameToPerson.get("Bob").copyBlockChain(alice);
		nameToPerson.get("David").copyBlockChain(alice);
	}
	
	@SuppressWarnings("serial")
	private void createPeople() {
		Person alice = new Person("Alice");
		alice.updateWallet(100);
		Person bob = new Person("Bob");
		bob.updateWallet(100);
		Person david = new Person("David");
		david.updateWallet(100);
		
		nameToPerson = new HashMap<String, Person>();
		nameToPerson.put("Alice", alice);
		nameToPerson.put("Bob", bob);
		nameToPerson.put("David", david);
		
		personModel = new DefaultListModel<String>() {
			String[] values = new String[] {"Alice","Bob","David"};
			public int getSize() {
				return values.length;
			}
			public String getElementAt(int index) {
				return values[index];
			}
		};
	}
	
	public JPanel getBottomPanel() {
		return bottomPanel;
	}
	
	public ArrayList<Person> getPeople() {
		ArrayList<Person> people = new ArrayList<Person>();
		
		for (String name : nameToPerson.keySet())
			people.add(nameToPerson.get(name));
		
		return people;
	}
}
