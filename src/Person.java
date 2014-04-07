import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import java.security.*;

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
				Transaction t = new Transaction(me, receiver, amount);
				String message = name + " wants to send " + amount + " educoins to " + to;

				String hash = getMD5(message);				
				World.history.append(message + " encrypted as " + hash + "\n");
				World.history.append(name + " created a transaction with serial number " + t.getSerial());
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
				String validate = s + nonce;
				found = findNonce(validate, requiredZeros);
				
				while (!found) {
					nonce += 1;
					validate = s + nonce;
					System.out.println(validate + " ");
					found = findNonce(validate, requiredZeros);			
				}

				System.out.println("Found nonce: " + nonce + " for string " + s);
			}
		};
		t.start();
	}
	
	public boolean findNonce(String s, int zeros) {
		String hash = getMD5(s);
		System.out.print(hash);
		for (int i = 0; i < zeros; i++) {
			char c = hash.charAt(i);
			
			if (c != '0') {
				return false;
			}
		}
		return true;
	}
}
