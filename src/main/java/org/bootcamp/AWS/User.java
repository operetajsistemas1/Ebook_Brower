package org.bootcamp.AWS;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.FetchType;
import javax.persistence.*;




@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@OneToMany(mappedBy = "user")
	@OrderBy("id ASC")
	private Set<Book> books;
	
	@OneToOne(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name="book_id")
	private Book book_id;
	
	public Book getBookId() {
		return book_id;
	}

	public void setBookId(Book book_id) {
		this.book_id = book_id;
	}

	private final String name;
	private String password;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	public User( String name, String password) {
		super();
		this.name = name;
		this.password = password;
		books = new HashSet<Book>();	
	}

//	public User() {
//		super();
//		books = new HashSet<Book>();
//	}
	
	public void addBook(Book book) {
		// 
		System.out.println("Invoice add item2: " + book.toString());
		books.add(book);
		System.out.println("Invoice content " + this.toString());		
		
	}
	@javax.persistence.OneToMany(mappedBy = "user") 
	public Set<Book> getItems() {
		
		return this.books;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [name=" + name + "]";
	}
	
	
	
	
	
}

