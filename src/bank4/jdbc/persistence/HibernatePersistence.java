package bank4.jdbc.persistence;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import bank4.jdbc.client.Account;
import bank4.jdbc.container.SpringContainer;

public class HibernatePersistence {
	
	public static void initAccountList() {
		 Session session = DBConnection.getSession();
	        
	     Transaction transaction = session.beginTransaction();
	     float random;
	     
	     for(int i = 0; i < 20; i++) {
	    	 random = (float) ((float)  Math.round(Math.random() * 10000.0) / 100.0);
	    	 Account account = (Account) SpringContainer.getBean("account");
	    	 account.setBalance(random);
	    	 session.persist(account);
	     }
	      
	     transaction.commit();
	     session.close();
	}
	
	public static Account getAccountByNumber(int number) {
		Session session = DBConnection.getSession();
		
		Transaction getTransaction = session.beginTransaction();
		Query query = session.createQuery("from Account a where a.number=:number");
		query.setInteger("number" , number);
		List result = query.list();
		
		Account account = (Account) result.get(0);
		
		getTransaction.commit();
		session.close();
		
		return account;
	}
	
	public static void updateAccountByNumber(int number, float newBalance){
	    Session session = DBConnection.getSession();

	    Transaction updateTransaction = session.beginTransaction();
	    Query query = session.createQuery("update Account a set a.balance=:newBalance where a.number=:number");
	    query.setInteger("number", number);
	    query.setFloat("newBalance", newBalance);
	    query.executeUpdate();
	    updateTransaction.commit();

	    session.close();
	}

}
