package OurGame.Audio;

import javax.sound.sampled.*;
import java.net.URL;

public class SoundManager {
    private Clip clip;
    private FloatControl volumeControl;

    public SoundManager(String resourcePath) {
        loadClip(resourcePath);
    }

    private void loadClip(String resourcePath) {
        try {
            URL url = getClass().getResource(resourcePath);
            if (url == null) {
                System.err.println("Audio resource not found: " + resourcePath);
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(ais);
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }
        } catch (Exception e) {
            System.err.println("Failed to load audio: " + resourcePath);
            e.printStackTrace();
        }
    }

    public void playLoop(float gainDb) {
        if (clip == null) return;
        setVolume(gainDb);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

    public void setVolume(float gainDb) {
        if (volumeControl == null) return;
        float clamped = Math.max(volumeControl.getMinimum(), Math.min(volumeControl.getMaximum(), gainDb));
        volumeControl.setValue(clamped);
    }

    public void stop() {
        if (clip == null) return;
        clip.stop();
    }
}
