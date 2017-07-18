package org.bootcamp.AWS;

import java.awt.EventQueue;

import javax.swing.JFrame;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.text.Document;

import org.springframework.util.StringUtils;

public class Ebook {

	private JFrame frame;
	Reader reader = new Reader();
	JTextPane textPane;
	int page = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ebook window = new Ebook();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		System.out.println("file url location: " + new java.io.File("").getAbsolutePath());
		System.out.println("file url location: " + new java.io.File("").getPath());

		File f1 = new File("books.dat");
		if (f1.exists()) {
			System.out.println("exists");
			System.out.println("not created");
		} else {
			System.out.println("not exists");
			System.out.println("created file");
			try {
				f1.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			FileInputStream fin;
			fin = new FileInputStream(f1);
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));
			String line = null;
			List<String> li = new LinkedList<String>();
			try {
				while ((line = br.readLine()) != null) {
					System.out.println("output list entries beneath");
					System.out.println(line);
					li.add(line);

				}

				String str;
				while (((str = br.readLine())) != null) {
					String[] ar = str.split(",");
				}
				br.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Create the application.
	 */
	public Ebook() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JToolBar toolBar = new JToolBar();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);

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

		JButton btnNewButton = new JButton("Table of Contents");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				BookSection content;
				try {
					content = reader.readSection(3);
					String stringOfContent = content.getSectionTextContent(); // Excludes
																				// html
																				// tags.
					textPane.setText(stringOfContent);
				} catch (ReadingException e) {
					e.printStackTrace();
				} catch (OutOfPagesException e) {
					e.printStackTrace();
				}

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
