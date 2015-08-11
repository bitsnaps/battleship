package handlers

import groovy.json.JsonSlurper
import model.Game
import model.Player
import ratpack.handling.Context
import ratpack.handling.Handler

import static groovy.json.JsonOutput.toJson

/**
 * Created by sven on 11.08.15.
 */
class BoatHandler implements Handler {

    private final JsonSlurper slurper = new JsonSlurper()

    @Override
    void handle(Context ctx) throws Exception {
        final Map<String, Map<String,String>> boatCoordinates = slurper.parseText( ctx.request.body.text )
        final String playerId = ctx.request.headers.get("playerId")
        final Game game = ctx.get(Game)

        final Player player = game.playerBy(playerId)
        if(player.placeBoat(boatCoordinates)) {
            ctx.response.send(toJson(player.field))
        } else {
            ctx.response.status(409).send()
        }
    }
}
