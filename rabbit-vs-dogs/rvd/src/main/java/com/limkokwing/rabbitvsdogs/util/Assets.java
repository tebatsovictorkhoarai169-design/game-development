package com.limkokwing.rabbitvsdogs.util;

public final class Assets
{
    //they belong to the class itself, not to any specific copy
    private static final String IMAGES = "/com/limkokwing/rabbitvsdogs/images/";
    private static final String VIDEOS = "/com/limkokwing/rabbitvsdogs/videos/";
    private static final String AUDIO = "/com/limkokwing/rabbitvsdogs/audio/";

    //other class can use these since they're public
        // images
    public static final String WELCOME_BACKGROUND_IMAGE = IMAGES + "background.png";
    public static final String LOGO_IMAGE = IMAGES + "logo.png";
    public static final String SETTING_DOG_IMAGE = IMAGES + "sittingdog.png";
    public static final String SITTING_RABBIT_IMAGE = IMAGES + "sittingrabbit.png";
        // videos
    public static final String BACKGROUND_VIDEO = VIDEOS + "background.mp4";
        // audios
    public static final String BACKGROUND_MUSIC = AUDIO + "background.mp3";

    private Assets() {}//private constructor

    //function
    public static String url(String classpathResource)
    {
        var resource = Assets.class.getResource(classpathResource);

        if (resource == null)
        {
            throw new IllegalStateException("Missing bundled resource: " + classpathResource);
        }
        return resource.toExternalForm();
    }
}
