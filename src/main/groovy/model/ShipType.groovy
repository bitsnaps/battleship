package model

enum ShipType {
    SCHLACHTSCHIFF(5), KREUZER(4), UBOOT(3), SCHNELLBOOT(2)

    private final int size

    private ShipType(int size) {
        this.size = size;
    }

    static ShipType typeOfSize(int size) {
        switch (size) {
            case SCHLACHTSCHIFF.size: return SCHLACHTSCHIFF
            case KREUZER.size: return KREUZER
            case UBOOT.size: return UBOOT
            case SCHNELLBOOT.size: return SCHNELLBOOT
        }
    }

}
