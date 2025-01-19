package model;
import javax.swing.ImageIcon;

public class Card {
    String cardName;
    ImageIcon cardImageIcon;
    
    public Card(String cardName, ImageIcon cardImageIcon) {
        this.cardName = cardName;
        this.cardImageIcon = cardImageIcon;
    }

    public String toString() {
        return cardName;
    }

    public ImageIcon getCardImageIcon() {
        return this.cardImageIcon;
    }
}