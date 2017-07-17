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
import java.awt.event.ActionEvent;


public class eBook {

	private JFrame frame;
	Reader reader = new Reader();
	JTextPane textPane;
	int page =0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					eBook window = new eBook();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public eBook() {
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
		
		JButton forewardButton = new JButton("Foreward");
		forewardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				page++;
				displayPage(page);
			}
			
			
			
			
		});
		toolBar.add(forewardButton);
		
		textPane = new JTextPane();
		frame.getContentPane().add(textPane, BorderLayout.CENTER);
		
		reader.setMaxContentPerSection(3000); // Max string length for the current page.
		reader.setIsIncludingTextContent(true); // Optional, to return the tags-excluded version.
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
			String sectionContent = bookSection.getSectionContent(); // Returns content as html.
			String sectionTextContent = bookSection.getSectionTextContent(); // Excludes html tags.
			System.out.println(sectionTextContent);
			textPane.setText(sectionTextContent);
		} catch (ReadingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (OutOfPagesException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
	}
	


}
