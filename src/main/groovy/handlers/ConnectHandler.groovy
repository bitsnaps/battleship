package handlers

import groovy.util.logging.Slf4j
import model.Game
import model.Player
import ratpack.handling.Context
import ratpack.handling.Handler

import static groovy.json.JsonOutput.toJson

@Slf4j
class ConnectHandler implements Handler {
    @Override
    void handle(Context ctx) throws Exception {
        Game game = ctx.get(Game)
        final Optional<String> id = connect(game)
        log.error(game.toString())
        if(id.present) {
            ctx.response.status(201).send(toJson([playerId: id.get()]))
        } else {
            ctx.response.status(409).send(toJson([playerId: "full"]))
        }
    }

    private Optional<String> connect(Game game) {
        //Groovy truth erklären bzw. Beispielaufgabe machen
        game.addPlayer()
    }
}
