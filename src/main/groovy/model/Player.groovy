package model

import static model.FieldState.*

/**
 * Created by sven on 11.08.15.
 */
class Player {
    String name
    String id
    Map<Integer, FieldState> field = [:]
    //Size : Count
    Map<Integer, Integer> ships = [5:1, 4:2, 3:3, 2:4]

    boolean placeBoat(Map<String, Map<String, String>> boatCoordinates) {
        final bow = calculatePosition(boatCoordinates.bow)
        final stern = calculatePosition(boatCoordinates.stern)

        if (isValidPos(bow, stern)) {
            final Collection<Integer> ship = getShipPositions(bow, stern)

            if (canShipBePlaced(ship) && isValidShipSize(ship)) {
                ship.each {
                    field.put(it, SHIP)
                }

                ships[ship.size()] = ships[ship.size()] - 1

                return true
            }
        }

        false
    }

    private Collection<Integer> getShipPositions(int bow, int stern) {

        final boolean horizontal = isHorizontal(bow, stern)

        (bow..stern).findAll {
            horizontal || (it - bow % 10 == 0) // vertical
        }
    }

    private boolean isValidPos(int bow, int stern) {
        bow >= 0 &&
                stern > 0 &&
                bow < stern &&
                bow < 99 &&
                stern <= 99 &&
                (isHorizontal(bow, stern) || isVertical(bow, stern))
    }

    private boolean isHorizontal(int bow, int stern) {
        ((bow / 10) as Integer) == ((stern / 10) as Integer)
    }

    private boolean isVertical(int bow, int stern) {
        (bow % 10 == stern % 10)
    }

    private boolean canShipBePlaced(Collection<Integer> ship) {
        Collection col = ship.find {
            field[it] != null
            //Erweitern um Schiffe dürfen sich nicht berühren
        }

        col.isEmpty()
    }

    private isValidShipSize(Collection<Integer> ship){
        ship.size() >= 2 && ship.size() <= 5 && ships[ship.size()] > 0
    }

    private int calculatePosition(Map<String, String> coordinate) {
        final String x = coordinate.x
        final String y = coordinate.y

        final String row = "abcdefghij"
        row.indexOf(y) * 10 + x.toInteger()
    }

    FieldState shotAt(Map<String, String> shotCoordinate) {

        final int pos = calculatePosition(shotCoordinate)

        switch (field.getOrDefault(pos, WATER)) {
            case SHIP: return HIT //prüfen ob Schiff gesunken
            case WATER: field[pos] = MISS; return MISS
            default: return field[pos]
        }
    }
}
