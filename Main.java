import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.lang.Math;

public class Main extends JPanel implements KeyListener {

    int winX = 0;
    int winY = 0;

    int marginX = 30;
    int marginY = 30;

    int bombsX, bombsY, bombs, flaggedBombs;

    boolean loss = false;

    int boardW = 10;
    int boardH = 10;
    int bombAmount = 10;

    int brickBorderWidth = 2;
    Color brickShade = new Color(60, 60, 60);
    Color brickBorderShade = new Color(90, 90, 90);
    Color brickShadeNumber = new Color(75, 75, 75);
    Color brickShadeBomb = new Color (200, 30, 30);

    int[][] board;
    int[][] playerBoard;
    int[][] clearedBoard;
    int[][] flaggedBoard;

    public Main(int x, int y) {

        winX = x;
        winY = y;

        generateBoard(boardW, boardH, bombAmount);

        //System.out.println("Hello World");

        setFocusable(true);
		setFocusTraversalKeysEnabled(true);

        addKeyListener(this);

    }

    public void generateBoard(int w, int h, int bombCount) {

        bombs = bombCount;

        if (bombs > w * h) {

            bombs = (w * h) - 1;

        }

        int[][] newBoard = new int[w][h];

        int[][] bombCoords = new int[bombs][2];

        Random random = new Random();

        System.out.println();

        for (int a = 0; a < bombs; a++) {

            bombCoords[a][0] = Math.abs(random.nextInt()) % w;
            bombCoords[a][1] = Math.abs(random.nextInt()) % h;

            System.out.println("{" + bombCoords[a][0] + "," + bombCoords[a][1] + "}");

        }

        System.out.println();

        for (int a = 0; a < bombs; a++) {

            boolean bombDuplicates = false;

            for (int b = 0; b < bombs; b++) {

                if ((bombCoords[a][0] == bombCoords[b][0] && bombCoords[a][1] == bombCoords[b][1]) && a != b) {

                    bombDuplicates = true;

                    System.out.println("Old Coord: {" + bombCoords[a][0] + "," + bombCoords[a][1] + "}");

                    break;

                }

            }

            while (bombDuplicates == true) {

                bombCoords[a][0] = Math.abs(random.nextInt()) % w;
                bombCoords[a][1] = Math.abs(random.nextInt()) % h;

                bombDuplicates = false;

                System.out.println("New Coord: {" + bombCoords[a][0] + "," + bombCoords[a][1] + "}");

                for (int b = 0; b < bombs; b++) {

                    if ((bombCoords[a][0] == bombCoords[b][0] && bombCoords[a][1] == bombCoords[b][1]) && a != b) {

                        bombDuplicates = true;

                        System.out.println("Old Coord: {" + bombCoords[a][0] + "," + bombCoords[a][1] + "}");

                        break;

                    }

                }

            }

        }

        System.out.println("\nNew Coords");

        for (int i = 0; i < bombs; i++) {

            System.out.println("{" + bombCoords[i][0] + "," + bombCoords[i][1] + "}");

        }

        bombsX = w;
        bombsY = h;

        for (int i = bombs; i < bombs; i++) {

            newBoard[bombCoords[i][0]][bombCoords[i][1]] = 10;

        }

        for (int a = 0; a < h; a++) {

            for (int b = 0; b < w; b++) {

                for (int i = 0; i < bombs; i++) {

                    if (b == bombCoords[i][0] && a == bombCoords[i][1]) {

                            newBoard[b][a] = 9;

                            break;

                    }

                }

            }

        }

        int surroundingBombs = 0;

        for (int a = 0; a < h; a++) {

            for (int b = 0; b < w; b++) {

                surroundingBombs = 0;

                if (newBoard[b][a] != 9) {

                    for (int c = -1; c < 2; c++) {

                        for (int d = -1; d < 2; d++) {

                            if (b + c >= 0 && a + d >= 0 && b + c < w && a + d < h) {

                                if (newBoard[b + c][a + d] == 9) {

                                    surroundingBombs++;

                                }

                            }

                        }

                    }

                    newBoard[b][a] = surroundingBombs;

                }

                System.out.print(newBoard[b][a] + "   ");

            }

            System.out.print("\n\n");

        }

        board = newBoard;
        playerBoard = new int[w][h];
        clearedBoard = new int[w][h];
        flaggedBoard = new int[w][h];
        flaggedBombs = 0;

        loss = false;

        for (int a = 0; a < w; a++) {

            for (int b = 0; b < h; b++) {

                playerBoard[a][b] = -1;

            }

        }

    }

