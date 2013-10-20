package com.bat.soloz.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
				} catch (Exception e) {
				}
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

	private MainUI(String fileName) {
		// setup gui stuff first
		setPreferredSize(new java.awt.Dimension(800, 600));

		GraphView view1 = new GraphView(new java.io.File(fileName));
		add(view1);
	}
}