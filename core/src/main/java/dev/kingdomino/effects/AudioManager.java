package dev.kingdomino.effects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    public static AudioManager instance;
    private Music music;

    private Sound scoreSound;
    private Sound scoreSound2;
    private Sound movingSound;
    private Sound placingSound;
    private Sound selectingSound;
    private Sound rotatingSound;
    private Sound cancelSound;
    private Sound newTurnSound;
    private Sound confirmSelectingSound;
    private Sound highlightSound;
    private Sound endGameSound;
    private Sound reduceSound;
    private Sound increaseSound;
    private Sound increaseSound2;

    private float masterVolume = 0.25f;
    private float musicVolume = 1.0f;
    private float effectsVolume = 1.0f;

    public enum SoundType {
        SCORE,
        SCORE2,
        SCOREREDUCING,
        SCOREINCREASING,
        SCOREINCREASING2,
        MOVING,
        PLACING,
        SELECTING,
        ROTATING,
        CANCEL,
        NEWTURN,
        CONFIRMSELECTING,
        HIGHLIGHT,
        ENDGAME
    }

    public static synchronized AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void load(AssetManager assetManager) {
        music = assetManager.get("audio/music.wav");
        music.setLooping(true);
        music.setPosition(20.0f);
        music.setPan(0.5f, musicVolume * masterVolume);

        movingSound = assetManager.get("audio/moving.ogg");
        scoreSound = assetManager.get("audio/scoring.ogg");
        placingSound = assetManager.get("audio/placing.ogg");
        selectingSound = assetManager.get("audio/selecting.ogg");
        rotatingSound = assetManager.get("audio/rotating.ogg");
        cancelSound = assetManager.get("audio/cancel.ogg");
        scoreSound2 = assetManager.get("audio/scoring2.ogg");
        newTurnSound = assetManager.get("audio/newturn.ogg");
        confirmSelectingSound = assetManager.get("audio/confirmselecting.ogg");
        highlightSound = assetManager.get("audio/highlight1.ogg");
        endGameSound = assetManager.get("audio/win.ogg");
        reduceSound = assetManager.get("audio/timpani.ogg");
        increaseSound = assetManager.get("audio/multhit1.ogg");
        increaseSound2 = assetManager.get("audio/multhit2.ogg");
    }

    public void playMusic() {
        music.play();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSound(SoundType soundType) {
        float effectVol = effectsVolume * masterVolume;
        switch (soundType) {
            case SCORE:
                scoreSound.play(effectVol);
                break;
            case SCORE2:
                scoreSound2.play(effectVol);
                break;
            case MOVING:
                movingSound.play(effectVol);
                break;
            case PLACING:
                placingSound.play(effectVol);
                break;
            case SELECTING:
                selectingSound.play(effectVol);
                break;
            case ROTATING:
                rotatingSound.play(effectVol);
                break;
            case CANCEL:
                cancelSound.play(effectVol);
                break;
            case NEWTURN:
                newTurnSound.play(effectVol * 1.5f); // dice roll sound a bit too quiet
                break;
            case CONFIRMSELECTING:
                confirmSelectingSound.play(effectVol);
                break;
            case HIGHLIGHT:
                highlightSound.play(effectVol);
                break;
            case ENDGAME:
                endGameSound.play(effectVol);
                break;
            case SCOREREDUCING:
                reduceSound.play(effectVol);
                break;
            case SCOREINCREASING:
                increaseSound.play(effectVol);
                break;
            case SCOREINCREASING2:
                increaseSound2.play(effectVol);
                break;
            default:
                break;
        }
    }

    public void dispose() {
        music.dispose();
    }

    public void setMasterVolume(float volume) {
        masterVolume = volume;
        music.setVolume(musicVolume * masterVolume);
    }

    public void setMusicVolume(float volume) {
        musicVolume = volume;
        music.setVolume(musicVolume * masterVolume);
    }

    public void setEffectsVolume(float volume) {
        effectsVolume = volume;
    }
}
