package handlers

import com.fasterxml.jackson.databind.JsonNode
import groovy.json.JsonSlurper
import model.FieldState
import model.Game
import model.GamePhase
import ratpack.handling.Context
import ratpack.handling.Handler

import static groovy.json.JsonOutput.toJson

class ShootingHandler implements Handler {

    JsonSlurper slurper = new JsonSlurper()

    @Override
    void handle(Context ctx) throws Exception {
        ctx.parse(JsonNode).onError {
            ctx.response.status(500).send()
        } then { JsonNode jsonNode ->
            final Map<String, String> fireCoordinate = slurper.parseText(jsonNode.toString())
            final String playerId = ctx.request.headers.get('playerId')

            final Game game = ctx.get(Game)

            if (game.myTurn(playerId) && game.gamePhase == GamePhase.SHOOTOUT) {
                FieldState fieldState = game.shootAt(fireCoordinate, playerId)
                ctx.response.status(200).contentType('application/json').send(toJson([shellingResult: fieldState]))
            } else {
                ctx.response.status(418).send()
            }
        }
    }
}
