package com.limkokwing.rabbitvsdogs.util;

import com.limkokwing.rabbitvsdogs.model.Difficulty;
import javafx.beans.property.*;

import java.util.prefs.Preferences;

public final class SettingsManager
{
    private static final SettingsManager INSTANCE = new SettingsManager();

    private final Preferences prefs = Preferences.userNodeForPackage(SettingsManager.class);

    private final BooleanProperty soundEffectsOn = new SimpleBooleanProperty(true);
    private final BooleanProperty musicOn = new SimpleBooleanProperty(true);
    private final DoubleProperty volume = new SimpleDoubleProperty(0.6);
    private final ObjectProperty<Difficulty> difficulty = new SimpleObjectProperty<>(Difficulty.MEDIUM);

    private SettingsManager()
    {
        soundEffectsOn.set(prefs.getBoolean("soundEffectsOn", true));
        musicOn.set(prefs.getBoolean("musicOn", true));
        volume.set(prefs.getDouble("volume", 0.6));

        String diffName = prefs.get("difficulty", Difficulty.MEDIUM.name());

        try
        {
            difficulty.set(Difficulty.valueOf(diffName));
        }
        catch (IllegalArgumentException ex)
        {
            difficulty.set(Difficulty.MEDIUM);
        }

        soundEffectsOn.addListener((obs, o, n) -> prefs.putBoolean("soundEffectsOn", n));
        musicOn.addListener((obs, o, n) -> prefs.putBoolean("musicOn", n));
        volume.addListener((obs, o, n) -> prefs.putDouble("volume", n.doubleValue()));
        difficulty.addListener((obs, o, n) -> prefs.put("difficulty", n.name()));
    }

    public static SettingsManager getInstance()
    {
        return INSTANCE;
    }
    public BooleanProperty soundEffectsOnProperty()
    {
        return soundEffectsOn;
    }
    public BooleanProperty musicOnProperty()
    {
        return musicOn;
    }
    public DoubleProperty volumeProperty()
    {
        return volume;
    }
    public ObjectProperty<Difficulty> difficultyProperty()
    {
        return difficulty;
    }
    public boolean isMusicOn()
    {
        return musicOn.get();
    }
    public double getVolume()
    {
        return volume.get();
    }
    public Difficulty getDifficulty()
    {
        return difficulty.get();
    }
}
