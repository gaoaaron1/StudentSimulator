package com.game.student.Scenes;

import static com.game.student.Const.gameScreen;
import static com.game.student.Simulator.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.student.Const;
import com.game.student.Screens.SelectScreen;
import com.game.student.Simulator;

public class Options implements Disposable {

    /*===================================== INITIALIZED VARIABLES  ================================================*/
    private TextureAtlas atlas;
    private Skin skin;
    public TextButton closeButton;
    public TextButton.TextButtonStyle textButtonStyle;

    public Image settingsMenu;
    public boolean enableSetting = false;

    public Container<Slider> musicContainer;
    public static Container<Slider> soundContainer;

    public Music musics;
    private Sound sounds;
    public float savedMusicVolume, savedSoundVolume, tempMusicVolume, tempSoundVolume;

    public boolean enableMusic = true;
    public boolean enableSound = true;

    public Stage stage;
    public static ImageButton musicButton, soundButton;
    public ImageButton.ImageButtonStyle musicStyle, soundStyle;

    public Skin skin2 = new Skin(Gdx.files.internal("slider.json"));
    public Slider slider = new Slider(0, 100, 1, false, skin2);

    //Slider variables
    public Slider slider2 = new Slider(0, 100, 1, false, skin2);

    private ImageButton settingButton;
    private ImageButton.ImageButtonStyle settingStyle;

    /*===================================== CONSTRUCTOR ================================================*/

    public Options() {
        skin = Simulator.buttonSkin;

        //Create stage
        stage = new Stage(new ExtendViewport(Const.V_WIDTH, Const.V_HEIGHT));

        settingStyle = new ImageButton.ImageButtonStyle();
        settingStyle.up = Simulator.buttonSkin.getDrawable("options_up");
        settingStyle.down = Simulator.buttonSkin.getDrawable("options_down");

        settingButton = new ImageButton(settingStyle);

        //Sound variables
        savedMusicVolume = Hud.prefs.getFloat("musicVolume:", Const.defaultVolume);
        savedSoundVolume = Hud.prefs.getFloat("soundVolume:", Const.defaultVolume);

        tempMusicVolume = savedMusicVolume;
        tempSoundVolume = savedSoundVolume;

        setMenu();
        setMusic("menu");
        setSound();

        musicStyle = new ImageButton.ImageButtonStyle();
        soundStyle = new ImageButton.ImageButtonStyle();

        loadSavedAudio();

        musicButton = new ImageButton(musicStyle);
        soundButton = new ImageButton(soundStyle);


        setButtonParameters();

        //Initialize containers
        musicContainer = new Container<Slider>(slider);
        createMusicSlider(musicContainer);
        soundContainer = new Container<Slider>(slider2);
        createSoundSlider(soundContainer);

        //Creates functionality for buttons
        createButtonFunctionality();
    }

    /*===================================== METHODS ================================================*/
    private void setButtonParameters() {
        ImageButton.ImageButtonStyle musicStyle = new ImageButton.ImageButtonStyle(Simulator.imageButtonStyle);
        musicStyle.imageUp = Simulator.buttonSkin.getDrawable("music_on_up");
        musicStyle.imageDown = Simulator.buttonSkin.getDrawable("music_on_up");
        musicButton.setWidth(Const.V_WIDTH / 8f);
        musicButton.setHeight(Const.V_WIDTH / 8f);
        musicButton.setPosition(Const.V_WIDTH / 1.35f, Const.V_HEIGHT / 0.87f);

        ImageButton.ImageButtonStyle soundStyle = new ImageButton.ImageButtonStyle(Simulator.imageButtonStyle);
        soundStyle.imageUp = Simulator.buttonSkin.getDrawable("volume_on_up");
        soundStyle.imageDown = Simulator.buttonSkin.getDrawable("volume_on_up");
        soundButton.setWidth(Const.V_WIDTH / 8f);
        soundButton.setHeight(Const.V_WIDTH / 8f);
        soundButton.setPosition(Const.V_WIDTH / 1.35f, Const.V_HEIGHT / 0.95f);

        ImageButton.ImageButtonStyle settingStyle = new ImageButton.ImageButtonStyle(Simulator.imageButtonStyle);
        settingStyle.imageUp = Simulator.buttonSkin.getDrawable("options_up");
        settingStyle.imageDown = Simulator.buttonSkin.getDrawable("options_down");
        settingButton.setWidth(Const.V_WIDTH / 5f);
        settingButton.setHeight(Const.V_WIDTH / 5f);
        settingButton.setPosition(Const.V_WIDTH / 0.8f, Const.V_HEIGHT / 1.8f);
        stage.addActor(settingButton);
    }

