package handlers

import model.Game
import model.player.PlayerId
import ratpack.handling.Context
import ratpack.handling.Handler

import static groovy.json.JsonOutput.toJson

class GameHandler implements Handler {

    @Override
    void handle(Context ctx) throws Exception {
        //TODO: should return the game state as JSON
    }
}
