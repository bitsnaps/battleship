package handlers

import com.fasterxml.jackson.databind.JsonNode
import groovy.json.JsonSlurper
import model.FieldState
import model.Game
import ratpack.handling.Context
import ratpack.handling.Handler

import static groovy.json.JsonOutput.toJson

class BoatHandler implements Handler {

    private final JsonSlurper slurper = new JsonSlurper()

    @Override
    void handle(Context ctx) throws Exception {
        ctx.parse(JsonNode).onError {
            ctx.response.status(500).send()
        } then { JsonNode jsonNode ->

            final Map<String, Map<String, String>> boatCoordinates = slurper.parseText(jsonNode.toString())
            final String playerId = ctx.request.headers.get("playerId")
            final Game game = ctx.get(Game)

            final Map<Integer, FieldState> field = game.placeBoat(boatCoordinates, playerId)
            if (field) {
                ctx.response.send(toJson(field))
            } else {
                ctx.response.status(409).send()
            }
        }
    }
}
