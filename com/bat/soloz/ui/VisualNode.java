package com.bat.soloz.ui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.bat.soloz.graph.MethodNode;
import com.bat.soloz.graph.Vector2;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author zebulun
 */
public class VisualNode extends JPanel {

	private MethodNode methodNode;

	VisualNode(MethodNode methodNode) {
		this.methodNode = methodNode;

		setPreferredSize(new Dimension(50, 50));
		add(new JLabel(methodNode.getShortName()));
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		MagicMouseMover mouseListener = new MagicMouseMover(this);
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);
	}
	// add mouse listener to make node draggable
	
		private class MagicMouseMover implements MouseMotionListener, MouseListener {
		private JPanel panel;
		private Vector2 mouseStart;
		private Vector2 panelStart;
		
		MagicMouseMover(JPanel panel){
			this.panel = panel;
			mouseStart = new Vector2();
			panelStart = new Vector2();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// get coords of both parent jpanel and cursor
			mouseStart.x = e.getXOnScreen();
			mouseStart.y = e.getYOnScreen();
			
			panelStart.x = panel.getX();
			panelStart.y = panel.getY();
			
			System.out.println("coords set");
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			int deltaX = e.getXOnScreen() - mouseStart.x;
			int deltaY = e.getYOnScreen() - mouseStart.y;
			
			panel.setLocation(panelStart.x + deltaX, panelStart.y + deltaY);
			panel.repaint();
		}
		@Override
		public void mouseMoved(MouseEvent e) {}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	 }
}