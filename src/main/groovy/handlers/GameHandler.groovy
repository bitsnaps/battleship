package handlers

import com.fasterxml.jackson.databind.JsonNode
import model.Game
import ratpack.handling.Context
import ratpack.handling.Handler

class GameHandler implements Handler {
    @Override
    void handle(Context ctx) throws Exception {

        //Polling resource
        ctx.parse(JsonNode).onError {
            ctx.response.status(500).send()
        } then { JsonNode jsonNode ->
            final String playerId = ctx.request.headers.get('playerId')
            final Game game = ctx.get(Game)

            playerId == game.currentPlayerId
        }
    }
}
