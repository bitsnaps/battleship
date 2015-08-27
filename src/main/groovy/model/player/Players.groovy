package model.player

import model.player.Player
import model.player.PlayerId

class Players {
    final List<Player> game = []
    List<PlayerId> defaultIds = [new PlayerId('1xyz'), new PlayerId('2abc')]
    List<String> playerNames = ['Player 1', 'Player 2']

    PlayerId getFirst(){
        game[0].id
    }

    boolean allShipsArePlaced() {
        full() && game.every { it.allShipsPlaced() }
    }

    Optional<PlayerId> addPlayer() {

        full() ? Optional.empty() : Optional.of(
                {
                    PlayerId playerId = defaultIds[game.size()]
                    String playerName = playerNames[game.size()]
                    game.add(new Player(name: playerName, id: playerId, field: [:]))
                    game.last().id
                }()
        )
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
}
