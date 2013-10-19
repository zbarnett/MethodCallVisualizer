package com.bat.soloz.ui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.bat.soloz.parserinterface.MethodNode;

/**
 *
 * @author zebulun
 */
public class VisualNode extends JPanel {
    private MethodNode methodNode;
    
    VisualNode(MethodNode methodNode){
        this.methodNode = methodNode;
        
        setPreferredSize(new Dimension(50, 50));
        add(new JLabel(methodNode.getShortName()));
        setBorder(BorderFactory.createLineBorder(Color.black));
    }
    
    // add mouse listener to make node draggable
}
