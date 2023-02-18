package com.game.student.Scenes;

import static com.game.student.Const.gameScreen;
import static com.game.student.Screens.SelectScreen.options;
import static com.game.student.Simulator.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.student.Const;
import com.game.student.Screens.PlayScreen;
import com.game.student.Screens.SelectScreen;
import com.game.student.Simulator;
import com.game.student.Tools.GifDecoder;

public class Tutorial implements Disposable, GestureDetector.GestureListener {

    /*======================================= INITIALIZED VARIABLES ===================================*/

    //Stage
    public Stage stage;

    // Text Buttons
    private TextButton exitButton, leftButton, rightButton;
    public TextButton closeButton;

    //Menu features
    public Image settingsMenu;
    public boolean enableTutorial = false;

    //Font features
    private BitmapFont textFont;
    public static Label tutorialTitleLabel, tutorialTextLabel;
    private String tutorialTitleString, tutorialTextString;

    private TextureRegion loading_circle_tex;
    public Sprite loading_circle;

    //Gif handler
    public float elapsed;
    public Animation<TextureRegion> animation, animation2;
    public Array<Animation<TextureRegion>> animations;
    private final String[] animation_names;
    private final float[] animation_duration;
    private int curr_anim_index = 0;
    private int gifImg;
    public boolean gifsLoaded = false;

    //input handler
    public InputMultiplexer inputMultiPlex;
    private GestureDetector gestureDetector;

    /*======================================= CONSTRUCTOR ===================================*/
    public Tutorial() {
        //Create stage
        stage = new Stage(new ExtendViewport(Const.V_WIDTH, Const.V_HEIGHT));

        animation_names = new String[8];
        animation_duration = new float[8];
        animations = new Array<>(8);

        loading_circle_tex = Simulator.buttonSkin.getRegion("loading_circle");
        loading_circle = new Sprite(loading_circle_tex);

        loading_circle.setPosition(-(Const.V_WIDTH) / 8f, -(Const.V_HEIGHT) / 20f);
        loading_circle.setSize(Const.V_WIDTH / 5f, Const.V_WIDTH / 5f);
        loading_circle.setOrigin(Const.V_WIDTH / 10f, Const.V_WIDTH / 10f);

        animation_names[0] = "gifs/fling.gif";
        animation_names[1] = "gifs/tutorial_item.gif";
        animation_names[2] = "gifs/tutorial_enemytypes.gif";
        animation_names[3] = "gifs/tutorial_headon.gif";
        animation_names[4] = "gifs/tutorial_tail.gif";
        animation_names[5] = "gifs/tutorial_cuttail.gif";
        animation_names[6] = "gifs/tutorial_head.gif";
        animation_names[7] = "gifs/tutorial_end.gif";

        animation_duration[0] = 0.10f;
        animation_duration[1] = 0.10f;
        animation_duration[2] = 0.10f;
        animation_duration[3] = 0.10f;
        animation_duration[4] = 0.10f;
        animation_duration[5] = 0.10f;
        animation_duration[6] = 0.10f;
        animation_duration[7] = 0.05f;

        //Declares our tutorial menu
        setMenu();

        //Set tutorial labels
        settutorialLabels();

        //Declares our navigator buttons
        setNavigationButtons();

        //Set button parameters
        setButtonParameters();

        //Set button functionality
        setButtonFunctionality();

        gifImg = 0;
        gifHandler(gifImg);

        //Declares gesture input
        inputMultiPlex = new InputMultiplexer();
        gestureDetector = new GestureDetector(this);
        inputMultiPlex.addProcessor(gestureDetector);
        inputMultiPlex.addProcessor(stage);
    }

    /*======================================= METHODS ===================================*/

