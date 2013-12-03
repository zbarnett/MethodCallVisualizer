package com.bat.soloz.ui;

import com.bat.soloz.graph.MethodNode;
import com.bat.soloz.graph.Vector2;
import com.bat.soloz.parserinterface.ParserInterface;
import japa.parser.ParseException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author zebulun
 */
public class GraphView extends JScrollPane {
	private GraphPanel graphPanel;
	private String name;
	private File file;
	HashMap<String, VisualNode> visualNodes;
	// TODO: store edges here too so they can be drawn quickly by simple iteration
	
	GraphView(final File file){
		super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		graphPanel = new GraphPanel();
		setViewportView(graphPanel);
		
		this.file = file;
		name = file.getName();
		visualNodes = new HashMap<>();
		
		try {
			analyze();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getName(){
		return name;
	}
	
	public Rectangle getBoundingBox(){
		Rectangle box = new Rectangle();
		
		box.x = Integer.MAX_VALUE;
		box.y = Integer.MAX_VALUE;
		box.width = 0;
		box.height = 0;
		
		int x2 = 0;
		int y2 = 0;
		
		for(VisualNode node : visualNodes.values()){
			if(node.getX() < box.x)
				box.x = node.getX();
			if(node.getY() < box.y)
				box.y = node.getY();
			if(node.getX()+node.getWidth() > x2)
				x2 = node.getX() + node.getWidth();
			if(node.getY()+node.getHeight() > y2)
				y2 = node.getY() + node.getHeight();
		}
		
		box.width = x2 - box.x - 1; // minus one cause it looked bad
		box.height = y2 - box.y - 1;
		
		return box;
	}
	
	private void updateViewport(Rectangle box){
		Dimension newSize = new Dimension(box.width, box.height);
		//graphPanel.setSize(newSize);
		graphPanel.setPreferredSize(newSize);
		graphPanel.scrollRectToVisible(box);
		graphPanel.revalidate();
		graphPanel.repaint();
	}
	
	private void analyze() {
		graphPanel.removeAll();
		visualNodes.clear();
		
		try{
			LinkedList<MethodNode> methodNodes = ParserInterface.analyzeSourceCode(file);

			for(MethodNode methodNode : methodNodes){
				VisualNode visualNode = new VisualNode(methodNode, graphPanel);

				// TODO: lay visual nodes out in an appealing manner
				visualNode.setLocation((int)(Math.random()*graphPanel.getWidth()), (int)(Math.random()*graphPanel.getHeight()));

				graphPanel.add(visualNode);
				visualNodes.put(methodNode.getLongName(), visualNode);
			}
		} catch(ParseException prse){
			JOptionPane.showMessageDialog(null, "Error occurred while parsing file " + prse.getMessage(), "Parse Error", JOptionPane.ERROR_MESSAGE);
			prse.printStackTrace();
		} catch(IOException ioe){
			JOptionPane.showMessageDialog(null, "Error occurred while reading file " + ioe.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
			ioe.printStackTrace();
		}
		
	}
	
	public void reanalyze(){
		try{
			analyze();
			repaint();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void drawSpline(final Graphics g, final Vector2 from, final Vector2 to){
		final double MINCURVE = 35;
		
		double x0 = from.x;
		double y0 = from.y;
		
		double x1 = from.x;
		double y1 = Math.max((from.y + to.y)/2 - from.y, MINCURVE) + from.y;
		double x2 = to.x;
		double y2 = to.y - Math.max((from.y + to.y)/2 - from.y, MINCURVE);
		
		double x3 = to.x;
		double y3 = to.y;
		
		
		double A = x3 - 3*x2 + 3*x1 - x0;
		double B = 3*x2 - 6*x1 + 3*x0;
		double C = 3*x1 - 3*x0;
		double D = x0;
		
		double E = y3 - 3*y2 + 3*y1 - y0;
		double F = 3*y2 - 6*y1 + 3*y0;
		double G = 3*y1 - 3*y0;
		double H = y0;
		
		for(double t = 0.0; t <= 1.0; t += 0.001){ // TODO: find largest value to increment t by
			int x = (int)((((A*t) + B)*t + C)*t + D);
			int y = (int)((((E*t) + F)*t + G)*t + H);
			g.drawRect(x, y, 1, 1); // TODO: this is very slow
		}
	}
	
	private class GraphPanel extends JPanel{
		GraphPanel(){
			setLayout(null);
			setSize(new Dimension(400, 400));
			setPreferredSize(new Dimension(400, 400));
			setBackground(new Color(0, 255, 0, 255));
			revalidate();
			repaint();
		}
		
		@Override
		public void paintComponent(Graphics g){ // TODO: use clipping region to speed up rendering
			super.paintComponent(g); // so that the entire screen is refreshed

			g.setColor(Color.BLACK);

			Rectangle box = getBoundingBox();
			updateViewport(box);
			
			// draw bounding box for debug purposes
			//g.drawRect(box.x, box.y, box.width, box.height);

			// TODO: use list of edges so they can be drawn quickly by simple iteration
			if(visualNodes != null && visualNodes.size() > 0){
				for(VisualNode visualNode : visualNodes.values()){
					if(visualNode.getMethodNode().getChildren() != null){
						for(MethodNode otherNode : visualNode.getMethodNode().getChildren()){
							// we don't want to draw a line to itself
							if(visualNode.getMethodNode() == otherNode)
								continue;
							
							Vector2 from = visualNode.getCallerPlugLoc();
							Vector2 to = visualNodes.get(otherNode.getLongName()).getCalleePlugLoc();

							drawSpline(g, from, to);
						}
					}
				}
			}
		}
	}
}