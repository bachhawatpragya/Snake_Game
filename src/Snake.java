import javax.swing.*;

public class Snake {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JFrame frame=new JFrame("Snake");
		frame.setVisible(true);
		frame.setSize(600, 600);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SnakeGamepanel snakegame= new SnakeGamepanel(600,600);
		frame.add(snakegame);
		frame.pack();
		snakegame.requestFocus();
	}

}