    public void makeMove() {

        System.out.println("\nMaking Move...");

        boolean moveMade = false;

        int surroundingBombs = 0;

        for (int a = 0; a < bombsY; a++) {

            for (int b = 0; b < bombsX; b++) {

                if (clearedBoard[b][a] == 0) {

                    surroundingBombs = 0;

                    for (int c = -1; c < 2; c++) {

                        for (int d = -1; d < 2; d++) {

                            if (b + c >= 0 && a + d >= 0 && b + c < bombsX && a + d < bombsY) {

                                //if (playerBoard[b + c][a + d] == 9) {

                                if (flaggedBoard[b + c][a + d] == 1) {

                                    surroundingBombs++;

                                }

                            }

                        }

                    }

                    if (playerBoard[b][a] == surroundingBombs) {

                        for (int c = -1; c < 2; c++) {

                            for (int d = -1; d < 2; d++) {

                                if (b + c >= 0 && a + d >= 0 && b + c < bombsX && a + d < bombsY) {

                                    if (flaggedBoard[b + c][a + d] == 0) {

                                        playerBoard[b + c][a + d] = board[b + c][a + d];

                                    }

                                }

                            }

                        }

                        clearedBoard[b][a] = 1;

                        System.out.println("Cleared Square: {" + b + "," + a + "}");

                        moveMade = true;

                    }

                }

            }

        }

        boolean moveMade2 = false;

        /*

        if (!moveMade) {

            int surroundingNumbers = 0;

            for (int a = 0; a < bombsY; a++) {

                for (int b = 0; b < bombsX; b++) {

                    if (playerBoard[b][a] >= 0 && playerBoard[b][a] <= 8) {

                        surroundingNumbers = 0;

                        for (int c = -1; c < 2; c++) {

                            for (int d = -1; d < 2; d++) {

                                if (b + c >= 0 && a + d >= 0 && b + c < bombsX && a + d < bombsY) {

                                    if ((playerBoard[b + c][a + d] >= 0 && playerBoard[b + c][a + d] <= 8)) {

                                        surroundingNumbers++;

                                    }



                                    else {

                                        nonNumbers[iteration][0] = c;
                                        nonNumbers[iteration][1] = d;

                                        iteration++;

                                    }



                                }

                                else {

                                    surroundingNumbers++;

                                }

                            }

                        }

                        if (playerBoard[b][a] == 8 - surroundingNumbers && clearedBoard[b][a] == 0) {

                            for (int c = -1; c < 2; c++) {

                                for (int d = -1; d < 2; d++) {

                                    if (b + c >= 0 && a + d >= 0 && b + c < bombsX && a + d < bombsY) {

                                        if (!(playerBoard[b + c][a + d] >= 0 && playerBoard[b + c][a + d] <= 8) && flaggedBoard[b + c][a + d] == 0) {

                                            flaggedBoard[b + c][a + d] = 1;

                                        }

                                    }

                                }

                            }

                            clearedBoard[b][a] = 1;

                            System.out.println("Flagged Bombs Around Square: {" + a + "," + b + "}");

                            moveMade2 = true;

                        }

                    }

                }

            }

        }

        */

        if (!moveMade) {

            int surroundingNumbers = 0;

            for (int a = 0; a < bombsY; a++) {

                for (int b = 0; b < bombsX; b++) {

                    if (playerBoard[b][a] >= 1 && playerBoard[b][a] <= 8) {

                        surroundingNumbers = 0;

                        for (int c = -1; c < 2; c++) {

                            for (int d = -1; d < 2; d++) {

                                if (b + c >= 0 && a + d >= 0 && b + c < bombsX && a + d < bombsY) {

                                    if (!(c == 0 && d == 0)) {

                                        if ((playerBoard[b + c][a + d] >= 0 && playerBoard[b + c][a + d] <= 8)) {

                                            surroundingNumbers++;

                                        }

                                    }

                                }

                                else {

                                    surroundingNumbers++;

                                }

                            }

                        }

                        System.out.println("Coord {" + b + "," + a + "} has " + surroundingNumbers + " numbers around it");
                        System.out.println("        " + playerBoard[b][a] + " | " + (8 - surroundingNumbers));
                        if (playerBoard[b][a] == (8 - surroundingNumbers) && clearedBoard[b][a] == 0) {

                            for (int c = -1; c < 2; c++) {

                                for (int d = -1; d < 2; d++) {

                                    if (b + c >= 0 && a + d >= 0 && b + c < bombsX && a + d < bombsY) {

                                        if (playerBoard[b + c][a + d] == -1) {

                                            if (flaggedBoard[b + c][a + d] == 0) {

                                                flaggedBoard[b + c][a + d] = 1;

                                                flaggedBombs++;

                                            }

                                            System.out.println("        Flagged Bomb {" + ( b + c) + "," + (a + d) + "}");

                                            moveMade2 = true;

                                        }

                                    }

                                }

                            }

                            clearedBoard[b][a] = 1;

                        }

                    }

                }

            }

        }

        if (!moveMade2 && !moveMade) {

            Random random = new Random();

            int x, y;

            do {

                x = Math.abs(random.nextInt()) % bombsX;
                y = Math.abs(random.nextInt()) % bombsY;

            }

            while(playerBoard[x][y] >= 0 || flaggedBoard[x][y] == 1);

            System.out.println("Selecting Random Coordinate: {" + x + "," + y + "}");

            playerBoard[x][y] = board[x][y];

        }

        for (int a = 0; a < bombsY; a++) {

            for (int b = 0; b < bombsX; b++) {

                if (playerBoard[b][a] == 9) {

                    loss = true;

                }

            }

        }

    }

