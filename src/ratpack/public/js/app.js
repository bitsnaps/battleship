'use strict';

angular.module('battleshipApp', [])
    .factory('Player', function($http) {
        var playerId = '',
            myTurn = false,
            gamePhase = '',
            availableShips = [],
            field = [],
            isVictory = false,
            undamagedShips = 30;

        return {
            getShips: function() {
                console.log("getShips called");
                return availableShips;
            },
            getField: function() {
                console.log("getField called");
                return field;
            },
            fire: function(coordinate) {
                console.log('Try to shoot on a ship');
                if(playerId != '' && coordinate != '') {
                    var req = {
                        method: 'PUT',
                        url: 'http://localhost:5050/shoot',
                        headers: {
                            'Content-Type': 'application/json',
                            'playerId' : playerId
                        },
                        data: { 'x': 'A', 'y' : '1' }
                    };
                    $http.post(req).then(function(result){

                    },function(error){

                    })
                }
                console.log("Coordinate: ",coordinate);
            },
            getRows: function() {
                console.log("getRows called");
                return [0,1,2,3,4,5,6,7,8,9];
            },
            getRow: function(index) {
                console.log("getRow called");
                var start, end, row;
                start = index*10;
                row = [];

                for (var i = 0; i < 10; i++) {
                    var found, pos = start + i;
                    found = field.forEach(function(currentValue) {
                        if(currentValue.pos === pos) {
                            return currentValue;
                        }
                    });
                    if (found) {
                        row[i] = found
                    } else {
                        row[i] = {"pos": start + i + 1, "state": "W"}
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
            },
            connect: function() {
                console.log('Try to connect');
                var req = {
                    method: 'POST',
                    url: 'http://localhost:5050/connect',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    data: { test: 'test' }
                };
                console.log("Req-Config: ",req);

                $http.post(req).then(
                    function(result) {
                        console.log('Start to set data');
                        playerId = result.data.playerId;
                        gamePhase = result.data.gamePhase;
                        myTurn = result.data.myTurn;
                        availableShips = result.data.availableShips;
                        field = result.data.field;
                        isVictory = result.data.isVictory;
                        undamagedShips = result.data.undamagedShips;
                        console.log('Data already set!');
                        //How Angular knows that "model" data are set ?
                    }, function(error) {
                        console.log(error);
                    }
                );
            }
        }

    })
    .controller('BoatsCtrl', function($scope, Player) {
        $scope.player = Player;
    });
