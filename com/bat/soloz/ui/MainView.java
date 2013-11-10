package com.bat.soloz.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author zebulun
 */
public class MainView extends JPanel {
	
	private LinkedList<GraphView> views;

	MainView(final String fileName) {
		views = new LinkedList<>();
		
		setPreferredSize(new Dimension(800, 600));
		setLayout(new BorderLayout());
		
		JTabbedPane tabbedPane = new JTabbedPane();
		add(tabbedPane);
		
		GraphView view = new GraphView(new java.io.File(fileName));
		views.add(view);
		JScrollPane scroller = new JScrollPane(view, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroller.updateUI();
		tabbedPane.addTab("Tab "+views.size(), null, scroller, null); // tabname, icon, panel, mouseover
	}
}