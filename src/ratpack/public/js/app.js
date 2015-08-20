'use strict';

angular.module('battleshipApp', [])
    .factory('Player', function($http) {
        var _playerId = '',
            _myTurn = false,
            _gamePhase = '',
            _availableShips = [],
            _field = [],
            _oppositeField = [],
            _isVictory = false,
            _undamagedShips = 30,
            _fire,
            _isOpenToFire,
            _isInConnection,
            _connect,
            _updateField,
            _isWater,
            _isShip,
            _isHit;

        _fire = function(coordinate) {
            console.log('Try to shoot on a ship');
            if(_playerId != '' && coordinate != '') {
                var req = {
                    method: 'PUT',
                    url: 'http://localhost:5050/shoot',
                    headers: {
                        'Content-Type': 'application/json',
                        'playerId' : _playerId
                    },
                    data: { 'x': 'A', 'y' : '1' }
                };
                $http.post(req).then(function(result){

                },function(error){

                })
            }
            console.log("Coordinate: ",coordinate);
        };

        _isOpenToFire = function() {
            return _gamePhase == 'SHOOTOUT';
        };

        _isInConnection = function() {
            console.log("PlayerId: [" + _playerId + "], gamePhase: [" + _gamePhase + "]");
            return (_playerId == '' && _gamePhase == '');
        };

        _connect = function() {
            if (_isInConnection()) {
                console.log('Try to connect');
                var req = {
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
                        _playerId = result.data.playerId;
                        _gamePhase = result.data.gamePhase;
                        _myTurn = result.data.myTurn;
                        _availableShips = result.data.availableShips;
                        _field = _updateField(result.data.field);
                        _isVictory = result.data.isVictory;
                        _undamagedShips = result.data.undamagedShips;
                        console.log('Data already set: ', _field);
                    }).catch(function (error) {
                        console.log(error);
                    });
            } else {
                console.log("You are already connected!")
            }
        };

        _updateField = function(positions) {
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
        };

        _isWater = function(cell) {
            return cell.state == 'W';
        };

        _isShip = function(cell) {
            return cell.state == 'S';
        };

        _isHit = function(cell) {
            return cell.state == 'X';
        };

        return {
            fire: _fire,
            isOpenToFire: _isOpenToFire,
            isInConnection: _isInConnection,
            connect: _connect,
            updateField: _updateField,
            isWater: _isWater,
            isShip: _isShip,
            isHit: _isHit,
            playerId: function() { return _playerId; },
            gamePhase: function() { return _gamePhase; },
            field: function() { return _field; },
            ships: function() { return _availableShips; }
        }

    })
    .controller('BoatsCtrl', function($scope, $http, Player) {
        $scope.player = Player;
    });
