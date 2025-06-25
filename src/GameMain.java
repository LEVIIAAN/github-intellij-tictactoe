import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = new Color(240, 240, 240);
    public static final Color COLOR_BG_STATUS = new Color(200, 200, 200);
    public static final Font FONT_STATUS = new Font("Arial", Font.PLAIN, 16);
    public static final Font FONT_SCORE = new Font("Arial", Font.BOLD, 24);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;

    private int scoreX = 0;
    private int scoreO = 0;
    private JLabel labelScoreX;
    private JLabel labelScoreO;

    public GameMain() {
        setLayout(new BorderLayout());
        SoundEffect.initGame();
        // Mouse input
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
                        currentState = board.stepGame(currentPlayer, row, col);

                        if (currentState == State.CROSS_WON) {
                            scoreX++;
                            updateScore();
                        } else if (currentState == State.NOUGHT_WON) {
                            scoreO++;
                            updateScore();
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

        // Status bar
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setOpaque(true);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusBar, BorderLayout.SOUTH);

        // Scoreboard
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setBackground(new Color(255, 255, 255));
        scorePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        labelScoreX = new JLabel("X: 0");
        labelScoreO = new JLabel("O: 0");
        labelScoreX.setFont(FONT_SCORE);
        labelScoreO.setFont(FONT_SCORE);
        labelScoreX.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelScoreO.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton resetButton = new JButton("Reset Score");
        resetButton.setFont(FONT_STATUS);
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.setBackground(new Color(100, 149, 237));
        resetButton.setForeground(Color.WHITE);
        resetButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        resetButton.addActionListener(e -> {
            scoreX = 0;
            scoreO = 0;
            updateScore();
        });

        scorePanel.add(labelScoreX);
        scorePanel.add(Box.createVerticalStrut(10));
        scorePanel.add(labelScoreO);
        scorePanel.add(Box.createVerticalStrut(20));
        scorePanel.add(resetButton);

        add(scorePanel, BorderLayout.EAST);

        setPreferredSize(new Dimension(Board.CANVAS_WIDTH + 160, Board.CANVAS_HEIGHT + 30));
        setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);
        board.paint(g);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'X' Won! Click to play again.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'O' Won! Click to play again.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setContentPane(new GameMain());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
