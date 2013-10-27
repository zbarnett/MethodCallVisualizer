package com.bat.soloz.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author zebulun
 */
public class MainUI extends JPanel {

	static JFrame window = null;
	static JPanel mainPanel = null;

	public static void main(final String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
					javax.swing.ToolTipManager.sharedInstance().setDismissDelay(10000); // set tooltip timeout to 10s 
				} catch (Exception e) {}
				window = new JFrame("Java Method Call Visualizer");
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				mainPanel = new MainUI(args[0]);
				window.getContentPane().add(mainPanel);

				window.setResizable(true);

				//Display the window.
				window.pack();
				window.setLocationRelativeTo(null);  // positions window in center of screen
				window.setVisible(true);
			}
		});
	}

	private MainUI(final String fileName) {
		// setup gui stuff first
		setPreferredSize(new Dimension(800, 600));
		setLayout(new BorderLayout());

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
		
		GraphView view1 = new GraphView(new java.io.File(fileName));
		JScrollPane scroller = new JScrollPane(view1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroller.updateUI();
		
		GraphView view2 = new GraphView(new java.io.File(fileName));
		JScrollPane scroller2 = new JScrollPane(view2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Tab 1", null, scroller, null); // tabname, icon, panel, mouseover
		tabbedPane.addTab("Tab 2", null, scroller2, null);
		add(tabbedPane);
	}
}