package utils;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    private Clip backgroundMusic;

    // Metode untuk memutar musik latar
    public void playBackgroundMusic(String filePath) {
        new Thread(() -> {
            try {
                File file = new File(filePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audioStream);
                // Menambahkan listener untuk mendeteksi saat musik selesai
                backgroundMusic.addLineListener(new LineListener() {
                    @Override
                    public void update(LineEvent event) {
                        if (event.getType() == LineEvent.Type.STOP) {
                            // Jika musik selesai (STOP), kita ulangi musik
                            backgroundMusic.setFramePosition(0);  // Set posisi ke awal
                            backgroundMusic.start();  // Mulai lagi dari awal
                        }
                    }
                });

                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);  // Mengatur loop secara terus menerus
                backgroundMusic.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void playSoundEffect(String filePath) {
        new Thread(() -> {
            try {
                File file = new File(filePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip soundEffect = AudioSystem.getClip();
                soundEffect.open(audioStream);
                soundEffect.start();  // Memulai pemutaran sound effect
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();  // Menghentikan pemutaran musik
            backgroundMusic.close(); // Menutup Clip untuk membersihkan sumber daya
        }
    }
}
