package logic;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;

import API.DatabaseHandler;
import model.Card;
import utils.AudioPlayer;

public class MatchCard extends JPanel {
    private String playerName;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    String[] cardList = {
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j"
    }; 

    int rows = 4;  
    int cols = 6; 
    int cardWidth = 90; 
    int cardHeight = 128; 

    ArrayList<Card> cardSet;  
    ImageIcon cardBackImageIcon;

    int boardWidth = cols * cardWidth; 
    int boardHeight = rows * cardHeight; 

    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();  
    JButton restartButton = new JButton(); 

    int errorCount = 0;
    int score = 0; // Initialize score to 0
    ArrayList<JButton> board;
    Timer hideCardTimer; 
    boolean gameReady = false; 
    JButton card1Selected; 
    JButton card2Selected;

    int matchedPairs = 0;   
    Timer reviewCardsTimer; 

    JLabel timerLabel = new JLabel(); 
    JLabel playerLabel = new JLabel();
    JLabel scoreLabel = new JLabel(); // Label to display score

    Timer gameTimer; 

    int initialTime = 25; 
    int timeLeft = initialTime;  
    int minutes = timeLeft / 60;  // Menghitung menit
    int seconds = timeLeft % 60;  // Menghitung detik
    
    private DatabaseHandler dbHandler;

    String globalPlayerName;
    
    public MatchCard(String playerName, CardLayout cardLayout, JPanel mainPanel) {
        this.playerName = playerName;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(new BorderLayout());
        JLabel label = new JLabel("MatchCard Game for " + playerName);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);

        System.out.println("MatchCard initialized for player: " + playerName);

        // Additional debug statement
        System.out.println("MatchCard panel created and added to mainPanel");

        AudioPlayer audioPlayer = new AudioPlayer();
        audioPlayer.playBackgroundMusic("assets/sound/bg_music.wav");

        AudioPlayer soundEffect = new AudioPlayer();

        setupCards();
        shuffleCards();

        dbHandler = new DatabaseHandler();

