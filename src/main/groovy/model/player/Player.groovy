package model.player

import groovy.util.logging.Slf4j
import model.FieldState
import model.Position
import model.field.Fleet
import model.field.OwnField
import model.field.Ship
import model.ship.ShipState

import static model.Position.calculatePosition

@Slf4j
class Player {
    String name
    PlayerId id

    OwnField ownField = new OwnField()
    Fleet fleet = new Fleet()

    // Field of opposite player
    Map<Integer, FieldState> oppositeField = [:]

    Player(String name, PlayerId id){
        this.name = name
        this.id = id
    }

    List<Position> positionListFor(Map<Integer, FieldState> matrix) {
        matrix.collect { key, value ->
            new Position(pos: key, state: value)
        }
    }


    def placeBoat(Map<String, Map<String, String>> boatCoordinates) {
        Ship ship = new Ship(boatCoordinates)
        if (fleet.isAvailable(ship)) {
            if (ownField.placeBoat(ship)) {
                fleet.placeShip(ship)
            }
        }
    }

    def setShotResult(Map<String, String> fireCoordinate, FieldState fieldState) {
        final int pos = calculatePosition(fireCoordinate)

        oppositeField.put(pos, fieldState)
    }

    boolean isShipsLeft(){
        fleet.hasShipsLeft()
    }

    List<ShipState> getAvailableShips(){
        fleet.availableShipsList()
    }
}
