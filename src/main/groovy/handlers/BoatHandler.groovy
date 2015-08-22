package handlers

import com.fasterxml.jackson.databind.JsonNode
import groovy.json.JsonSlurper
import model.Game
import model.State
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

            State state = game.placeBoat(boatCoordinates, playerId)
            if (state) {
                ctx.response.send(toJson(state))
            } else {
                ctx.response.status(409).send(toJson(state))
            }
        }
    }
}
