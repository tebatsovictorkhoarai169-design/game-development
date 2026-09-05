package com.limkokwing.rabbitvsdogs.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public final class SoundManager
{
    private static final SoundManager INSTANCE = new SoundManager();

    private MediaPlayer musicPlayer;
    private boolean started = false;

    private SoundManager() {}

    public static SoundManager getInstance()
    {
        return INSTANCE;
    }

    //loop the sound
    public void startBackgroundMusic()
    {
        if (started)
        {
            return;
        }

        Media media = new Media(Assets.url(Assets.BACKGROUND_MUSIC));
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        SettingsManager settings = SettingsManager.getInstance();
        musicPlayer.setVolume(settings.getVolume());
        musicPlayer.setMute(!settings.isMusicOn());

        // volume/mute
        settings.volumeProperty().addListener((obs, o, n) -> musicPlayer.setVolume(n.doubleValue()));
        settings.musicOnProperty().addListener((obs, o, n) -> musicPlayer.setMute(!n));

        musicPlayer.play();
        started = true;
    }

    // terminate the sound
    public void stopBackgroundMusic()
    {
        if (musicPlayer != null)
        {
            musicPlayer.stop();
            musicPlayer.dispose();
        }
        started = false;
    }
}