package model

import groovy.transform.Immutable

@Immutable
class State {
    String playerId
    boolean myTurn
    GamePhase gamePhase
    Map<Integer, Integer> availableShips
    //starts with 0
    boolean isVictory = false
    Map<Integer, FieldState> field
    int undamagedShips
}