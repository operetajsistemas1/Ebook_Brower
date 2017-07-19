package org.bootcamp.AWS;

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import com.github.mertakdut.BookSection;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.OutOfPagesException;
import com.github.mertakdut.exception.ReadingException;

import java.awt.BorderLayout;
import javax.swing.JDesktopPane;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.text.Document;

import org.springframework.util.StringUtils;

public class Ebook implements Serializable {

	/**
	 * 
	 */
	private static User user;
	private static final long serialVersionUID = 1L;
	//// private JFrame frame2;
	private JFrame frame;
	JToolBar toolBar;
	Reader reader = new Reader();
	JTextPane textPane;
	int page = 0;
	private JTextArea tarea = new JTextArea(10, 10);
	private JTextField tfield = new JTextField(10);
	private volatile boolean isThreadRunning = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					File f1 = new File("Books.dat");
					ObjectOutputStream oos;
					ObjectInputStream ois;
					if (f1.exists()) {
						Set<User> users= new HashSet<User>();
						user = new User(null,null);
						ois = new ObjectInputStream(new FileInputStream(f1));
						//////////////users.add((User) ois.readObject());
						user=(User) ois.readObject();
					
						
						ois.close();					
						System.out.println("exists");
						Book book = null;
						if (user.getBookId() == 0){
							//TODO: iet uz grāmatu sarakstu
							new Ebook(book);
						} else {
							int bookId  = user.getBookId();
							Iterator<Book> iter = user.getItems().iterator();
							while (iter.hasNext()){
								book = iter.next();
								if (book.getId()== bookId) break;
								else book = null;
							}
							new Ebook(book);
						}
					} else {
						user = new User("Andris","parole");
						oos = new ObjectOutputStream(new FileOutputStream(f1));
						oos.writeObject(user);
						oos.close();
						System.out.println("not exists");

						Book book = null;
						new Ebook(book);
						try {
							f1.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Ebook(Book book) {
		System.out.println("Ebook init");
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		toolBar = new JToolBar();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		toolBar.setVisible(true);
		if (book != null){
			displayBook(book);
		}else {
			displayList();
		}

	}

	
	public void displayBook(Book book){

		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				page--;
				displayPage(page);

			}
		});
		toolBar.add(backButton);
		JButton forewardButton = new JButton("Forward");
		forewardButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				page++;
				displayPage(page);
			}

		});
		toolBar.add(forewardButton);
		
		JButton btnNewButton = new JButton("Books List");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayList();
			}
		});
		
		btnNewButton.setHorizontalAlignment(SwingConstants.RIGHT);
		toolBar.add(btnNewButton);
		
		
		
		
		
		
		
		textPane = new JTextPane();
		frame.getContentPane().add(textPane, BorderLayout.CENTER);

		reader.setMaxContentPerSection(3000); // Max string length for the
												// current page.
		reader.setIsIncludingTextContent(true); // Optional, to return the
												// tags-excluded version.
		try {
			reader.setFullContent("Metamorphosis-jackson.epub");
		} catch (ReadingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // Must call before readSection.

		displayPage(page);
		
	}
	
	

	
	public void displayList(){
		
		Set<Book> books = user.getItems();
		JButton btnAddABook = new JButton("Add a Book");
		btnAddABook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JButton	saveButton = new JButton("Save a Book");
				
					
						frame.add(saveButton);
						toolBar.add(saveButton);
						
						
					
				
				
			}
		});
		btnAddABook.setHorizontalAlignment(SwingConstants.RIGHT);
		toolBar.add(btnAddABook);

				

//				frame.getContentPane().removeAll();
//				// frame.removeAll(); /////or remove(JComponent)
//				// frame.revalidate();
//				frame.getContentPane().repaint();
//				frame.setBounds(100, 100, 450, 300);

//				BookSection content;
//				try {
//					content = reader.readSection(3);
//					String stringOfContent = content.getSectionTextContent();
//					textPane.setText(stringOfContent);
//				} catch (ReadingException e) {
//					e.printStackTrace();
//				} catch (OutOfPagesException e) {
//					e.printStackTrace();
//				}

				//// tarea.setText("Hello there\n");
				//// tarea.append("Hello student://");
//				User user1 = new User(null, null);
//				
//				
//				Book b = new Book();
//				b.setId(1);
//			//	book.setName("Metamorphosis-jackson.epub");
//				FileOutputStream fos;
//				
//				user1.addBook(b);
				
//				try {
//					fos = new FileOutputStream("Books.dat");
//					ObjectOutputStream oos = new ObjectOutputStream(fos);
//					oos.writeObject(user1.getItems());
//					oos.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

//				try {
//					FileInputStream fis = new FileInputStream("Books.dat");
//					ObjectInputStream ois = new ObjectInputStream(fis);
//					
//					Set<Book> set = (Set<Book>) ois.readObject();
//					////System.out.println("to id in : function bookslist" + bin.getId());
//
//					for(Book b2: set) {
//						System.out.println("pirmaja elementa saturs" + b2.getId());
//					    break;
//					}
//					
//					ois.close();
//				} catch (ClassNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

//				JScrollPane scroll = new JScrollPane(tarea);
//
//				tfield.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent ae) {
//						tarea.append(tfield.getText() + "\n");
//					}
//				});

				tarea.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						int x = me.getX();
						int y = me.getY();
						System.out.println("X : " + x);
						System.out.println("Y : " + y);
						int startOffset = tarea.viewToModel(new Point(x, y));
						System.out.println("Start Offset : " + startOffset);
						String text = tarea.getText();
						int searchLocation = text.indexOf("student://", startOffset);
						System.out.println("Search Location : " + searchLocation);
				//		if (searchLocation == startOffset)
					//		JOptionPane.showMessageDialog(scroll, Ebook.this, "BINGO you found me.", searchLocation);
					}
				});

//				frame.getContentPane().add(scroll, BorderLayout.CENTER);
//				frame.getContentPane().add(tfield, BorderLayout.PAGE_END);
//				frame.pack();
//				frame.setBounds(100, 100, 450, 300);
//				//// frame3.setLocationByPlatform(true);
				frame.setVisible(true);




		
		
	}
	
	
	
	
	
	
	
	
	

	public void displayPage(int page) {

		BookSection bookSection;
		try {
			bookSection = reader.readSection(page);
			String sectionContent = bookSection.getSectionContent(); // Returns
																		// content
																		// as
																		// html.
			String sectionTextContent = bookSection.getSectionTextContent(); // Excludes
																				// html
																				// tags.
			System.out.println(sectionTextContent);
			textPane.setText(sectionTextContent);
			System.out.println("getSectionContent()");
			///// System.out.println(bookSection.getSectionContent());
			// System.out.println(bookSection.getSectionTextContent());
			/// System.out.println(bookSection.getLabel());

		} catch (ReadingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (OutOfPagesException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}