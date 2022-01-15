package bank4.jdbc.persistence;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import bank4.jdbc.client.Account;

public class HibernatePersistence {

	public int persist(Account account) {

		Session session = DBConnection.getSession();
		Transaction persistTransaction1 = session.beginTransaction();

		Serializable id = session.save(account);
		persistTransaction1.commit();

		session.close();
		return (Integer) id;

	}

}
