package model

import groovy.transform.Immutable

@Immutable
class Position {
    int pos;
    FieldState state;

    static int calculatePosition(Map<String, String> coordinate) {
        final String x = coordinate.x.toLowerCase()
        final String y = coordinate.y.toLowerCase()

        final String row = "abcdefghij"
        row.indexOf(x) * 10 + y.toInteger() - 1
    }
}
