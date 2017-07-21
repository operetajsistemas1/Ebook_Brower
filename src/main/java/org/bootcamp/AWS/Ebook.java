package org.bootcamp.AWS;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import com.github.mertakdut.BookSection;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.OutOfPagesException;
import com.github.mertakdut.exception.ReadingException;
import java.awt.BorderLayout;

import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;

import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.Iterator;

import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.JSONException;

public class Ebook implements Serializable {

	/**
	 * 
	 */
	private static JList list;
	private static Ebook ebook;
	private static User user;
	private static MouseListener mouseListener;
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	JToolBar toolBar;
	Reader reader = new Reader();
	JTextPane textPane;
	int page = 0;
	private JTextArea tarea = new JTextArea(10, 10);
	private JTextField tfield = new JTextField(10);
	private volatile boolean isThreadRunning = false;
	static File f1;
	static private ObjectOutputStream oos;
	static private ObjectInputStream ois;
	static private String name = "user1";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					f1 = new File("Books.dat");
					if (f1.exists()) {

						user = new User();
						ois = new ObjectInputStream(new FileInputStream(f1));
						user = (User) ois.readObject();
						// ois.close();
						System.out.println("exists");
						User tempUser = ApacheHttpClientGet.searchUser(user);
						if (tempUser == null) {
							user = ApacheHttpClientGet.inserUser(user);
						} else {
							System.out.println("Got user from server:" + tempUser.toString());
							user = tempUser;
							updateUser(tempUser);
						}

						Book book = null;
						if (user.getBookId() == 0) {
							ebook = new Ebook(book);
						} else {
							int bookId = user.getBookId();
							Iterator<Book> iter = user.getItems().iterator();
							while (iter.hasNext()) {
								book = iter.next();
								if (book.getId() == bookId)
									break;////// this.bookId=book.getId()
								else
									book = null;
							}
							ebook = new Ebook(book);
						}

					} else {
						f1.createNewFile();
						user = new User(name, "parole");
						user.setBookId(0);
						System.out.println("not exists");
						User tempUser = ApacheHttpClientGet.searchUser(user);

						if (tempUser != null) {
							user = tempUser;

						} else {
							user = ApacheHttpClientGet.inserUser(user);
						}
						Book book = null;
						ebook = new Ebook(book);
						ebook.saveUser();
						System.out.println(user.toString());
						try {
							f1.createNewFile();
						} catch (IOException e) {
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
		frame.setBounds(150, 150, 500, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		toolBar = new JToolBar();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		toolBar.setVisible(true);
		if (book != null) {
			displayBook(book);
		} else {
			displayList();
		}

	}

	public void displayBook(Book book) {
		if (list != null) {
			list.setVisible(false);

		}
		toolBar.removeAll();
		toolBar.repaint();
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				book.setLine(book.getLine() - 1);
				displayPage(book);
				try {
					ApacheHttpClientGet.updateBook(book, user);
				} catch (IOException | JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		toolBar.removeAll();
		toolBar.repaint();
		toolBar.add(backButton);
		JButton forewardButton = new JButton("Forward");
		forewardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				book.setLine(book.getLine() + 1);
				displayPage(book);
				try {
					ApacheHttpClientGet.updateBook(book, user);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
		textPane.removeAll();
		textPane.repaint();
		StyledDocument doc = textPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_JUSTIFIED);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);

		JButton btnChangeFont = new JButton("Change Font");
		btnChangeFont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Font font = new Font("Serif", Font.ITALIC, 12);
				textPane.setFont(font);
				toolBar.repaint();
			}
		});
		btnChangeFont.setHorizontalAlignment(SwingConstants.RIGHT);
		toolBar.add(btnChangeFont);