    private void gifHandler(int gifImg) {
        switch (gifImg) {
            case 0:
                tutorialTitleString = "Welcome to the\n" + "tutorial menu\n" + "selection!";
                curr_anim_index = 0;
                tutorialTextString = "Click arrows or swipe\n" + "to navigate between\n" + "the pages.";
                break;
            case 1:
                tutorialTitleString = "Caterpillar eats\n" + "apples!";
                curr_anim_index = 1;
                tutorialTextString = "When a caterpillar eats\n" + "an apple it grows a new\n" + "body part!\n\n"
                        + "BEWARE: Enemies will\n" + "eat apples too!";
                break;
            case 2:
                tutorialTitleString = "Enemies";
                curr_anim_index = 2;
                tutorialTextString = "Enemies the same size or\n" + "longer than player have\n" + "red eyes, Otherwise they\n" + "have normal set of eyes.\n\n"
                        + "NOTE: Avoid Head-on\n" + "collisions with red eyed\n" + "caterpillars!\n";
                break;
            case 3:
                tutorialTitleString = "Head-on collision";
                curr_anim_index = 3;
                tutorialTextString = "In a head-on collision,\n" + "the longer caterpillar\n" + "devours the short one.\n\n"
                        + "NOTE: Caterpillars that\n" + "are the same size\n" + "devour each other.";
                break;
            case 4:
                tutorialTitleString = "Long-tailed enemies";
                curr_anim_index = 4;
                tutorialTextString = "If an enemy is too large,\n" + "chip away at their tails\n" + "then go for the head!\n\n"
                        + "NOTE: Enemy eyes will\n" + "turn white if they are\n" + "smaller than you.";
                break;
            case 5:
                tutorialTitleString = "Cutting tails";
                curr_anim_index = 5;
                tutorialTextString = "Caterpillars can eat body\n" + "parts and the rest of the\n" + "tail falls off!\n\n"
                        + "NOTE: Caterpillars can go\n" + "for the head by eating\n" + "away at the neck.";
                break;
            case 6:
                tutorialTitleString = "Only a head";
                curr_anim_index = 6;
                tutorialTextString = "eat another head from\n" +
                        "the back rather than\n" + "head-on.\n\n"
                        + "NOTE: Caterpillars can go\n" + "for the head by eating\n" + "away at the neck.";
                break;
            case 7:
                tutorialTitleString = "End of tutorial";
                curr_anim_index = 7;
                tutorialTextString = "Enjoy the game!\n\n" + "Press the X button to\n" + "close the tutorial.";
                break;
            default:
                tutorialTitleString = "Pikachu";
                curr_anim_index = 0;
                break;
        }

        if (gifsLoaded) {
            animation = animations.get(curr_anim_index);
            animation2 = animations.get(0);
        }

        tutorialTitleLabel.setText(String.format(tutorialTitleString));
        tutorialTextLabel.setText(String.format(tutorialTextString));
    }

    private void setButtonParameters() {
        //EXIT BUTTON
        exitButton = new TextButton("", Simulator.textButtonStyle);
        exitButton.setStyle(Simulator.textButtonStyle);
        exitButton.setColor(new Color(0.4f, 0.1f, 0.1f, 1.0f));
        exitButton.setText("EXIT");
        exitButton.getLabel().setFontScale(1.2f);
        exitButton.setWidth(Const.V_WIDTH / 2f);
        exitButton.setHeight(Const.V_HEIGHT / 8f);
        exitButton.setPosition(Const.V_WIDTH / 1.35f, Const.V_HEIGHT / 1.25f);
        //stage.addActor(exitButton);


    }

    public void loadGif(String file, float duration) {
        GifDecoder.GIFAnimation gif = manager.get(file, GifDecoder.GIFAnimation.class);
        animations.add(gif.rebuildAnimation(duration, Animation.PlayMode.LOOP));
    }

    public void update() {
        if (!gifsLoaded) {
            for (int i = 0; i < animation_names.length; i++) {
                if (manager.isLoaded(animation_names[i]) && i >= animations.size) {
                    loadGif(animation_names[i], animation_duration[i]);
                }
            }

            if (manager.isLoaded(animation_names[curr_anim_index])) {
                animation = animations.get(curr_anim_index);
                animation2 = animations.get(0);
            } else {
                loading_circle.setRotation(elapsed * 100);
                animation = null;
                animation2 = null;
            }

            if (animations.size == animation_names.length) {
                gifsLoaded = true;
            }
        }
    }

