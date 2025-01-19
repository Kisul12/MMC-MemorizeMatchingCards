import GUI.*;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Memoriez Matching Card");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setLocationRelativeTo(null);

                CardLayout cardLayout = new CardLayout();
                JPanel mainPanel = new JPanel(cardLayout);

                HomePage homePage = new HomePage(cardLayout, mainPanel);
                mainPanel.add(homePage, "HomePage");

                frame.add(mainPanel);
                frame.setVisible(true);
                System.out.println("Main frame is visible");

                // Debugging: Check if HomePage is added and shown
                cardLayout.show(mainPanel, "HomePage");
                System.out.println("HomePage should be visible");
            }
        });
    }
}