package com.bat.soloz.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author zebulun
 */
public class Starter {
	static JFrame window;
	static MainView mainView;
	
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

				mainView = new MainView();
				window.getContentPane().add(mainView);
				
				// so command line and dragging will open file to analyze
				if(args != null && args.length > 0)
					mainView.addTab(new File(args[0]));

				window.setResizable(true);

				//Display the window.
				window.pack();
				window.setLocationRelativeTo(null);  // positions window in center of screen
				window.setVisible(true);
				
				// setup menu
				JMenuBar menuBar = new JMenuBar();
				window.setJMenuBar(menuBar);
				
				//Initialize listener for the top horizontal menu bar
				MenuListener menuListener = new MenuListener();
		
				// file menu
				JMenu fileMenu = new JMenu("File");
				menuBar.add(fileMenu);
				
				JMenuItem openFileMenuItem = new JMenuItem("Analyze File", KeyEvent.VK_O);
				openFileMenuItem.setActionCommand("Analyze File");
				openFileMenuItem.addActionListener(menuListener);
				fileMenu.add(openFileMenuItem);
				
				JMenuItem openDirectoryMenuItem = new JMenuItem("Analyze Directory", KeyEvent.VK_O);
				openDirectoryMenuItem.setActionCommand("Analyze Directory");
				openDirectoryMenuItem.addActionListener(menuListener);
				fileMenu.add(openDirectoryMenuItem);
				
				JMenuItem closeTabMenuItem = new JMenuItem("Close Tab", KeyEvent.VK_C);
				closeTabMenuItem.setActionCommand("Close Tab");
				closeTabMenuItem.addActionListener(menuListener);
				fileMenu.add(closeTabMenuItem);
				
				JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
				exitMenuItem.setActionCommand("Exit");
				exitMenuItem.addActionListener(menuListener);
				fileMenu.add(exitMenuItem);

				// edit menu
				JMenu editMenu = new JMenu("Edit");
				menuBar.add(editMenu);
				
				JMenuItem reanalyzeMenuItem = new JMenuItem("Re-analyze", KeyEvent.VK_R);
				reanalyzeMenuItem.setActionCommand("Re-analyze");
				reanalyzeMenuItem.addActionListener(menuListener);
				editMenu.add(reanalyzeMenuItem);

				// help menu
				JMenu helpMenu = new JMenu("Help");
				menuBar.add(helpMenu);
				
				JMenuItem aboutMenuItem = new JMenuItem("About", KeyEvent.VK_A);
				aboutMenuItem.setActionCommand("About");
				aboutMenuItem.addActionListener(menuListener);
				helpMenu.add(aboutMenuItem);
			}
		});
	}
	
	private static class MenuListener implements ActionListener {
		final JFileChooser openDialog;
		
		MenuListener(){
			super();
			
			openDialog = new JFileChooser();
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			switch(event.getActionCommand()){
				case "Analyze File" : analyzeFile(); break;
				case "Analyze Directory" : analyzeDirectory(); break;
				case "Close Tab" : closeTab(); break;
				case "Exit" : exit(); break;
				case "Re-analyze" : reanalyze(); break;
				case "About" : about(); break;
				default : System.out.println("Menu Listener error!"); break;
			}
		}
		
		private void analyzeFile(){
			openDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
			int returnVal = openDialog.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION)
				mainView.addTab(openDialog.getSelectedFile());
		}
		
		private void analyzeDirectory(){
			openDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			int returnVal = openDialog.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION)
				mainView.addTab(openDialog.getSelectedFile());
		}
		
		private void closeTab() {
			mainView.closeActiveTab();
		}
		
		private void exit(){
			System.exit(0);
		}
		
		private void reanalyze(){
			mainView.reanalyze();
		}
		
		private void about(){
			JOptionPane.showMessageDialog(mainView, "This program is designed to provide a graphical overview\n"+
					"of the method call structure of a Java program.\n\n"+
					"Any questions or comments can be sent to\n\n"+
					"Zebulun Barnett\nspacephlite@gmail.com", "About", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}