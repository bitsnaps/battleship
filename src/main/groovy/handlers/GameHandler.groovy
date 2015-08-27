package handlers

import model.Game
import model.PlayerId
import ratpack.handling.Context
import ratpack.handling.Handler

import static groovy.json.JsonOutput.toJson

class GameHandler implements Handler {
    @Override
    void handle(Context ctx) throws Exception {
        //Polling resource

        final Game game = ctx.get(Game)

        ctx.response.send(toJson(game.getState(ctx.get(PlayerId))))
    }
}
