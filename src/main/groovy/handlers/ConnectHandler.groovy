package handlers

import groovy.util.logging.Slf4j
import model.Game
import model.Player
import ratpack.handling.Context
import ratpack.handling.Handler

import static groovy.json.JsonOutput.toJson

/**
 * Created by sven on 11.08.15.
 */
@Slf4j
class ConnectHandler implements Handler {
    @Override
    void handle(Context ctx) throws Exception {
        Game game = ctx.get(Game)
        final Optional<String> id = connect(game)
        log.error(game.toString())
        if(id.present) {
            ctx.response.status(200).send(toJson([playerId: id.get()]))
        } else {
            ctx.response.status(409).send(toJson([playerId: "full"]))
        }
    }

    private Optional<String> connect(Game game) {
        //Groovy truth erklären bzw. Beispielaufgabe machen
        if (game.game.player1) {
            if (game.game.player2) {
                Optional.empty()
            } else {
                game.game.player2 = new Player(name: "Player 2", id: "2abc", field: [:])
                Optional.of(game.game.player2.id)
            }
        } else {
            game.game.player1 = new Player(name: "Player 1", id: "1xyz", field: [:])
            Optional.of(game.game.player1.id)
        }
    }
}
