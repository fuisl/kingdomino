package dev.kingdomino.game;

import com.badlogic.gdx.Game;

public class Kingdomino extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen());
    }   

}
