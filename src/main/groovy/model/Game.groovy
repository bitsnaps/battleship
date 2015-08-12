package model

import groovy.transform.ToString

/**
 * Created by sven on 11.08.15.
 */
@ToString
class Game {
    //Erklärung Notation Map - Collectionhandling
    Map<String,Player> game = [:]

    Player playerBy(String id) {
        game.find { it.id == id }
    }

    Player oppositePlayer(String id) {
        game.find { it.id != id }
    }

    FieldState shootAt(Map<String,String> fireCoordinate, String shooterId) {
        final Player opponent = oppositePlayer(shooterId)
        final Player shooter = playerBy(shooterId)

        opponent.shotAt(fireCoordinate)
    }
}
