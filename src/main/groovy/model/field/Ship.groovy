package model.field

import static model.Position.calculatePosition


class Ship {
    Collection<Integer> fields = new HashSet<Integer>()

    Ship(Map<String, Map<String, String>> boatCoordinates) {
        fields = getShipPositions(
                calculatePosition(boatCoordinates.bow),
                calculatePosition(boatCoordinates.stern)
        )
    }


    Integer getBow() {
        fields.min()
    }

    Integer getStern() {
        fields.max()
    }


    boolean isVertical() {
        (bow % 10 == stern % 10)
    }

    boolean isHorizontal() {
        isHorizontal(bow, stern)
    }

    public static boolean isHorizontal(int bow, int stern) {
        ((bow / 10) as Integer) == ((stern / 10) as Integer)

    }

    int getSize() {
        fields.size()
    }


    private Collection<Integer> getShipPositions(int bow, int stern) {
        final boolean horizontal = isHorizontal(bow, stern)

        (bow..stern).findAll {
            final boolean vertical = ((bow % 10) == (it % 10))

            horizontal || vertical
        }
    }


}
