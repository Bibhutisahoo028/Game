import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BlockBreakerGame extends JPanel implements KeyListener, ActionListener {
    
    // Define the game elements
    private int paddleX = 300;
    private final int paddleWidth = 100;
    private final int paddleHeight = 15;
    private int ballX = 350;
    private int ballY = 250;
    private int ballDirectionX = -1;
    private int ballDirectionY = -2;
    private final int ballRadius = 10;
    private Timer timer;

    // Blocks
    private final int blockRows = 5;
    private final int blockColumns = 7;
    private final int blockWidth = 60;
    private final int blockHeight = 20;
    private boolean[][] blocks = new boolean[blockRows][blockColumns];

    public BlockBreakerGame() {
        // Set up the panel and timer
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        addKeyListener(this);
        
        // Initialize blocks
        for (int i = 0; i < blockRows; i++) {
            for (int j = 0; j < blockColumns; j++) {
                blocks[i][j] = true; // All blocks are visible initially
            }
        }

        // Timer for game updates
        timer = new Timer(5, this);  // Update every 5 ms
        timer.start();
    }

    // Draw the game elements
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw paddle
        g.setColor(Color.BLUE);
        g.fillRect(paddleX, getHeight() - paddleHeight - 30, paddleWidth, paddleHeight);

        // Draw ball
        g.setColor(Color.RED);
        g.fillOval(ballX, ballY, ballRadius * 2, ballRadius * 2);

        // Draw blocks
        g.setColor(Color.GREEN);
        for (int i = 0; i < blockRows; i++) {
            for (int j = 0; j < blockColumns; j++) {
                if (blocks[i][j]) {
                    g.fillRect(j * (blockWidth + 10) + 50, i * (blockHeight + 10) + 50, blockWidth, blockHeight);
                }
            }
        }
    }

    // Handle key press events
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && paddleX > 0) {
            paddleX -= 20; // Move paddle left
        }
        if (key == KeyEvent.VK_RIGHT && paddleX < getWidth() - paddleWidth) {
            paddleX += 20; // Move paddle right
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    // Game update (called every timer tick)
    @Override
    public void actionPerformed(ActionEvent e) {
        // Move the ball
        ballX += ballDirectionX;
        ballY += ballDirectionY;

        // Ball collision with walls
        if (ballX <= 0 || ballX >= getWidth() - ballRadius * 2) {
            ballDirectionX = -ballDirectionX; // Reverse horizontal direction
        }
        if (ballY <= 0) {
            ballDirectionY = -ballDirectionY; // Reverse vertical direction (top wall)
        }
        if (ballY >= getHeight() - ballRadius * 2 - paddleHeight - 30) {
            // Ball hits the paddle
            if (ballX + ballRadius > paddleX && ballX + ballRadius < paddleX + paddleWidth) {
                ballDirectionY = -ballDirectionY; // Reverse direction when hitting paddle
            } else if (ballY >= getHeight()) {
                // Game over
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over!");
                System.exit(0);
            }
        }

        // Ball collision with blocks
        for (int i = 0; i < blockRows; i++) {
            for (int j = 0; j < blockColumns; j++) {
                if (blocks[i][j]) {
                    int blockX = j * (blockWidth + 10) + 50;
                    int blockY = i * (blockHeight + 10) + 50;
                    if (ballX + ballRadius > blockX && ballX + ballRadius < blockX + blockWidth &&
                        ballY + ballRadius > blockY && ballY + ballRadius < blockY + blockHeight) {
                        // Ball hits block
                        blocks[i][j] = false; // Remove block
                        ballDirectionY = -ballDirectionY; // Reverse direction
                        break;
                    }
                }
            }
        }

        // Repaint the screen
        repaint();
    }

    // Main method to set up the game window
    public static void main(String[] args) {
        JFrame frame = new JFrame("Block Breaker Game");
        BlockBreakerGame game = new BlockBreakerGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        frame.pack();
        frame.setVisible(true);
    }
}
