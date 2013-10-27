package com.bat.soloz.ui;

import com.bat.soloz.graph.MethodNode;
import com.bat.soloz.graph.Vector2;
import com.bat.soloz.parserinterface.ParserInterface;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.HashMap;

import java.util.LinkedList;
import javax.swing.JPanel;

/**
 *
 * @author zebulun
 */
public class GraphView extends JPanel {
	LinkedList<VisualNode> visualNodes;
	HashMap<String, VisualNode> visNodes;
	
	GraphView(final java.io.File sourceFile){
		setLayout(null);
		try {
			LinkedList<MethodNode> methodNodes = ParserInterface.analyzeSourceFile(sourceFile);
			
			visualNodes = new LinkedList<>();
			visNodes = new HashMap<>();
		
			Dimension size = new Dimension(400, 400);
			setPreferredSize(size);
			setSize(size);
			setBackground(new Color(0, 255, 0, 255));
			// TODO: lay visual nodes out in an appealing manner

			System.out.println("GraphView width:"+getWidth() + " height:"+getHeight());
			
			for(MethodNode methodNode : methodNodes){
				VisualNode visualNode = new VisualNode(methodNode, this);
				
				visualNode.setLocation((int)(Math.random()*getWidth()), (int)(Math.random()*getHeight()));
				
				this.add(visualNode);
				visualNodes.add(visualNode);
				visNodes.put(methodNode.getLongName(), visualNode);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g){ // TODO: use clipping region to speed up rendering
		super.paintComponent(g); // so that the entire screen is refreshed
		
		g.setColor(Color.BLACK);
		
		if(visNodes != null && visNodes.size() > 0)
			for(VisualNode visualNode : visNodes.values()){
				if(visualNode.getMethodNode().getChildren() != null)
				for(MethodNode otherNode : visualNode.getMethodNode().getChildren()){
					Vector2 from = visualNode.getCallerPlugLoc();
					Vector2 to = visNodes.get(otherNode.getLongName()).getCalleePlugLoc();

					drawSpline(g, from, to);
				}
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
}