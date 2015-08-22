package model

import groovy.transform.Immutable

@Immutable
class State {
    String playerId
    boolean myTurn
    GamePhase gamePhase
    List<Ship> availableShips
    //starts with 0
    boolean isVictory = false
    List<Position> field
    List<Position> oppositeField
    int undamagedShips
}
