package com.bat.soloz.ui;

import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author zebulun
 */
public class Starter {
	static JFrame window = null;
	static JPanel mainPanel = null;
	
	public static void main(final String[] args){
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
					javax.swing.ToolTipManager.sharedInstance().setDismissDelay(10000); // set tooltip timeout to 10s 
				} catch (Exception e) {}
				window = new JFrame("Method Call Visualizer");
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				mainPanel = new MainView(args[0]);
				window.getContentPane().add(mainPanel);

				window.setResizable(true);

				//Display the window.
				window.pack();
				window.setLocationRelativeTo(null);  // positions window in center of screen
				window.setVisible(true);
				
				// setup JFrame
				JMenuBar menuBar = new JMenuBar();
				window.setJMenuBar(menuBar);
		
				JMenu fileMenu = new JMenu("File");
				menuBar.add(fileMenu);
				fileMenu.add(new JMenuItem("New", KeyEvent.VK_N));
				fileMenu.add(new JMenuItem("Open", KeyEvent.VK_O));
				fileMenu.add(new JMenuItem("Exit", KeyEvent.VK_X));

				JMenu editMenu = new JMenu("Edit");
				menuBar.add(editMenu);
				editMenu.add(new JMenuItem("Re-analyze", KeyEvent.VK_R));

				JMenu helpMenu = new JMenu("Help");
				menuBar.add(helpMenu);
				helpMenu.add(new JMenuItem("About", KeyEvent.VK_A));
			}
		});
	}
}