'use strict';

angular.module('battleshipApp', [])
    .factory('Player', function($http) {
        var player = {},ready = false;

        $http.get('js/player.json').then(
            function(result) {
                player = result.data;
                ready = true;
            }
        );

        var findPosition = function(pos) {
            var found;
            player.field.forEach(function(currentValue) {
                if(currentValue.pos === pos) {
                    found = currentValue;
                }
            });
            return found;
        }

        return {
            getName: function() {
                return player.name;
            },
            getShips: function() {
                return player.ships;
            },
            getField: function() {
                return player.field;
            },
            fire: function(coordinate) {
                console.log(coordinate)
            },
            getRows: function() {
                return [0,1,2,3,4,5,6,7,8,9];
            },
            getRow: function(index) {
                var start, end, row;
                start = index*10;
                row = [];

                if(ready) {
                    for (var i = 0; i < 10; i++) {
                        var found, pos = start + i;
                        found = findPosition(pos);
                        if (found) {
                            row[i] = found
                        } else {
                            row[i] = {"pos": start + i + 1, "state": "W"}
                        }
                    }
                }
                return row;
            },
            isWater: function(cell) {
                return cell.state == 'W';
            },
            isShip: function(cell) {
                return cell.state == 'S';
            },
            isHit: function(cell) {
                return cell.state == 'X';
            }
        }

    })
    .controller('BoatsCtrl', function($scope, Player) {
        $scope.player = Player;
    });
