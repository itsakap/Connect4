//ControlPanel.java
//File for adding buttons to Panel
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ControlPanel extends JPanel{

	private final int WIDTH = 800;
	private final int HEIGHT = 450;
	private JPanel panel = this;

	//the various buttons
	private JButton next, reset, undo;
	
	//constuctor; appearance of panel
	public ControlPanel(){
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setBackground(Color.white);

		ActionListener listener = new ControlListener();

		next = new JButton("Submit");
		next.addActionListener(listener);
		
		reset = new JButton("Reset Game");
		reset.addActionListener(listener);
		
		undo = new JButton("Undo Move");
		undo.addActionListener(listener);
		
		panel.add(next);
		panel.add(reset);
		panel.add(undo);
	}
	
	private class ControlListener implements ActionListener {
		//do the correct action for each button
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();

			if (source == next){
				GameFrame.moveNext();
			}

			if (source == reset) {
				GameFrame.reset();
			}
			if(source==undo){
				GameFrame.undo();
			}
		}
	}
}