package org.bootcamp.AWS;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Persistence;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;




public class AWSManager {
	EntityManagerFactory emf; // factory class to create new EntityManager
	EntityManager em; // entity manager to manage persistence
	EntityTransaction entityTransaction; // optional transaction

	public AWSManager() {
		// Initialize EntityManager using local H2 database
		emf = Persistence.createEntityManagerFactory("AWS-connection");
		em = emf.createEntityManager();
		// Initialize EnitityTransaction to manage transactions directly
		entityTransaction = em.getTransaction();
	}

	public User createUser(String name, String password) {
		User user = new User(name, password);
		persist(user);
		return user;
	}

	public Book createUserBook(User user, String name) {
		Book book = new Book(name);
		book.setUser(user);
		persist(book);
		user.addBook(book);
		persist(user);		
		return book;
	}

	public void persist(Object o) {
		// Saves any passed object into database using EntityManager
		System.out.println("Save to db");
		try {
			entityTransaction.begin();
			em.persist(o);
			entityTransaction.commit();
		} catch (Exception e) {
			entityTransaction.rollback();
			e.printStackTrace();
		}
	}

	public User searchInvoice(String name) {
		User user  = new User("dummy", "dummy");
		user = this.em.find(User.class, name);
		System.out.println("Found invoice " + user.toString());
		return user;
	}

	public Book searchBook(String name) {
		// 
		System.out.println("Searching for item");	
		Book book = em.find(Book.class, name);
		System.out.println("Found item " + book.toString());		
		return book;
	}
	
	
	public Book deleteBook(Book book) {
	//	book.setUser(null);
	//	em.persist();
		System.out.println("Searching for item");	
		Book book2 = this.searchBook(book.getName());
		System.out.println("Found item " + book2.toString());	
		em.remove(book2);	
		return book;
	}
	

	public void clearData() {
		// Use this method to delete any previous data from database tables
		System.out.println("clearing data");
		entityTransaction.begin();
		em.createNativeQuery("delete from User; delete from Book;").executeUpdate();
		entityTransaction.commit();
	}

//	public static void main(String[] args) {
//		// By default, run Junit test if started as Java application
//		// Feel free to change this method for your own needs
//		AWSManager test = new AWSManager();
//		User user = test.createUser("aaa", "bbb");
//		Book book = test.createUserBook(user, "aaaaa");
//		test.deleteBook(book);
//		
//		System.exit(1);
//		
//	}
	
	
}
