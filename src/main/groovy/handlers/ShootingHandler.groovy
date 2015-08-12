package handlers

import com.fasterxml.jackson.databind.JsonNode
import groovy.json.JsonSlurper
import model.Game
import model.Player
import ratpack.handling.Context
import ratpack.handling.Handler

/**
 * Created by sven on 12.08.15.
 */
class ShootingHandler implements Handler {

    JsonSlurper slurper = new JsonSlurper()

    @Override
    void handle(Context ctx) throws Exception {

        ctx.parse(JsonNode).onError {
            ctx.response.status(500).send()
        } then { JsonNode jsonNode ->
            final Map<String,String> fireCoordinate = slurper.parseText(jsonNode.asText())
            final String playerId = ctx.request.headers.get("playerId")
            final Game game = ctx.get(Game)



        }


    }
}
