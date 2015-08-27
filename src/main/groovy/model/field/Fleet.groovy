package model.field

import model.ship.ShipState
import model.ship.ShipType

class Fleet {
    //Size : Count
    Map<Integer, Integer> availableShips = [5: 1, 4: 2, 3: 3, 2: 4]
    int shipCounter = 30

    List<ShipState> availableShipsList() {
//TODO
    }

    boolean hasShipsLeft() {
        shipCounter > 0
    }

    boolean isAvailable(Ship ship){
        availableShips[ship.size]>0
    }

    void placeShip(Ship ship){
        availableShips[ship.size]=availableShips[ship.size]-1
    }

    boolean isAllShipsDeployed() {
        availableShips.values().inject(0) { result, shipCount ->
            result + shipCount
        } == 0
    }
}
