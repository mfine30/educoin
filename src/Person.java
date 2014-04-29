import java.util.ArrayList;
import java.util.Random;
import java.io.UnsupportedEncodingException;
import java.security.*;

import javax.swing.*;

public class Person {
	String name;
	ArrayList<Transaction> blockChain;
	
	public Person(String name) {
		this.name = name;
		blockChain = new ArrayList<Transaction>();
	}
	
	public String getName() {
		return name;
	}
	
	/*
	 * If a person comes into the world, they will get their block chain
	 * from a peer who already existed in the world
	 */
	public void copyBlockChain(Person peer) {
		blockChain = new ArrayList<Transaction>();
		for (int i = 0; i < peer.blockChain.size(); i++) {
			blockChain.add(peer.blockChain.get(i));
		}
	}
	
	/*
	 * Create and send a transaction from yourself to user "to"
	 */
	public void sendTransaction(final String to, final double amount, final World window) {
				
		final String name = getName();
		final Person me = this;
		final Person receiver = World.nameToPerson.get(to);
		
		Thread send = new Thread() {
			public void run() {
				//Make a new transaction and add it to the sender and receiver's blockchain
				final Transaction t = new Transaction(me, receiver, amount, window);
				blockChain.add(t);
				
				//propogate transaction to the world
				final ArrayList<Person> people = window.getPeople();
				propogateTransaction(t, people);
				boolean valid = findValidator(t, people, window);
				
				//We have enough coins to commit transaction
				if (valid) {
				
					//final String message = name + " wants to send " + amount + " educoins to " + to;
					//final String hash = getMD5(message);
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							//World.history.append(message + " encrypted as " + hash + "\n");
							World.history.append(name + " created a transaction with serial number " + t.getSerial() + "\n");
						}							
					});
					
				//Can't spend what you don't have
				} else {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							World.history.append(me.getName() + " does not have enough coins for transaction " + t.getSerial() +"\n");
						}
					});
					//Remove from transaction from everyone's block chain
					for (Person p : people) {
						blockChain.remove(t);
					}
					t.reject();
				}
			}
		};
		send.start();
	}
	
	/*
	 * Randomly choose a peer who is not the sender or receive to validate the transaction and receive the mining reward
	 */
	public boolean findValidator(Transaction t, ArrayList<Person> people, final World window) {
		Random r = new Random();
		Person p = null;
		boolean found = false;
		
		while (!found) {		
			int i = r.nextInt(people.size());
			p = people.get(i);
			if (!p.equals(t.sender) && !p.equals(t.receiver)) {
				found = true;
			}			
		}
		
		double amount = 0;
		Person curSender = t.sender;
		
		//Check the ledger that sender has enough credits
		for (Transaction tran : blockChain) {
			Person sender = tran.sender;
			Person receiver = tran.receiver;
			
			if (sender.equals(curSender)) {
				amount -= tran.amount;
			} 
			if (receiver.equals(curSender)) {
				amount += tran.amount;
			}
			System.out.println(curSender.getName() + ": " + amount);
		}		
		
//		for (Transaction tran : blockChain) {
//			System.out.println("Transaction: ");
//			System.out.println("Sender: " + tran.sender.getName());
//			System.out.println("Receiver: " + tran.receiver.getName());
//			System.out.println("Amount: " + tran.amount);
//		}
		
		if (amount >= 0) {
			validate(t.getSerial(), p);
			
			//A transaction is approved after 3 transactions have been added after it
			if (blockChain.size() >= 3) {
				boolean findNext = false;
				int i = 0;
				while (!findNext && i < blockChain.size() && blockChain.size() > 5) {
					Transaction temp = blockChain.get(i);
					if (!temp.rejected && !temp.isApproved && !temp.fromWorld) {
						temp.approve();
						findNext = true;
					}
					i++;
				}
			}
			//reward the miner
			Transaction reward = new Transaction(new Person("World"), p, World.reward, window);
			reward.fromWorld();
			propogateTransaction(reward, people);
			
			System.out.println("True");
			return true;			
		}
		System.out.println("False");
		return false;		
	}
	
	/*
	 * Sender will tell everyone in the world about the new transaction
	 * TODO: Each person should have an address book and update their address book of the changes
	 */
	public void propogateTransaction(Transaction t, ArrayList<Person> people) {
		for (Person p : people) {
			if (!p.equals(this)) {
				p.blockChain.add(t);
			}
		}
	}
	
	
	/*
	 * This is the validation method. Essentially forces you to do work to approve a block
	 * TODO: a block should only be approved once a quorum of users is met
	 */
	public void validate(final String s, final Person p) {
		final int requiredZeros = World.zeros;				
		
		Thread t = new Thread() {
			public void run() {
				int nonce = 0;
				boolean found = false;
				String validString = s + nonce;
				found = findNonce(validString, requiredZeros);
				
				while (!found) {
					nonce += 1;
					validString = s + nonce;				
					found = findNonce(validString, requiredZeros);		
					
				}
				final int n = nonce;
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						World.history.append(p.getName() + " validated " + s + " with nonce " + n + "\n");						
					}					
				});
				//System.out.println("Found nonce: " + nonce + " for string " + s);
			}
		};
		t.start();
	}
	
	/*
	 * Check if our nonce is in fact valid with the appropriate number of 
	 * leading 0's
	 */
	public static boolean findNonce(String s, int zeros) {
		String hash = getMD5(s);
		//System.out.println("Hash of " + s + " is " + hash);
		for (int i = 0; i < zeros; i++) {
			char c = hash.charAt(i);
			
			if (c != '0') {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Convert a string into an MD5 hashed string
	 */
	public static String getMD5(String s) {
		byte[] encryption = null;
		try {
			byte[] messageBytes = s.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			encryption = md.digest(messageBytes);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
				
		String hash = "";
		for (byte b : encryption) {
			hash += String.format("%02x", b&0xff);
		}
		return hash;
	}
}
