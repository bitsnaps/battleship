package model.ship

import groovy.transform.Immutable

@Immutable
class ShipState {
    ShipType type;
    int size;
    int count;
}
