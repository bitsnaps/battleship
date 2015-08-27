package model

import groovy.transform.Immutable

@Immutable
class PlayerId {
    String id;

    @Override
    String toString(){
        id
    }
}
