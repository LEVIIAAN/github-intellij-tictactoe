import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = new Color(34, 40, 49);
    public static final Color COLOR_BG_STATUS = new Color(48, 56, 65);
    public static final Color TEXT_COLOR = new Color(234, 232, 232);

    public static final Font FONT_STATUS = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_SCORE = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_SCORE_LABEL = new Font("Segoe UI", Font.PLAIN, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;

    private int scoreX = 0;
    private int scoreO = 0;
    private JPanel scorePanel;
    private JLabel labelScoreX, labelScoreO;

    // Variabel untuk kontrol volume
    private JSlider volumeSlider;
    private JLabel volumeLabel;

    public GameMain() {
        // Inisialisasi sound effect
        SoundEffect.initGame();

        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        // Status bar
        statusBar = new JLabel("", SwingConstants.CENTER);
        statusBar.setFont(FONT_STATUS);
        statusBar.setForeground(TEXT_COLOR);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        add(statusBar, BorderLayout.SOUTH);

        // Scoreboard
        scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setBackground(new Color(44, 52, 60));
        scorePanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        labelScoreX = new JLabel("X: 0", SwingConstants.CENTER);
        labelScoreX.setFont(FONT_SCORE);
        labelScoreX.setForeground(TEXT_COLOR);
        labelScoreO = new JLabel("O: 0", SwingConstants.CENTER);
        labelScoreO.setFont(FONT_SCORE);
        labelScoreO.setForeground(TEXT_COLOR);

        scorePanel.add(labelScoreX);
        scorePanel.add(labelScoreO);
        add(scorePanel, BorderLayout.EAST);

        // Reset Score Button
        JButton resetButton = new JButton("Reset Score");
        resetButton.setFont(FONT_SCORE_LABEL);
        resetButton.setForeground(TEXT_COLOR);
        resetButton.setBackground(COLOR_BG_STATUS);
        resetButton.addActionListener(e -> resetScore());
        scorePanel.add(resetButton);



        // Mouse listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {

                        SoundEffect.EAT_FOOD.play(); // Play sound ketika jalan main

                        currentState = board.stepGame(currentPlayer, row, col);

                        if (currentState == State.CROSS_WON) {
                            scoreX++;
                            updateScore();
                            SoundEffect.EXPLODE.play();
                        } else if (currentState == State.NOUGHT_WON) {
                            scoreO++;
                            updateScore();
                            SoundEffect.EXPLODE.play();
                        }

                        if (currentState == State.PLAYING) {
                            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                        }
                    }
                } else {
                    newGame();
                }
                repaint();
            }
        });

        setPreferredSize(new Dimension(Board.CANVAS_WIDTH + 200, Board.CANVAS_HEIGHT + 60));
        initGame();
        newGame();
    }


    private void updateScore() {
        labelScoreX.setText("X: " + scoreX);
        labelScoreO.setText("O: " + scoreO);
    }

    public void initGame() {
        board = new Board();
    }

    public void newGame() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED;
            }
        }
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setContentPane(new GameMain());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private void resetScore() {
        scoreX = 0;
        scoreO = 0;
        updateScore();  // Update scores
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);

        board.paint(g);

        // Print status-bar message
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
            SoundEffect.DIE.play();
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'X' Won! Click to play again.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'O' Won! Click to play again.");
        }
    }
}

