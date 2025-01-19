package GUI;
import java.awt.*;
import javax.swing.*;

import API.DatabaseHandler;
import logic.MatchCard;
import utils.AudioPlayer;

public class HomePage extends JPanel {
    private JTextField nameField;
    private JButton startButton, leaderboardButton, exitButton;
    private DatabaseHandler dbHandler;
    private String playerName;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Image backgroundImage;

    public HomePage(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        AudioPlayer soundEffect = new AudioPlayer();

        dbHandler = new DatabaseHandler();


        backgroundImage = new ImageIcon("Assets/img/background_home.jpg").getImage();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));

        JLabel nameGameLabel = new JLabel(new ImageIcon("Assets/img/logo_mmc.png"));
        nameGameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        

        JLabel welcomeLabel = new JLabel("Masukkan Nickname");
        welcomeLabel.setFont(new Font("poppins", Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setForeground(Color.white);

        nameField = new JTextField(15);
        nameField.setFont(new Font("Poppins", Font.PLAIN, 14));
        nameField.setMaximumSize(new Dimension(150, 40));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.setHorizontalAlignment(JTextField.CENTER);

        startButton = new JButton(new ImageIcon("Assets/img/start_button.png"));
        startButton.setContentAreaFilled(false); 
        startButton.setBorderPainted(false);     
        startButton.setFocusPainted(false);    
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
      
        startButton.addActionListener(e -> {
            // soundEffect.playSoundEffect("Assets/sound/tap.wav");  
            System.out.println("Start button clicked!");
        });

        leaderboardButton = new JButton(new ImageIcon("Assets/img/leaderboard_button.png"));
        leaderboardButton.setContentAreaFilled(false); 
        leaderboardButton.setBorderPainted(false);    
        leaderboardButton.setFocusPainted(false);      
        leaderboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);

    
        leaderboardButton.addActionListener(e -> {
            soundEffect.playSoundEffect("Assets/sound/tap.wav");  
            System.out.println("Leaderboard button clicked!");
        });

        exitButton = new JButton(new ImageIcon("Assets/img/exit_button.png"));
        exitButton.setContentAreaFilled(false); 
        exitButton.setBorderPainted(false);     
        exitButton.setFocusPainted(false);    
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);


        exitButton.addActionListener(e -> {
            soundEffect.playSoundEffect("Assets/sound/tap.wav");  
            System.exit(0); 
        });


        startButton.addActionListener(e -> {
            soundEffect.playSoundEffect("Assets/sound/tap.wav");  
            playerName = nameField.getText().trim();
            if (!playerName.isEmpty()) {
                if (dbHandler.checkIfUserExists(playerName)) {
                    JOptionPane.showMessageDialog(null, "Welcome back, " + playerName + "! Starting the game...");
                } else {
                    dbHandler.updateScore(playerName, 0);
                    JOptionPane.showMessageDialog(null, "Welcome, " + playerName + "! Starting the game...");
                }
                MatchCard matchCard = new MatchCard(playerName, cardLayout, mainPanel);
                mainPanel.add(matchCard, "MatchCard");
                System.out.println("MatchCard added to mainPanel");
                cardLayout.show(mainPanel, "MatchCard");
                System.out.println("CardLayout showing MatchCard");
                mainPanel.revalidate();
                mainPanel.repaint();
                System.out.println("mainPanel revalidated and repainted");
            } else {
                JOptionPane.showMessageDialog(null, "Please enter your name!");
            }
        });
        leaderboardButton.addActionListener(e -> new LeaderboardPage(cardLayout, mainPanel));

        exitButton.addActionListener(e -> System.exit(0));

        add(nameGameLabel);
        add(Box.createVerticalStrut(20));
        add(welcomeLabel);
        add(Box.createVerticalStrut(20));
        add(nameField);
        add(Box.createVerticalStrut(20));
        add(startButton);
        add(Box.createVerticalStrut(20));
        add(leaderboardButton);
        add(Box.createVerticalStrut(20));
        add(exitButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}