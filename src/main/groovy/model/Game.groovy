package model

import groovy.transform.ToString

@ToString
class Game {
    String currentPlayerId = null
    //Erklärung Notation Map - Collectionhandling
    final List<Player> game = []

    GamePhase gamePhase = GamePhase.PLACEMENT

    Player playerBy(String id) {
        game.find { it.id == id }
    }

    boolean full() {
        game.size() == 2
    }

    Player oppositePlayer(String id) {
        game.find { it.id != id }
    }

    FieldState shootAt(Map<String, String> fireCoordinate, String shooterId) {
        final Player opponent = oppositePlayer(shooterId)
        final Player shooter = playerBy(shooterId)

        final FieldState state = opponent.shotAt(fireCoordinate)
        shooter.setShotResult(fireCoordinate, state)
        currentPlayerId = opponent.id

        state
    }

    Map<Integer, FieldState> placeBoat(Map<String, Map<String, String>> boatCoordinates, String playerId) {
        final Player player = playerBy(playerId)
        if (player) {
            final map = player.placeBoat(boatCoordinates)

            if (allShipsArePlaced()) {
                //Now let the game start
                currentPlayerId = game.player1.id
            }

            map
        } else {
            null
        }
    }

    boolean allShipsArePlaced() {
        game.values().size() == 2 &&
                game.values().inject(true) { result, pl ->
                    result && pl.allShipsPlaced()
                }
    }

    boolean myTurn(playerId) {
        currentPlayerId == null ? false : currentPlayerId == playerId
    }

    Optional<String> addPlayer() {
        List<String> defaultIds = ['1xyz', '2abc']
        List<String> playerNames = ['Player 1', 'Player 2']


        full() ? Optional.empty() : Optional.of(
                {
                    String playerId = defaultIds[game.size()]
                    String playerName = playerNames[game.size()]
                    game.add(new Player(name: playerName, id: playerId, field: [:]))
                    game.last().id
                }()
        )
    }
}
