package com.bat.soloz.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author zebulun
 */
public class MainView extends JPanel {
	private JTabbedPane tabbedPane;

	MainView() {
		setPreferredSize(new Dimension(800, 600));
		setLayout(new BorderLayout());
		
		tabbedPane = new JTabbedPane();
		add(tabbedPane);
	}
	
	public void reanalyze(){
		GraphView view = (GraphView)tabbedPane.getSelectedComponent();
		view.reanalyze();
	}
	
	public void addTab(File file){
		GraphView view = new GraphView(file);
		tabbedPane.addTab(view.getName(), null, view, null); // tabname, icon, panel, mouseover
		
		// make newest tab the active tab
		tabbedPane.setSelectedComponent(view);
	}
}