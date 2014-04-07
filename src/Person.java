import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import java.security.*;

import javax.swing.*;

public class Person {
	String name;
	ArrayList<Transaction> blockChain;
	
	public Person(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void sendTransaction(final String to, final double amount) {
		final String name = getName();
		final Person me = this;
		final Person receiver = World.nameToPerson.get(to);
		Thread send = new Thread() {
			public void run() {
				//ha, ha world history
				final Transaction t = new Transaction(me, receiver, amount);
				final String message = name + " wants to send " + amount + " educoins to " + to;

				final String hash = getMD5(message);
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						World.history.append(message + " encrypted as " + hash + "\n");
						World.history.append(name + " created a transaction with serial number " + t.getSerial());						
					}							
					
				});
				
				validate(t.toString());
			}
		};
		send.start();
	}
	
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
	
	public void validate(final String s) {
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

				System.out.println("Found nonce: " + nonce + " for string " + s);
			}
		};
		t.start();
	}
	
	public boolean findNonce(String s, int zeros) {
		String hash = getMD5(s);
		System.out.println("Hash of " + s + " is " + hash);
		for (int i = 0; i < zeros; i++) {
			char c = hash.charAt(i);
			
			if (c != '0') {
				return false;
			}
		}
		return true;
	}
}