    public void paint(Graphics g) {

        if (flaggedBombs >= bombs) {

            System.out.println(flaggedBombs + " | " + bombs);

            g.setColor(new Color(35, 35, 200));

        }

        else if (loss) {

            g.setColor(new Color(200, 35, 35));

        }

        else {

            g.setColor(new Color(35, 35, 35));

        }

        g.fillRect(0, 0, winX, winY);

        g.setColor(new Color(0, 255, 0));

        //g.fillRect(20, 20, 50, 50);

        for (int a = 0; a < bombsX; a++) {

            for (int b = 0; b < bombsY; b++) {

                int posA = (a * ((winX - (marginX * 2)) / bombsX)) + marginX;
                int posB = (b * ((winY - (marginY * 2)) / bombsY)) + marginY;

                int modA = (((winX - (marginX * 2)) / bombsX));
                int modB = (((winY - (marginY * 2)) / bombsY));

                g.setColor(brickBorderShade);
                g.fillRect(posA, posB, modA, modB);

                if (playerBoard[a][b] == 0) {

                    g.setColor(brickShadeNumber);
                    g.fillRect(posA + brickBorderWidth, posB + brickBorderWidth, modA - brickBorderWidth, modB - brickBorderWidth);

                }

                else if (playerBoard[a][b] == 9) {

                    g.setColor(brickShadeBomb);
                    g.fillRect(posA + brickBorderWidth, posB + brickBorderWidth, modA - brickBorderWidth, modB - brickBorderWidth);

                }

                else if (playerBoard[a][b] >= 0) {

                    g.setColor(brickShadeNumber);
                    g.fillRect(posA + brickBorderWidth, posB + brickBorderWidth, modA - brickBorderWidth, modB - brickBorderWidth);
                    g.setColor(new Color(0, 255, 255));
                    g.drawString(Integer.toString(playerBoard[a][b]), posA, posB + modB);

                }

                else {

                    g.setColor(brickShade);
                    g.fillRect(posA + brickBorderWidth, posB + brickBorderWidth, modA - brickBorderWidth, modB - brickBorderWidth);

                }

                if (flaggedBoard[a][b] == 1) {

                    g.setColor(new Color(255, 0, 0));
                    g.drawString("F", posA, posB + modB);

                }

                //g.fillRect(posA + brickBorderWidth, posB + brickBorderWidth, modA - brickBorderWidth, modB - brickBorderWidth);

                /*
                System.out.println();
                System.out.println("posA: " + posA);
                System.out.println("posB: " + posB);
                System.out.println("modA: " + modA);
                System.out.println("modB: " + modB);
                System.out.println();
                */

            }

        }

        repaint();

    }

    @Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

    @Override
	public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_X) {

			generateBoard(boardW, boardH, bombAmount);

		}

        if (e.getKeyCode() == KeyEvent.VK_C) {

            if (flaggedBombs < bombs && loss == false) {

                makeMove();

            }

		}

	}

    @Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
