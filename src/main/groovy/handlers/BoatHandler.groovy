package handlers

import com.fasterxml.jackson.databind.JsonNode
import groovy.json.JsonSlurper
import model.Game
import model.Player
import ratpack.exec.Promise
import ratpack.handling.Context
import ratpack.handling.Handler

import static ratpack.jackson.Jackson.jsonNode

import static groovy.json.JsonOutput.toJson

/**
 * Created by sven on 11.08.15.
 */
class BoatHandler implements Handler {

    private final JsonSlurper slurper = new JsonSlurper()

    @Override
    void handle(Context ctx) throws Exception {

        ctx.parse(JsonNode).onError {
            ctx.response.status(500).send()
        } then { JsonNode jsonNode ->
            final Map<String, Map<String, String>> boatCoordinates = slurper.parseText(jsonNode.asText())
            final String playerId = ctx.request.headers.get("playerId")
            final Game game = ctx.get(Game)

            final Player player = game.playerBy(playerId)
            if (player.placeBoat(boatCoordinates)) {
                ctx.response.send(toJson(player.field))
            } else {
                ctx.response.status(409).send()
            }
        }


    }
}
