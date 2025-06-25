import javax.sound.sampled.*;
import java.net.URL;
import java.io.IOException;

public enum SoundEffect {
    EAT_FOOD("audio/eatfood.wav"),
    EXPLODE("audio/explode.wav"),
    DIE("audio/die.wav");


    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.HIGH;

    private Clip clip;

    private SoundEffect(String soundFileName) {
        try {
            URL url = getClass().getClassLoader().getResource(soundFileName);
            if (url == null) {
                System.err.println("File not found: " + soundFileName);
                return; // Keluar jika file tidak ditemukan
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream); // Pastikan clip dibuka
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    public void play() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning())
                clip.stop();   // Stop the player if it is still running
            clip.setFramePosition(0); // rewind to the beginning
            clip.start();     // Start playing
        }
    }

    static void initGame() {
        values(); // calls the constructor for all the elements
    }

    public Clip getClip() {
        return clip; // Getter untuk clip
    }

}