    private void setButtonFunctionality() {
        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                options.playButtonClick();
                if (gifImg > 0) {
                    gifImg -= 1;
                    gifHandler(gifImg);
                }
            }
        });

        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                options.playButtonClick();
                if (gifImg < 7) {
                    gifImg += 1;
                    gifHandler(gifImg);
                }
            }
        });


    }

    private void setNavigationButtons() {
        //LEFT BUTTON
        leftButton = new TextButton("", Simulator.textButtonStyle);
        leftButton.setStyle(Simulator.textButtonStyle);
        leftButton.setColor(new Color(0.1f, 0.1f, 0.1f, 1.0f));
        leftButton.setText("<");
        leftButton.getLabel().setFontScale(1.2f);
        leftButton.setWidth(Const.V_WIDTH / 8f);
        leftButton.setHeight(Const.V_HEIGHT / 8f);
        leftButton.setPosition(Const.V_WIDTH / 2f, Const.V_HEIGHT / 1f);

        //RIGHT BUTTON
        rightButton = new TextButton("", Simulator.textButtonStyle);
        rightButton.setStyle(Simulator.textButtonStyle);
        rightButton.setColor(new Color(0.1f, 0.1f, 0.1f, 1.0f));
        rightButton.setText(">");
        rightButton.getLabel().setFontScale(1.2f);
        rightButton.setWidth(Const.V_WIDTH / 8f);
        rightButton.setHeight(Const.V_HEIGHT / 8f);
        rightButton.setPosition(Const.V_WIDTH * 1.375f, Const.V_HEIGHT / 1f);
    }

    private void settutorialLabels() {
        //Declare font
        textFont = manager.get("fonts/junegull.ttf", BitmapFont.class);

        //Declare title label
        tutorialTitleString = "";
        tutorialTitleLabel = new Label(String.format(tutorialTitleString), new Label.LabelStyle(textFont, Color.WHITE));
        tutorialTitleLabel.setFontScale(0.8f);
        tutorialTitleLabel.setWidth(Const.V_WIDTH / 12f);
        tutorialTitleLabel.setHeight(Const.V_HEIGHT / 12f);
        tutorialTitleLabel.setPosition(Const.V_WIDTH / 1.45f, Const.V_HEIGHT * 1.2f);

        //Declare text body label
        tutorialTextString = "";
        tutorialTextLabel = new Label(String.format(tutorialTextString), new Label.LabelStyle(textFont, Color.WHITE));
        tutorialTextLabel.setFontScale(0.6f);
        tutorialTextLabel.setWidth(Const.V_WIDTH / 12f);
        tutorialTextLabel.setHeight(Const.V_HEIGHT / 12f);
        tutorialTextLabel.setPosition(Const.V_WIDTH / 1.45f, Const.V_HEIGHT / 1.4f);
    }

    public void setMenu() {
        settingsMenu = new Image(new Texture(Gdx.files.internal("menutemplate.png")));
        settingsMenu.setWidth(Const.V_WIDTH / 1.25f);
        settingsMenu.setHeight(Const.V_HEIGHT / 1.25f);
        settingsMenu.setPosition(Const.V_WIDTH / 1.65f, Const.V_HEIGHT / 1.75f);

        //Initialize buttons
        closeButton = new TextButton("", Simulator.textButtonStyle);
        closeButton.setColor(new Color(0.5f, 0.0f, 0.0f, 1.00f));
        closeButton.setText("X");
        closeButton.setWidth(Const.V_WIDTH / 11f);
        closeButton.setHeight(Const.V_WIDTH / 11f);
        closeButton.setPosition(Const.V_WIDTH / 0.757f, Const.V_HEIGHT / 0.762f);


        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                options.playButtonClick();
                curr_anim_index = 0;
                tutorialHandler();
            }
        });
    }

    public void tutorialHandler() {
        if (enableTutorial == false) {
            stage.addActor(settingsMenu);
            stage.addActor(closeButton);
            stage.addActor(leftButton);
            stage.addActor(rightButton);
            stage.addActor(tutorialTitleLabel);
            stage.addActor(tutorialTextLabel);
            Gdx.input.setInputProcessor(inputMultiPlex);
            enableTutorial = true;
        } else {
            settingsMenu.remove();
            closeButton.remove();
            leftButton.remove();
            rightButton.remove();
            tutorialTitleLabel.remove();
            tutorialTextLabel.remove();
            if (gameScreen == 0) {
                Gdx.input.setInputProcessor(SelectScreen.multiplexer);
            } else if (gameScreen == 1) {
                PlayScreen.newPlayer = false;
            }
            enableTutorial = false;
        }
    }


    @Override
    public void dispose() {

    }

    /*========================================== GESTURE METHODS ===================================*/

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        String message = "TAP";
        Gdx.app.log("INFO", message);
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (enableTutorial == true) {
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                if (velocityX > 0) {
                    options.playButtonClick();
                    if (gifImg > 0) {
                        gifImg -= 1;
                        gifHandler(gifImg);
                    }
                } else {
                    options.playButtonClick();
                    if (gifImg < 7) {
                        gifImg += 1;
                        gifHandler(gifImg);
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
