package model.ship

import groovy.transform.Immutable

@Immutable
class Ship {
    ShipType type;
    int size;
    int count;
}
