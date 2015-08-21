package model

import static model.FieldState.*

class Player {
    String name
    String id
    //Players own field
    //a1 is 0
    Map<Integer, FieldState> field = [:]
    // Field of opposite player
    Map<Integer, FieldState> oppositeField = [:]
    //Size : Count
    Map<Integer, Integer> availableShips = [5: 1, 4: 2, 3: 3, 2: 4]
    int shipCounter = 30

    Map<Integer, FieldState> placeBoat(Map<String, Map<String, String>> boatCoordinates) {
        final bow = calculatePosition(boatCoordinates.bow)
        final stern = calculatePosition(boatCoordinates.stern)

        if (isValidPos(bow, stern)) {
            final Collection<Integer> ship = getShipPositions(bow, stern)

            if (canShipBePlaced(ship) && isValidShipSize(ship)) {
                ship.each {
                    field.put(it, SHIP)
                }

                availableShips[ship.size()] = availableShips[ship.size()] - 1

                return field
            }
        }

        return null
    }

    boolean hasShipsLeft(){
        shipCounter>0
    }

    List<Ship> availableShipsList() {
        availableShips.collect { key, value ->
            new Ship(
                type: ShipType.typeOfSize(key.toInteger()),
                size: key.toInteger(),
                count: value)
        }
    }

    List<Position> positionListFor(Map<Integer, FieldState> matrix) {
        matrix.collect { key, value ->
            new Position(key, value)
        }
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
        ship.every { field[it] == null }
        //erweitern um ist benachbart zu einem schiff
    }

    private isValidShipSize(Collection<Integer> ship) {
        ship.size() >= 2 && ship.size() <= 5 && availableShips[ship.size()] > 0
    }

    private int calculatePosition(Map<String, String> coordinate) {
        final String x = coordinate.x.toLowerCase()
        final String y = coordinate.y.toLowerCase()

        final String row = "abcdefghij"
        row.indexOf(x) * 10 + y.toInteger() - 1
    }

    FieldState shotAt(Map<String, String> shotCoordinate) {

        final int pos = calculatePosition(shotCoordinate)

        switch (field.getOrDefault(pos, WATER)) {
            case SHIP:
                //als hit markieren
                shipCounter--
                field[pos] = HIT
                makeSunk(pos)
                break
            case WATER:
                field[pos] = MISS;
                break
        }
        return field[pos]
    }


    void makeSunk(int pos) {
        Set<Integer> startSet = new HashSet<>()
        startSet.add(pos)
        Set<Integer> completeShip = collectAllNonWaterNeighbours(pos, startSet)

        if (completeShip.every { field[it] == HIT }) {
            completeShip.each { field[it] = SUNK }
        }
    }

    Set<Integer> collectAllNonWaterNeighbours(int position, Set<Integer> knownNeighbours) {
        Set<Integer> neighbors = new Neighbors(position).all

        Set<Integer> newfoundNonWaterNeighbours = neighbors.findAll {
            (field[it] == HIT || field[it] == SHIP) && !knownNeighbours.contains(it)
        }

        newfoundNonWaterNeighbours.each {
            knownNeighbours.add(it)
            knownNeighbours.addAll(collectAllNonWaterNeighbours(it, knownNeighbours))
        }

        knownNeighbours
    }

    def setShotResult(Map<String, String> fireCoordinate, FieldState fieldState) {
        final int pos = calculatePosition(fireCoordinate)

        oppositeField.put(pos, fieldState)
    }

    boolean allShipsPlaced() {
        availableShips.values().inject(0) { result, shipCount ->
            result + shipCount
        } == 0
    }


}
