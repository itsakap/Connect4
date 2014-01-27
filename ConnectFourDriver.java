//A driver to display the JFrame

import javax.swing.*;

public class ConnectFourDriver extends JFrame{

	static JFrame frame;

	public static void main(String[] args) {
		frame = new GameFrame();
		frame.pack();
	}

}