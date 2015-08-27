package handlers

import groovy.util.logging.Slf4j
import model.Game
import model.player.PlayerId
import ratpack.handling.Context
import ratpack.handling.Handler

import static groovy.json.JsonOutput.toJson

@Slf4j
class ConnectHandler implements Handler {
    @Override
    void handle(Context ctx) throws Exception {
        Game game = ctx.get(Game)
        final Optional<PlayerId> id = game.addPlayer()

        if(id.present) {
            ctx.response.status(201).send(toJson(game.getState(id.get())))
        } else {
            ctx.response.status(409).send(toJson([playerId: "full"]))
        }
    }
}