		frame.getContentPane().add(textPane, BorderLayout.CENTER);
		textPane.setVisible(true);
		reader.setMaxContentPerSection(1250); // Max string length for the
		reader.setIsIncludingTextContent(true); // Optional, to return the
		try {
			//// reader.setFullContent(book.getLocation()+"/"+book.getName()+
			//// ".epub");
			reader.setFullContent(book.getName() + ".epub");
		} catch (ReadingException e1) {
			e1.printStackTrace();
		} // Must call before readSection.
		frame.setVisible(true);
		displayPage(book);

	}

	public void displayList() {
		if (textPane != null) {
			// list.disable();
			// list.hide();
			// list.removeAll();
			textPane.setVisible(false);
			// frame.remove(list);
			// frame.repaint();

		}
		System.out.println("display list user" + user.toString());
		Vector<Book> books = user.getItems();
		toolBar.removeAll();
		toolBar.repaint();
		JButton btnAddABook = new JButton("Add a Book");
		btnAddABook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

				int returnValue = jfc.showOpenDialog(null);
				// int returnValue = jfc.showSaveDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					String path = selectedFile.getParent();
					String nameWithExtension = selectedFile.getName();
					String name = nameWithExtension.replaceFirst("[.][^.]+$", "");
					String extension = nameWithExtension.substring(nameWithExtension.lastIndexOf(".") + 1);
					if (extension.equalsIgnoreCase("epub")) {
						Book book = new Book(name);
						book.setLocation(path);
						book.setExist(true);
						book.setUser(user);
						book.setLine(0);
						System.out.println("adding new item");
						try {
							Book book2 = ApacheHttpClientGet.inserBook(book, user);
							book.setId(book2.getId());
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}

						System.out.println("Imported book: " + book.toString());
						System.out.println(book.toString() +": " +book.getLocation());
						user.addBook(book);
						saveUser();
						bookChosen(book);
						textPane.setVisible(true);
					}

				}
			}
		});
		btnAddABook.setHorizontalAlignment(SwingConstants.RIGHT);
		toolBar.add(btnAddABook);

		list = new JList(user.getItems());
		System.out.println(user.getItems().toString());


		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {

					Iterator<Book> iter = user.getItems().iterator();
					Book b = null;
					for (int i = 0; iter.hasNext(); i++) {
						System.out.println(b.toString() +": " +b.getLocation());
						b = iter.next();
						if (i == list.getSelectedIndex()) {
							break;
						}
					}
					bookChosen(b);

				}
			}
		});

		frame.getContentPane().add(list, BorderLayout.CENTER);
		// frame.add(list);
		frame.setVisible(true);
		list.setVisible(true);

	}
	public void bookChosen(Book b){
		user.setBookId(b.getId());
		try {
			user = ApacheHttpClientGet.updateUser(user);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		displayBook(b);
	}
	// public static void updateJList() {
	// //list.removeAll();
	// list.removeAll();
	// list= new JList(user.getItems());
	// list.repaint();
	// frame.getContentPane().add(list, BorderLayout.CENTER);
	//
	//
	//
	// }

	public static void saveUser() {
		System.out.println("Saving user: " + user.toString());
		try {
			oos = new ObjectOutputStream(new FileOutputStream(f1));
			oos.writeObject(user);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void updateUser(User serverUser) {
		System.out.println("update user: " + user.toString());
		System.out.println("update server user: " + serverUser.toString());
		Iterator<Book> iter = serverUser.getItems().iterator();
		while (iter.hasNext()) {
			Book book = iter.next();
			if (user.getItems().contains(book)) {
				System.out.println("Contains: " + book.toString());
				Iterator<Book> iterLocal = user.getItems().iterator();
				while (iterLocal.hasNext()) {
					Book bookLocal = iterLocal.next();
					if (bookLocal.equals(book))
						bookLocal.setLine(book.getLine());
				}

			} else {
				book.setExist(false);
				user.addBook(book);
			}
		}

		System.out.println("update done: " + user.toString());
	}

	public void displayPage(Book book) {

		BookSection bookSection;
		try {
			bookSection = reader.readSection(book.getLine());
			String sectionContent = bookSection.getSectionContent(); // Returns
			// content
			// as
			// html.
			String sectionTextContent = bookSection.getSectionTextContent(); // Excludes
			// html
			// tags.
			//System.out.println(sectionTextContent);
			textPane.setText(sectionTextContent);
			System.out.println("getSectionContent()");
			///// System.out.println(bookSection.getSectionContent());
			// System.out.println(bookSection.getSectionTextContent());
			/// System.out.println(bookSection.getLabel());
			//		book = ApacheHttpClientGet.updateBook(book, user);
		} catch (ReadingException e1) {
			e1.printStackTrace();
		} catch (OutOfPagesException e1) {
			e1.printStackTrace();

		}

	}
}