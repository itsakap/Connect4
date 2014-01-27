//GameFrame.java
//For setting up the frame with all components.

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GameFrame extends JFrame{

	//constants
	final static int FWIDTH = 1000;
	final static int FHEIGHT = 900;	

	static GamePanel currentPanel = new GamePanel ();

	//frame constructor
	public GameFrame(){		

		JFrame frame = new JFrame("Go For it: Connect 4");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(FWIDTH, FHEIGHT);
		Container content = frame.getContentPane();
		content.setBackground(Color.white);
		content.setLayout(new FlowLayout());
		content.add(new JLabel("Connect 4"));

		//add the Components into the JFrame
		frame.add(currentPanel);
		frame.add(new ControlPanel());

		//launch the frame
		frame.setVisible(true);

	}	

	//private listener class
	private class GameListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			currentPanel.compTurn();
			currentPanel.repaint();
		}
	}

	//methods that the buttons call:


	public static void moveNext(){
		currentPanel.compTurn();
		currentPanel.repaint();
	}

	public static void reset(){
		currentPanel.reset();
		currentPanel.repaint();
	}
	public static void undo(){
		currentPanel.undo();
		currentPanel.repaint();
	}
}