        globalPlayerName = playerName;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(boardWidth, boardHeight));

        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        
        playerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playerLabel.setHorizontalAlignment(JLabel.CENTER);
        playerLabel.setText(playerName);
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        timerLabel.setText(String.format("Waktu Tersisa: %02d:%02d", minutes, seconds));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // textLabel.setFont(new Font("Arial", Font.BOLD, 20));
        // textLabel.setHorizontalAlignment(JLabel.CENTER);
        // textLabel.setText("Salah: " + Integer.toString(errorCount));
        // textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font for score label
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setText("Skor: " + score); // Initialize score label
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        textPanel.add(playerLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(timerLabel);
        // textPanel.add(Box.createVerticalStrut(10));
        // textPanel.add(textLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(scoreLabel); // Add score label to text panel
        
        JPanel gamePanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("Assets/img/background.jpg");
                backgroundImage = new ImageIcon(backgroundImage.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH));
                Image image = backgroundImage.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }  
        };
        
        textPanel.setOpaque(false);
        gamePanel.add(textPanel, BorderLayout.NORTH);
                
        board = new ArrayList<JButton>();

        boardPanel.setLayout(new GridLayout(rows, cols, 0, 0));  
        boardPanel.setOpaque(false);

        gamePanel.setBorder(BorderFactory.createEmptyBorder(50,500,70,500));

        for (int i = 0; i < cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(false);
            tile.setContentAreaFilled(false);
            tile.setBorder(null);
            tile.setIcon(cardSet.get(i).getCardImageIcon());
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!gameReady) {
                        return;
                    }

                    JButton tile = (JButton) e.getSource();
                    if (tile.getIcon() == cardBackImageIcon) {
                        if (card1Selected == null) {
                            soundEffect.playSoundEffect("Assets/sound/tap.wav");  
                            card1Selected = tile;
                            int index = board.indexOf(card1Selected);
                            card1Selected.setIcon(cardSet.get(index).getCardImageIcon());
                        } else if (card2Selected == null) {  
                            soundEffect.playSoundEffect("Assets/sound/tap.wav");  
                            card2Selected = tile;
                            int index = board.indexOf(card2Selected);
                            card2Selected.setIcon(cardSet.get(index).getCardImageIcon());

                            if (card1Selected.getIcon() != card2Selected.getIcon()) {
                                soundEffect.playSoundEffect("Assets/sound/false.wav");
                                errorCount += 1;
                                score = Math.max(0, score - 2); // Pengurangan point sebanyak 2 kecuali score 0
                                int minutes = timeLeft / 60;  // Menghitung menit
                                int seconds = timeLeft % 60;  // Menghitung detik
                                timerLabel.setText(String.format("Waktu Tersisa: %02d:%02d", minutes, seconds)); // Update timer label
                                textLabel.setText("Salah: " + Integer.toString(errorCount));
                                scoreLabel.setText("Skor: " + score); // Update score label
                                hideCardTimer.start();  
                            } else {
                                soundEffect.playSoundEffect("Assets/sound/true.wav");
                                matchedPairs += 1;  
                                score += 10; // [enambahan point kalo bener]
                                timeLeft += 5; // penambahan waktu kalau bener juga
                                scoreLabel.setText("Skor: " + score); // Update score label
                                int minutes = timeLeft / 60;  // Menghitung menit
                                int seconds = timeLeft % 60;  // Menghitung detik
                                timerLabel.setText(String.format("Waktu Tersisa: %02d:%02d +5 ", minutes, seconds)); // Update timer label
                                card1Selected = null;
                                card2Selected = null;

                                if (matchedPairs == (cardSet.size() / 2)) {
                                    gameReady = false;  
                                    gameTimer.stop();
                                    int reply = JOptionPane.showConfirmDialog(null, "Semua kartu sudah dicocokkan! Ingin bermain lagi?", 
                                                                              "Permainan Selesai", JOptionPane.YES_NO_OPTION);
                                    if (reply == JOptionPane.YES_OPTION) {
                                        restartGame();
                                        updateScore();
                                    } else {
                                        audioPlayer.stopBackgroundMusic();
                                        updateScore();
                                        cardLayout.show(mainPanel, "HomePage");
                                    }
                                }
                            }
                        }
                    }
                }
            });
            board.add(tile);
            boardPanel.add(tile);
        }
        gamePanel.add(boardPanel);

        JButton restartButton = new JButton();
        restartButton.setContentAreaFilled(false); 
        restartButton.setBorderPainted(false); 
        restartButton.setFocusPainted(false); 

       
        Image restartImg = new ImageIcon(new File("Assets/img/restart_button.png").getAbsolutePath()).getImage();
        ImageIcon restartIcon = new ImageIcon(restartImg.getScaledInstance(210, 70, Image.SCALE_SMOOTH));
        restartButton.setIcon(restartIcon);

        
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameReady) {
                    return;
                }

                restartGame();
                hideCardTimer.start();
            }
        });

        restartGamePanel.add(restartButton);
        restartGamePanel.setOpaque(false);
        gamePanel.add(restartGamePanel, BorderLayout.SOUTH);

        add(gamePanel, BorderLayout.CENTER);

        hideCardTimer = new Timer(1050, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideCards();
            }
        });
        hideCardTimer.setRepeats(false);
        hideCardTimer.start();

        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    int minutes = timeLeft / 60;  // Menghitung menit
                    int seconds = timeLeft % 60;  // Menghitung detik
                    timerLabel.setText(String.format("Waktu Tersisa: %02d:%02d", minutes, seconds)); // Update timer label
                } else {
                    gameTimer.stop();  
                    gameReady = false;  
                    int reply = JOptionPane.showConfirmDialog(null, "Waktu Telah Habis, Ingin bermain lagi?", 
                                                                "Permainan Selesai", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        restartGame();
                        updateScore();  
                    } else {
                        audioPlayer.stopBackgroundMusic();
                        updateScore();
                        cardLayout.show(mainPanel, "HomePage");
                    }
                }
            }
        });
        gameTimer.start();  
    }

    void setupCards() {
        cardSet = new ArrayList<Card>();
        for (String cardName : cardList) {
            File cardFile = new File("Assets/img/" + cardName + ".jpg");
            if (cardFile.exists()) {
                Image cardImg = new ImageIcon(cardFile.getAbsolutePath()).getImage();
                ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
                Card card = new Card(cardName, cardImageIcon);
                cardSet.add(card);
            } else {
                System.err.println("Error: Image file not found for card: " + cardName);
            }
        }

        cardSet.addAll(cardSet);

        File cardBackFile = new File("Assets/img/back.jpg");
        
        if (cardBackFile.exists()) {
            Image cardBackImg = new ImageIcon(cardBackFile.getAbsolutePath()).getImage();
            cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
        } else {
            System.err.println("Error: Image file not found for card back");
        }
    }

    void shuffleCards() {
        for (int i = 0; i < cardSet.size(); i++) {
            int j = (int) (Math.random() * cardSet.size());
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
    }

    void hideCards() {
        if (gameReady && card1Selected != null && card2Selected != null) {
            card1Selected.setIcon(cardBackImageIcon);
            card1Selected = null;  
            card2Selected.setIcon(cardBackImageIcon);  
            card2Selected = null;  
        } else {
            for (int i = 0; i < board.size(); i++) {
                board.get(i).setIcon(cardBackImageIcon);  
            }
            gameReady = true; 
            restartButton.setEnabled(true);  
        }
    }

    void restartGame() {
        matchedPairs = 0;  
        errorCount = 0;    
        score = 0; // Reset score to 0
        textLabel.setText("Salah: " + Integer.toString(errorCount));  
        scoreLabel.setText("Skor: " + score); // Reset score label
        setupCards();
        shuffleCards();  
        gameReady = false;  
    
        timeLeft = initialTime;  
        int minutes = timeLeft / 60;  // Menghitung menit
        int seconds = timeLeft % 60;  // Menghitung detik
        timerLabel.setText(String.format("Waktu Tersisa: %02d:%02d", minutes, seconds));  
        gameTimer.restart();  
    
        for (int i = 0; i < board.size(); i++) {
            board.get(i).setIcon(cardSet.get(i).getCardImageIcon());
        }
    
        reviewCardsTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideCardTimer.start();  
            }
        });

        reviewCardsTimer.setRepeats(false);
        reviewCardsTimer.start();
    }
    
    void updateScore() {
        // score = 
        int currentHighScore = dbHandler.getScore(globalPlayerName); // Mendapatkan skor saat ini dari database
    
        // if (score > currentHighScore) { // Hanya memperbarui jika skor baru lebih tinggi
        //     dbHandler.updateScore(globalPlayerName, score);
        // }
    
        if (dbHandler != null) {
            if (score > currentHighScore) {
                dbHandler.updateScore(globalPlayerName, score);
            }
        } else {
            System.out.println("Error: DatabaseHandler is not initialized.");
        }
    }
}
