import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;

public class SoundManager {
    private static final Map<String, Clip> preloadedClips = new HashMap<>();
    private static Clip soundEffectClip;
    private static Clip backgroundMusicClip;

    // Preload a clip for reuse
    public static void preloadClip(String name, String filePath) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            preloadedClips.put(name, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error preloading clip: " + e.getMessage());
        }
    }

    // Play a preloaded clip by name
    public static void playPreloadedClip(String name) {
        Clip clip = preloadedClips.get(name);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        } else {
            System.out.println("Clip not found: " + name);
        }
    }

    // Play a short sound effect for a fixed duration
    public static synchronized void playSoundEffect(String filePath, int durationMs) {
        try {
            if (soundEffectClip != null && soundEffectClip.isRunning()) {
                soundEffectClip.stop();
                soundEffectClip.close();
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            soundEffectClip = AudioSystem.getClip();
            soundEffectClip.open(audioStream);
            soundEffectClip.start();

            new Thread(() -> {
                try {
                    Thread.sleep(durationMs);
                    if (soundEffectClip != null && soundEffectClip.isRunning()) {
                        soundEffectClip.stop();
                        soundEffectClip.close();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error playing sound effect: " + e.getMessage());
        }
    }

    // Play background music (looped or once)
    public static synchronized void playBackgroundMusic(String filePath, boolean loop) {
        try {
            if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
                backgroundMusicClip.stop();
                backgroundMusicClip.close();
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioStream);
            if (loop) {
                backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                backgroundMusicClip.start();
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error playing background music: " + e.getMessage());
        }
    }

    // Stop background music
    public static synchronized void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
        }
    }

    // Unload a preloaded clip
    public static void unloadClip(String name) {
        Clip clip = preloadedClips.remove(name);
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}

 

 

 

/*Example Usage

 

// Preload sounds at startup
SoundManager.preloadClip("hit", "brick_hit.wav");
SoundManager.preloadClip("theme", "background_theme.wav");

// Play preloaded sound effect
SoundManager.playPreloadedClip("hit");

// Play short sound effect directly
SoundManager.playSoundEffect("brick_hit.wav", 300);

// Start background music
SoundManager.playBackgroundMusic("background_theme.wav", true);

// Stop background music
SoundManager.stopBackgroundMusic();

// Unload clip when done
SoundManager.unloadClip("hit");*/
