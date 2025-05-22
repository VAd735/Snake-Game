
package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGameNew {

    public static class snakeGameNew extends JPanel implements ActionListener {

        private final int CELL_SIZE = 25;   // size of each cell is 25 pixels
        private final int WIDTH = 600;   // field width = 600 pixels
        private final int HEIGHT = 600;   // field height = 600 pixels
        private final int NUM_CELLS_X = WIDTH / CELL_SIZE;
        private final int NUM_CELLS_Y = HEIGHT / CELL_SIZE;

        private final LinkedList<Point> snake;
        private Point food;
        private boolean gameOver;
        private boolean paused;
        private int direction;  // 0 = up, 1 = right, 2 = down, 3 = left
        private final Timer timer;

        public snakeGameNew() {
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setBackground(Color.BLACK);
            snake = new LinkedList<>();
            resetGame();

            // Timer for game updates (20 ms = 50 FPS)
            timer = new Timer(200, this); // snake speed
            timer.start(); // starts immediately on startup

            // Key listener for controlling the snake
            addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_UP && direction != 2) {
                        direction = 0;   // up
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && direction != 3) {
                        direction = 1;   // right
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN && direction != 0) {
                        direction = 2;   // down
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT && direction != 1) {
                        direction = 3;   // left
                    } else if (e.getKeyCode() == KeyEvent.VK_P) {  // pause or start the game
                        paused = !paused;
                        if (!paused) {   // pause
                            timer.start();
                        } else {   // stop the game
                            timer.stop();
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
                        resetGame();   // restart the game
                    }
                }
            });
            setFocusable(true);
        }

        public void resetGame() {   // start of the game
            snake.clear();
            direction = 2;   // Initial movement down
            // Initial snake length
            int INITIAL_SNAKE_LENGTH = 4;  // snake length
            for (int i = INITIAL_SNAKE_LENGTH - 2; i >= 0; i--) {
                snake.add(new Point(i, 0));
            }
            spawnFood();
            gameOver = false;
            paused = false;
        }

        public void spawnFood() {     // random food generation
            Random rand = new Random();

            // condition to ensure food doesn't spawn on the snake
            do {
                food = new Point(rand.nextInt(NUM_CELLS_X), rand.nextInt(NUM_CELLS_Y));
            } while (snake.contains(food));
        }

        public void actionPerformed(ActionEvent e) {
            if (paused || gameOver) return; // start game after pause or restart

            // Move the snake
            Point newHead = getPoint();
            if ( newHead.x < 0 || newHead.x >= NUM_CELLS_X || newHead.y < 0 || newHead.y >= NUM_CELLS_Y) {
                gameOver = true;
                repaint();
                return;
            }

            // Check for collision with itself
            if (snake.contains(newHead)) {
                gameOver = true;
                repaint();
                return;
            }

            // Add new head to the snake
            snake.addFirst(newHead);

            // Check if the snake eats the food
            if (newHead.equals(food)) {
                spawnFood(); // generate new food
            } else {
                snake.removeLast(); // remove the tail
            }
            repaint();
        }

        private Point getPoint() {
            Point head = snake.getFirst();
            Point newHead = switch (direction) {  // control conditions for the snake
                case 0 -> new Point(head.x, head.y - 1);   // up

                case 1 -> new Point(head.x + 1, head.y);   // right

                case 2 -> new Point(head.x, head.y + 1);   // down

                case 3 -> new Point(head.x - 1, head.y);
                default -> null;   // left
            };

            // Check for collision with the walls
            assert newHead != null;
            return newHead;
        }

        // Drawing the game process
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (gameOver) {   // check after game over
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 40));
                g.drawString("Game Over!", WIDTH / 4, HEIGHT / 2);
                g.drawString("Press 'R' to Restart", WIDTH / 4, HEIGHT / 2 + 50);
                return;
            }

            // Drawing the snake
            g.setColor(Color.GREEN);
            for (Point p : snake) {
                g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }

            // Drawing the food
            g.setColor(Color.RED);
            g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE); // drawing the rectangle on the screen

            // Display the message when the game is paused
            if (paused) {
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString("PAUSED", WIDTH / 3, HEIGHT / 2);
            }
        }

        public static void main(String[] args) {    // creating the window with graphical interface for the game
            JFrame frame = new JFrame("Snake_Game");
            snakeGameNew game = new snakeGameNew();
            frame.add(game);
            frame.pack();      // sets the window size
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // window close operation when exiting the game
            frame.setVisible(true);
        }

    }
}



