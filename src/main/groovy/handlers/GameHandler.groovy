package handlers

import model.Game
import model.player.PlayerId
import ratpack.handling.Context
import ratpack.handling.Handler

import static groovy.json.JsonOutput.toJson

class GameHandler implements Handler {
    @Override
    void handle(Context ctx) throws Exception {

        final Game game = ctx.get(Game)
        final PlayerId playerId = ctx.get(PlayerId)
        ctx.response.send(toJson(game.getState(playerId)))
    }
}
