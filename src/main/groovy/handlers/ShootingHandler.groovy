package handlers

import com.fasterxml.jackson.databind.JsonNode
import groovy.json.JsonSlurper
import model.Game
import model.GamePhase
import model.player.PlayerId
import model.State
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

            final Game game = ctx.get(Game)

            if (game.myTurn(ctx.get(PlayerId)) && game.gamePhase == GamePhase.SHOOTOUT) {
                State state = game.shootAt(fireCoordinate, ctx.get(PlayerId))
                ctx.response.status(200).contentType('application/json').send(toJson(state))
            } else {
                ctx.response.status(418).send()
            }
        }
    }
}
