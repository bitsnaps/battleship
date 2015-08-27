package model

import groovy.transform.ToString

@ToString
class Game {
    PlayerId currentPlayerId = null
    //Erklärung Notation Map - Collectionhandling
    final List<Player> game = []

    GamePhase gamePhase = GamePhase.PLACEMENT

    Player playerBy(String id) {
        playerBy(new PlayerId(id))
    }

    Player playerBy(PlayerId playerId) {
        game.find { it.id == playerId }
    }

    boolean full() {
        game.size() == 2
    }

    Player oppositePlayer(PlayerId id) {
        game.find { it.id != id }
    }

    State shootAt(Map<String, String> fireCoordinate, PlayerId shooterId) {
        final Player opponent = oppositePlayer(shooterId)
        final Player shooter = playerBy(shooterId)

        final FieldState state = opponent.shotAt(fireCoordinate)
        shooter.setShotResult(fireCoordinate, state)

        if(!opponent.hasShipsLeft()){
            gamePhase= GamePhase.FINISHED
        }

        currentPlayerId = opponent.id

        getState(shooterId)
    }

    State placeBoat(Map<String, Map<String, String>> boatCoordinates, PlayerId playerId) {
        final Player player = playerBy(playerId)
        if (player) {
            final map = player.placeBoat(boatCoordinates)

            if (allShipsArePlaced()) {
                //Now let the game start
                gamePhase=GamePhase.SHOOTOUT
                currentPlayerId = game.first().id
            }

            getState(playerId)
        } else {
            null
        }
    }

    boolean allShipsArePlaced() {
        game.size() == 2 && game.every { it.allShipsPlaced() }
    }

    boolean myTurn(playerId) {
        currentPlayerId == null ? false : currentPlayerId == playerId
    }

    Optional<PlayerId> addPlayer() {
        List<PlayerId> defaultIds = [new PlayerId('1xyz'), new PlayerId('2abc')]
        List<String> playerNames = ['Player 1', 'Player 2']


        full() ? Optional.empty() : Optional.of(
                {
                    PlayerId playerId = defaultIds[game.size()]
                    String playerName = playerNames[game.size()]
                    game.add(new Player(name: playerName, id: playerId, field: [:]))
                    game.last().id
                }()
        )
    }

    State getState(PlayerId playerId){
        Player player = playerBy(playerId)
        new State(
                playerId:       playerId,
                myTurn:         myTurn(playerId),
                gamePhase:      gamePhase,
                availableShips: player.availableShipsList(),
                field:          player.positionListFor(player.field),
                isVictory:      player.hasShipsLeft(),
                oppositeField:  player.positionListFor(player.oppositeField),
                undamagedShips: player.shipCounter
        )
    }
}
