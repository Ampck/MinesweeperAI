import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;

public class Window {

	public static void main(String[] args) {

		int winX = 500;
		int winY = 500;

		JFrame obj = new JFrame();
		Main game = new Main(winX, winY);
		game.setPreferredSize(new Dimension(winX, winY));
		obj.setResizable(false);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.add(game);
		obj.pack();
		obj.setVisible(true);

	}

}
