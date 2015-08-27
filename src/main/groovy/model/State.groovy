package model

import groovy.transform.Immutable
import model.ship.ShipState

@Immutable
class State {
    String playerId
    boolean myTurn
    GamePhase gamePhase
    List<ShipState> availableShips
    //starts with 0
    boolean isVictory = false
    List<Position> field
    List<Position> oppositeField
    int undamagedShips
}
