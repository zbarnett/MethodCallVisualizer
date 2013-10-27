package com.bat.soloz.ui;

import com.bat.soloz.graph.MethodNode;
import com.bat.soloz.graph.Vector2;
import com.bat.soloz.parserinterface.ParserInterface;
import java.awt.Color;
import java.awt.Dimension;
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

			for(MethodNode methodNode : methodNodes){
				VisualNode visualNode = new VisualNode(methodNode, this);
				this.add(visualNode);
				visualNodes.add(visualNode);
				visNodes.put(methodNode.getLongName(), visualNode);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(java.awt.Graphics g){ // TODO: use clipping region to speed up rendering
		super.paintComponent(g); // so that the entire screen is refreshed
		
		g.setColor(Color.BLACK);
		
		if(visNodes != null && visNodes.size() > 0)
			for(VisualNode visualNode : visNodes.values()){
				if(visualNode.getMethodNode().getChildren() != null)
				for(MethodNode otherNode : visualNode.getMethodNode().getChildren()){
					Vector2 from = visualNode.getCallerPlugLoc();
					Vector2 to = visNodes.get(otherNode.getLongName()).getCalleePlugLoc();
					
					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.drawLine(from.x, from.y, to.x, to.y);
					
					// TODO: draw splines!
				}
			}
	}
}