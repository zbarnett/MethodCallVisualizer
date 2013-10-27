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
	private JPanel parent;

	VisualNode(final MethodNode methodNode, final JPanel parent) {
		this.methodNode = methodNode;
		this.parent = parent;

		setLocation((int)(Math.random()*200), (int)(Math.random()*200));
		
		setSize(new Dimension(50, 50));
		setPreferredSize(new Dimension(50, 50));
		add(new JLabel(methodNode.getShortName()));
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		MagicMouseMover mouseListener = new MagicMouseMover(this);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
	}
	
	Vector2 getCalleePlugLoc(){
		Vector2 loc = new Vector2();
		loc.x = getX() + getWidth()/2;
		loc.y = getY();
		return loc;
	}
	
	Vector2 getCallerPlugLoc(){
		Vector2 loc = new Vector2();
		loc.x = getX() + getWidth()/2;
		loc.y = getY() + getHeight();
		return loc;
	}
	
	MethodNode getMethodNode(){
		return methodNode;
	}
	
	private class MagicMouseMover implements MouseMotionListener, MouseListener {
		private JPanel panel;
		private Vector2 mouseStart;
		private Vector2 panelStart;
		
		MagicMouseMover(final JPanel panel){
			this.panel = panel;
			mouseStart = new Vector2();
			panelStart = new Vector2();
		}

		@Override
		public void mousePressed(final MouseEvent e) {
			// get coords of both parent jpanel and cursor
			mouseStart.x = e.getXOnScreen();
			mouseStart.y = e.getYOnScreen();
			
			panelStart.x = panel.getX();
			panelStart.y = panel.getY();
			
			System.out.println("coords set");
		}
		@Override
		public void mouseDragged(final MouseEvent e) {
			int deltaX = e.getXOnScreen() - mouseStart.x;
			int deltaY = e.getYOnScreen() - mouseStart.y;
			
			panel.setLocation(panelStart.x + deltaX, panelStart.y + deltaY);
			parent.repaint();
		}
		@Override
		public void mouseMoved(final MouseEvent e) {}
		@Override
		public void mouseClicked(final MouseEvent e) {}
		@Override
		public void mouseReleased(final MouseEvent e) {}
		@Override
		public void mouseEntered(final MouseEvent e) {}
		@Override
		public void mouseExited(final MouseEvent e) {}
	 }
}