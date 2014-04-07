public class Transaction {
	
	Person sender;
	Person receiver; 
	double amount;
	String serialNumber;
	boolean isApproved;
	
	public Transaction(Person from, Person to, double amount) {
		this.sender = from;
		this.receiver = to;
		this.amount = amount;
		serialNumber = Person.getMD5(this.toString());
	}
	
	public String getSerial() {
		return serialNumber;
	}
	
}
