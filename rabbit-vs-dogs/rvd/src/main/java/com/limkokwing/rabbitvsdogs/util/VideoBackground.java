package com.limkokwing.rabbitvsdogs.util;

import javafx.beans.value.ChangeListener;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoBackground
{
    private final MediaPlayer player;
    private final MediaView view;

    public VideoBackground(Region sizeTarget)
    {
        Media media = new Media(Assets.url(Assets.BACKGROUND_VIDEO));
        player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setMute(true);
        player.setAutoPlay(true);

        view = new MediaView(player);
        view.setPreserveRatio(false);
        view.setSmooth(true);

        ChangeListener<Number> resize = (obs, o, n) ->
        {
            view.setFitWidth(sizeTarget.getWidth());
            view.setFitHeight(sizeTarget.getHeight());
        };

        sizeTarget.widthProperty().addListener(resize);
        sizeTarget.heightProperty().addListener(resize);
        view.setFitWidth(sizeTarget.getWidth());
        view.setFitHeight(sizeTarget.getHeight());
    }

    public MediaView getView()
    {
        return view;
    }

    public void dispose()
    {
        player.stop();
        player.dispose();
    }
}
