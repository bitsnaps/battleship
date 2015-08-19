'use strict';

angular.module('battleshipApp', [])
    .factory('Player', function($http) {
        var playerId = '',
            myTurn = false,
            gamePhase = '',
            availableShips = [],
            field = [],
            oppositeField = [],
            rows = [0,1,2,3,4,5,6,7,8,9],
            isVictory = false,
            undamagedShips = 30;

        return {
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
            isOpenToFire: function() {
                return gamePhase == 'SHOOTOUT';
            },
            isInConnection: function() {
                console.log("PlayerId: [" + playerId + "], gamePhase: [" + gamePhase + "]");
                return (playerId == '' && gamePhase == '');
            },
            connect: function() {
                if (this.isInConnection()) {
                    console.log('Try to connect');
                    var that = this,
                        req = {
                        method: 'POST',
                        url: 'http://localhost:5050/connect',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        data: {test: 'test'}
                    };
                    console.log("Req-Config: ", req);

                    $http.post(req.url, req.data, req).then(
                        function (result) {
                            console.log('Start to set data: ', result.data);
                            that.playerId = result.data.playerId;
                            that.gamePhase = result.data.gamePhase;
                            that.myTurn = result.data.myTurn;
                            that.availableShips = result.data.availableShips;
                            that.field = that.updateField(result.data.field);
                            that.isVictory = result.data.isVictory;
                            that.undamagedShips = result.data.undamagedShips;
                            console.log('Data already set: ', that.field);
                        }).catch(function (error) {
                            console.log(error);
                        });
                } else {
                    console.log("You are already connected!")
                }
            },
            updateField: function(positions) {
                var rows = [];
                for (var x = 0; x < 10; x++) {

                    var start, col;
                    start = x * 10;
                    col = [];

                    for (var i = 0; i < 10; i++) {
                        //var found, pos = start + i;
                        //found = field.forEach(function (currentValue) {
                        //    if (currentValue.pos === pos) {
                        //        return currentValue;
                        //    }
                        //});
                        //if (found) {
                        //    row[i] = found
                        //} else {
                            col[i] = {"pos": start + i + 1, "state": "W"}
                        //}
                    }
                    rows[x] = col;
                }
                console.log('Finish update field.');
                return rows;
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

        }

    })
    .controller('BoatsCtrl', function($scope, $http, Player) {
        $scope.player = Player;
    });