    private void createButtonFunctionality() {

        //Music Button
        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (enableMusic == true) {
                    musicStyle.up = Simulator.buttonSkin.getDrawable("music_off_up");
                    musicStyle.down = Simulator.buttonSkin.getDrawable("music_off_down");
                    slider.setValue(0);
                    Hud.prefs.putFloat("musicVolume:", slider.getValue());
                    Hud.prefs.flush();
                    musics.pause();
                    enableMusic = false;
                    playButtonClick();
                } else {
                    musicStyle.up = Simulator.buttonSkin.getDrawable("music_on_up");
                    musicStyle.down = Simulator.buttonSkin.getDrawable("music_on_down");
                    slider.setValue(tempMusicVolume);
                    Hud.prefs.putFloat("musicVolume:", slider.getValue());
                    Hud.prefs.flush();
                    musics.play();
                    enableMusic = true;
                    playButtonClick();
                }
            }
        });

        settingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButtonClick();
                optionsHandler();
            }
        });

        //Sound button
        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (enableSound == true) {
                    soundStyle.up = Simulator.buttonSkin.getDrawable("volume_off_up");
                    soundStyle.down = Simulator.buttonSkin.getDrawable("volume_off_down");
                    slider2.setValue(0);
                    Hud.prefs.putFloat("soundVolume:", slider2.getValue());
                    Hud.prefs.flush();
                    enableSound = false;
                } else {
                    soundStyle.up = Simulator.buttonSkin.getDrawable("volume_on_up");
                    soundStyle.down = Simulator.buttonSkin.getDrawable("volume_on_down");
                    slider2.setValue(tempSoundVolume);
                    Hud.prefs.putFloat("soundVolume:", slider2.getValue());
                    Hud.prefs.flush();
                    enableSound = true;
                    playButtonClick();
                }
            }
        });
    }

    public void optionsHandler() {
        if (enableSetting == false) {
            stage.addActor(settingsMenu);
            stage.addActor(musicContainer);
            stage.addActor(closeButton);
            stage.addActor(soundContainer);
            stage.addActor(musicButton);
            stage.addActor(soundButton);
            Gdx.input.setInputProcessor(stage);
            enableSetting = true;
        } else {
            settingsMenu.remove();
            musicContainer.remove();
            closeButton.remove();
            soundContainer.remove();
            musicButton.remove();
            soundButton.remove();
            enableSetting = false;
            if (gameScreen == 0) {
                Gdx.input.setInputProcessor(SelectScreen.multiplexer);
            } else if (gameScreen == 1) {
                Gdx.input.setInputProcessor(Hud.stage2);
            }

        }
    }

    public void setMenu() {
        settingsMenu = new Image(new Texture(Gdx.files.internal("menutemplate.png")));
        settingsMenu.setWidth(Const.V_WIDTH / 1.5f);
        settingsMenu.setHeight(Const.V_HEIGHT / 1.5f);
        settingsMenu.setPosition(Const.V_WIDTH / 1.5f, Const.V_HEIGHT / 1.5f);

        //Initialize buttons
        closeButton = new TextButton("", Simulator.textButtonStyle);
        closeButton.setColor(new Color(0.5f, 0.0f, 0.0f, 1.00f));
        closeButton.setText("X");
        closeButton.setWidth(Const.V_WIDTH / 11f);
        closeButton.setHeight(Const.V_WIDTH / 11f);
        closeButton.setPosition(Const.V_WIDTH / 0.8f, Const.V_HEIGHT / 0.785f);


        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButtonClick();
                optionsHandler();

            }
        });
    }

    private void createMusicSlider(Container<Slider> container) {
        slider.setValue(savedMusicVolume);
        container.setTransform(true);   // for enabling scaling and rotation

        container.setOrigin(container.getWidth() / 2, container.getHeight() / 2);
        container.setWidth(Const.V_WIDTH / 2f);
        container.setHeight(Const.V_HEIGHT / 8f);
        container.setPosition(Const.V_WIDTH / 3f, Const.V_HEIGHT / 1f);
        container.setScale(3);  //scale according to your requirement

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Set slider to our pref volume otherwise set default of 50
                Hud.prefs.putFloat("musicVolume:", slider.getValue());
                Hud.prefs.flush();

                if (slider.getValue() > 1) {
                    tempMusicVolume = slider.getValue();
                }
                musics.setVolume(slider.getValue() / 100.0f);
                Const.defaultVolume = slider.getValue();

                if (slider.getValue() > 0) {
                    musicStyle.up = Simulator.buttonSkin.getDrawable("music_on_up");
                    musicStyle.down = Simulator.buttonSkin.getDrawable("music_on_down");
                    musics.play();
                    enableMusic = true;
                } else {
                    musicStyle.up = Simulator.buttonSkin.getDrawable("music_off_up");
                    musicStyle.down = Simulator.buttonSkin.getDrawable("music_off_down");
                    musics.pause();
                    enableMusic = false;
                }
            }
        });
    }

    private void createSoundSlider(Container<Slider> container) {
        //Set slider to our pref volume otherwise set default of 50
        slider2.setValue(savedSoundVolume);
        container.setTransform(true);   // for enabling scaling and rotation

        container.setOrigin(container.getWidth() / 2, container.getHeight() / 2);
        container.setWidth(Const.V_WIDTH / 2f);
        container.setHeight(Const.V_HEIGHT / 8f);
        container.setPosition(Const.V_WIDTH / 3f, Const.V_HEIGHT / 1.1f);
        container.setScale(3);  //scale according to your requirement

        slider2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                Hud.prefs.putFloat("soundVolume:", slider2.getValue());
                Hud.prefs.flush();

                if (slider2.getValue() > 1) {
                    tempSoundVolume = slider2.getValue();
                }

                if (slider2.getValue() > 0) {
                    soundStyle.up = Simulator.buttonSkin.getDrawable("volume_on_up");
                    soundStyle.down = Simulator.buttonSkin.getDrawable("volume_on_down");
                    sounds.setVolume(0, slider2.getValue() / 100.0f);
                    enableSound = true;
                } else {
                    soundStyle.up = Simulator.buttonSkin.getDrawable("volume_off_up");
                    soundStyle.down = Simulator.buttonSkin.getDrawable("volume_off_down");
                    enableSound = false;
                }

            }
        });

    }

    public void setSound() {
        sounds = manager.get("audio/music/click.wav", Sound.class);
    }

    public void setMusic(String soundtrack) {

        //Allows for music to be played constructor
        if (soundtrack == "menu") {
            musics = manager.get("audio/music/village.mp3", Music.class);
        } else if (soundtrack == "game") {
            musics = manager.get("audio/music/IndianRain.mp3", Music.class);
        }

        musics.setLooping(true);
        musics.setVolume(savedMusicVolume / 10);
        musics.play();

        //Allows for sound to be played
        //sounds = manager.get("audio/music/click.wav", Sound.class);
    }

    public void playButtonClick() {
        if (enableSound) {
            sounds.setVolume(sounds.play(), slider2.getValue() / 100.0f);
        }
    }

    private void loadSavedAudio() {
        if (savedMusicVolume > 0) {
            musicStyle.up = Simulator.buttonSkin.getDrawable("music_on_up");
            musicStyle.down = Simulator.buttonSkin.getDrawable("music_on_down");
        } else {
            musicStyle.up = Simulator.buttonSkin.getDrawable("music_off_up");
            musicStyle.down = Simulator.buttonSkin.getDrawable("music_off_down");
        }

        if (savedSoundVolume > 0) {
            soundStyle.up = Simulator.buttonSkin.getDrawable("volume_on_up");
            soundStyle.down = Simulator.buttonSkin.getDrawable("volume_on_down");
        } else {
            soundStyle.up = Simulator.buttonSkin.getDrawable("volume_off_up");
            soundStyle.down = Simulator.buttonSkin.getDrawable("volume_off_down");
        }
    }

    public void setSettingButtonVisible(boolean isVisible) {
        settingButton.setVisible(isVisible);
    }


    /*=========================== GETTERS =====================================*/
    public Skin getSkin() {
        return skin;
    }

    public Music getMusic() {
        return musics;
    }

    public Sound getSound() {
        return sounds;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        musics.dispose();
        //stage.dispose();
    }
}
