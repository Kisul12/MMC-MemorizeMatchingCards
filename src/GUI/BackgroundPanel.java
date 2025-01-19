// package GUI;
// import java.awt.Graphics;
// import java.awt.Image;

// import javax.swing.ImageIcon;
// import javax.swing.JPanel;

// // Panel untuk menampilkan background   
// class BackgroundPanel extends JPanel {
//     private Image backgroundImage;

//     public BackgroundPanel(String imagePath) {
//         // Memuat gambar latar belakang
//         this.backgroundImage = new ImageIcon(imagePath).getImage();
//         if (backgroundImage == null) {
//             System.out.println("Gambar tidak ditemukan!");
//         } else {
//             System.out.println("Gambar berhasil dimuat!");
//         }        
//     }

//     @Override
//     protected void paintComponent(Graphics g) {
//         super.paintComponent(g);
//         // Menggambar gambar latar belakang
//         g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
//     }
// }