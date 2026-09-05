package com.limkokwing.rabbitvsdogs.model;

public enum Difficulty
{
    EASY("Easy", 10 * 60),
    MEDIUM("Medium", 5 * 60),
    HARD("Hard", 2 * 60),
    EXTREME("Extreme", 60);

    private final String label;
    private final int seconds;

    Difficulty(String label, int seconds)
    {
        this.label = label;
        this.seconds = seconds;
    }

    public String getLabel()
    {
        return label;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public String getFullLabel()
    {
        int minutes = seconds / 60;

        if (seconds < 60)
        {
            return label + " (" + seconds + " sec)";
        }
        return label + " (" + minutes + " min)";
    }

    @Override
    public String toString()
    {
        return getFullLabel();
    }
}
