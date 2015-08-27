package handlers

import model.player.PlayerId
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.registry.Registry

class PlayerIdHandler implements Handler {

    @Override
    void handle(Context ctx) throws Exception {
        final String playerId = ctx.request.headers.get('playerId')
        playerId ? ctx.next(Registry.single(new PlayerId(playerId))) : ctx.response.status(401)
    }
}
