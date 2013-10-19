package com.bat.soloz.ui;

import java.awt.Dimension;
import java.io.File;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import com.bat.soloz.parserinterface.MethodNode;
import com.bat.soloz.parserinterface.ParserInterface;

/**
 *
 * @author zebulun
 */
public class MainUI extends JPanel {

    static JFrame window = null;
    static JPanel mainPanel = null;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    ToolTipManager.sharedInstance().setDismissDelay(10000); // set tooltip timeout to 10s 
                } catch (Exception e) {
                }
                window = new JFrame("Java Method Call Visualizer");

                mainPanel = new MainUI();
                window.getContentPane().add(mainPanel);

                window.setResizable(true);

                //Display the window.
                window.pack();
                window.setLocationRelativeTo(null);  // positions window in center of screen
                window.setVisible(true);
            }
        });
    }
    
    private MainUI(){
        // TODO: add project to github
        
        // setup gui stuff first
        setPreferredSize(new Dimension(800, 600));
        
        // now setup method parser stuff
        File sourceFile = new File("test.java");
        LinkedList<MethodNode> methodNodes = null;
        
        try {
            methodNodes = ParserInterface.analyzeSourceFile(sourceFile);
        } catch(Exception e){
            System.out.println("An error has occurred while parsing the source file: " + sourceFile);
            System.out.println("The program will now terminate.");
            
            e.printStackTrace();
            System.exit(1);
        }
        
        for(MethodNode methodNode : methodNodes){
            add(new VisualNode(methodNode));
        }
    }
}