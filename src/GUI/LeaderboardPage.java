package GUI;
import javax.swing.*;

import API.DatabaseHandler;
import model.User;
import utils.AudioPlayer;

import java.awt.*;
import java.util.ArrayList;

public class LeaderboardPage extends JPanel {
    private JFrame leaderboardFrame;
    private JPanel leaderboardPanel;
    private DatabaseHandler dbHandler;

    AudioPlayer soundEffect = new AudioPlayer();

    public LeaderboardPage(CardLayout cardLayout, JPanel mainPanel) {
        dbHandler = new DatabaseHandler(); // Initialize DatabaseHandler

        leaderboardFrame = new JFrame("Leaderboard");
        leaderboardFrame.setLayout(new BorderLayout());
        leaderboardFrame.setSize(350, 400);
        leaderboardFrame.setLocationRelativeTo(null);
        leaderboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.setBackground(new Color(245, 245, 245)); // Light background color
        leaderboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Panel padding

        // Judul Leaderboard
        JLabel leaderboardLabel = new JLabel("Leaderboard");
        leaderboardLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        leaderboardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardPanel.add(leaderboardLabel);

        // Add vertical space
        leaderboardPanel.add(Box.createVerticalStrut(20));

        // Pnael untuk player nya
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS)); // Vertical layout
        playerPanel.setBackground(new Color(245, 245, 245));

        // Add player kedalam panel
        loadLeaderboardData(playerPanel);

        // Add the playerPanel to the leaderboardPanel
        JScrollPane scrollPane = new JScrollPane(playerPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scroll
        leaderboardPanel.add(scrollPane);

        // Add vertical space
        leaderboardPanel.add(Box.createVerticalStrut(20));

        // Tombol Kembali
        JButton backButton = new JButton("Kembali");
        backButton.setFont(new Font("Poppins", Font.BOLD, 14));
        backButton.setBackground(new Color(255, 87, 34)); // Orange Red
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 87, 34), 2), // Border
            BorderFactory.createEmptyBorder(5, 20, 5, 20) // Padding
        ));

        backButton.addActionListener(e -> {
            soundEffect.playSoundEffect("Assets/sound/tap.wav");  
            leaderboardFrame.dispose(); 
        });

        leaderboardPanel.add(backButton);

        // Add leaderboardPanel to the frame
        leaderboardFrame.add(leaderboardPanel, BorderLayout.CENTER);

        leaderboardFrame.setVisible(true);
    }

    // Method to load leaderboard data from the database
    private void loadLeaderboardData(JPanel playerPanel ) {
        ArrayList<User> users = dbHandler.getUsers();
        for (User user : users) {
            // Create a label for each player and center it
            String playerData = user.getName() + " - " + user.getScore();
            JLabel playerLabel = new JLabel(playerData);
            playerLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
            playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            playerPanel.add(playerLabel);
        }
    }
}
