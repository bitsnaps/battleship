package model.player

class Players {
    final List<Player> players = []
    List<PlayerId> defaultIds = [new PlayerId('1xyz'), new PlayerId('2abc')]
    List<String> playerNames = ['Player 1', 'Player 2']

    PlayerId getFirst(){
        players[0].id
    }

    boolean allShipsArePlaced() {
        full() && players.every { it.fleet.allShipsDeployed }
    }

    Optional<PlayerId> addPlayer() {

        full() ? Optional.empty() : Optional.of(
                {
                    PlayerId playerId = defaultIds[players.size()]
                    String playerName = playerNames[players.size()]
                    players.add(new Player(playerName, playerId))
                    players.last().id
                }()
        )
    }

    Player playerBy(PlayerId playerId) {
        players.find { it.id == playerId }
    }

    boolean full() {
        players.size() == 2
    }

    Player oppositePlayer(PlayerId id) {
        players.find { it.id != id }
    }
}
