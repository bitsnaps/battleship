package model.field

import model.FieldState
import model.Neighbors

import static model.FieldState.*
import static model.Position.calculatePosition

class OwnField {
//a1 is 0
    Map<Integer, FieldState> field = [:]


    boolean placeBoat(Ship ship) {
        boolean success = false
        if (isValidPos(ship)) {
            if (canShipBePlaced(ship)) {

                ship.fields.each {
                    field.put(it, SHIP)
                }
                success = true
            }
        }
        success
    }


    FieldState shotAt(Map<String, String> shotCoordinate, Fleet fleet) {

        final int pos = calculatePosition(shotCoordinate)

        switch (get(pos)) {
            case SHIP:
                //als hit markieren
                fleet.shipCounter = fleet.shipCounter - 1
                field[pos] = HIT
                makeSunk(pos)
                break
            case WATER:
                field[pos] = MISS;
                break
        }
        return field[pos]
    }

    private void makeSunk(int pos) {
        Set<Integer> startSet = new HashSet<>()
        startSet.add(pos)
        Set<Integer> completeShip = allNonWaterNeighbours(pos, startSet)

        if (completeShip.every { field[it] == HIT }) {
            completeShip.each { field[it] = SUNK }
        }
    }

    /**
     * Find all neighboring positions to a given position
     */
    private Set<Integer> allNonWaterNeighbours(int position, Set<Integer> knownNeighbours) {
        Set<Integer> neighbors = new Neighbors(position).all

        Set<Integer> newfoundNonWaterNeighbours = neighbors.findAll {
            (field[it] == HIT || field[it] == SHIP) && !knownNeighbours.contains(it)
        }

        newfoundNonWaterNeighbours.each {
            knownNeighbours.add(it)
            knownNeighbours.addAll(allNonWaterNeighbours(it, knownNeighbours))
        }

        knownNeighbours
    }

    private FieldState get(Integer position) {
        field.getOrDefault(position, WATER)
    }

    private boolean isValidPos(Ship ship) {
        final Integer bow = ship.bow
        final Integer stern = ship.stern
        bow >= 0 &&
                stern > 0 &&
                bow < stern &&
                bow < 99 &&
                stern <= 99 &&
                (ship.horizontal || ship.vertical)
    }


    private boolean canShipBePlaced(Ship ship) {
        ship.fields.every { field[it] == null }
    }
}
