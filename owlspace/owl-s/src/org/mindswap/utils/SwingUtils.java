/*
 * Created on Oct 30, 2004
 */
package org.mindswap.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Evren Sirin
 */
public class SwingUtils {
	public final static ActionListener windowCloserAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JComponent c = (JComponent) e.getSource();
			Window w = (Window) c.getTopLevelAncestor();  
			w.dispose();
		}
	};
	
    public static void centerFrame(Window frame) {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = frame.getSize();
		screenSize.height = screenSize.height/2;
		screenSize.width = screenSize.width/2;
		size.height = size.height/2;
		size.width = size.width/2;
		int y = screenSize.height - size.height;
		int x = screenSize.width - size.width;
		
		frame.setLocation(x, y);
    }

	
	public static void showMessage(String title, String str) {		
		JFrame info = new JFrame(title);
		JTextArea t = new JTextArea(str, 15, 40);
		t.setEditable(false);
		t.setLineWrap(true);
		t.setWrapStyleWord(true);
		JButton ok = new JButton("Close");
		ok.addActionListener(windowCloserAction);
		info.getContentPane().setLayout(new BoxLayout(info.getContentPane(), BoxLayout.Y_AXIS));
		info.getContentPane().add(new JScrollPane(t));
		info.getContentPane().add(ok);
		ok.setAlignmentX(Component.CENTER_ALIGNMENT);
		info.pack();
		//info.setResizable(false);
		centerFrame(info);
		//info.show();
		info.setVisible(true);
	}
}
