package model

import groovy.transform.ToString
import model.player.Player
import model.player.PlayerId
import model.player.Players

@ToString
class Game {
    PlayerId currentPlayerId = null
    Players players = new Players()
    GamePhase gamePhase = GamePhase.PLACEMENT

    State shootAt(Map<String, String> fireCoordinate, PlayerId shooterId) {
        final Player opponent = players.oppositePlayer(shooterId)
        final Player shooter = players.playerBy(shooterId)

        final FieldState state = opponent.ownField.shotAt(fireCoordinate, opponent.fleet)
        shooter.setShotResult(fireCoordinate, state)

        if (!opponent.shipsLeft) {
            gamePhase = GamePhase.FINISHED
        }

        currentPlayerId = opponent.id

        getState(shooterId)
    }

    //TODO: place a ship,
    //if all ships are placed initialize shootuot phase (set gamePhase and currentPlayerId
    State placeBoat(Map<String, Map<String, String>> boatCoordinates, PlayerId playerId) {
    }


    boolean myTurn(playerId) {
        currentPlayerId == null ? false : currentPlayerId == playerId
    }

    Optional<PlayerId> addPlayer() {
        players.addPlayer()
    }

    State getState(PlayerId playerId) {
        //TODO return the game state of corresponding player
    }
}
