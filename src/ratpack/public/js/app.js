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
            _isInPlacement,
            _connect,
            _updateField,
            _findPosition,
            _placeBoat,
            _getCellContent;

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
            return (_playerId == '' && _gamePhase == '');
        };

        _isInPlacement = function() {
            return _gamePhase == 'PLACEMENT';
        };

        _getCellContent = function(cell) {
            if(cell.pos == 0) {
                return cell.state;
            }
            return "";
        }

        _connect = function() {
            if (_isInConnection()) {
                var req = {
                        method: 'POST',
                        url: 'http://localhost:5050/connect',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        data: {test: 'test'}
                    };

                $http.post(req.url, req.data, req).then(
                    function (result) {
                        _playerId = result.data.playerId;
                        _gamePhase = result.data.gamePhase;
                        _myTurn = result.data.myTurn;
                        _availableShips = result.data.availableShips;
                        _field = _updateField(result.data.field);
                        _isVictory = result.data.isVictory;
                        _undamagedShips = result.data.undamagedShips;
                    }).catch(function (error) {
                        console.log(error);
                    });
            } else {
                console.log("You are already connected!")
            }
        };

        _updateField = function(positions) {
            var rows = [[{"pos": 0, "state": '0', "coordinate": ''},
                         {"pos": 0, "state": '1', "coordinate": ''},
                         {"pos": 0, "state": '2', "coordinate": ''},
                         {"pos": 0, "state": '3', "coordinate": ''},
                         {"pos": 0, "state": '4', "coordinate": ''},
                         {"pos": 0, "state": '5', "coordinate": ''},
                         {"pos": 0, "state": '6', "coordinate": ''},
                         {"pos": 0, "state": '7', "coordinate": ''},
                         {"pos": 0, "state": '8', "coordinate": ''},
                         {"pos": 0, "state": '9', "coordinate": ''},
                         {"pos": 0, "state": '10', "coordinate": ''}]];
            var rowChar = ['A','B','C','D','E','F','G','H','I','J'];
            for (var x = 1; x < 11; x++) {

                var start, col;
                start = (x-1) * 10;
                col = [{"pos":0, "state": rowChar[x-1], "coordinate": ''}];

                for (var i = 1; i < 11; i++) {
                    var found, p = start + i;
                    found = _findPosition(positions, p-1);
                    if (found.length == 1) {
                        col[i] = {"pos": found[0].pos+1, "state": found[0].state, "coordinate": rowChar[x-1]+i};
                    } else {
                        col[i] = {"pos": start + i, "state": "WATER", "coordinate": rowChar[x-1]+i};
                    }
                }
                rows[x] = col;
            }
            return rows;
        };

        _findPosition = function(matrix, position) {
            var result = matrix.filter(function (pos) {
                if (pos.pos == position) {
                    return true;
                }
                return false;
            });
            return result;
        };

        _placeBoat = function(bow,stern) {
            console.log("Bug: "+bow+" Heck: "+stern);
            var req = {
                method: 'POST',
                url: 'http://localhost:5050/ship',
                headers: {
                    'Content-Type': 'application/json',
                    'playerId': _playerId
                },
                data: {"bow": {"x":bow.charAt(0),"y":bow.charAt(1)},
                       "stern": {"x":stern.charAt(0),"y":stern.charAt(1)}}
            };

            $http.post(req.url, req.data, req).then(
                function (result) {
                    console.log("Ships: ",result.data.availableShips);
                    console.log("Matrix: ",result.data.field);
                    _availableShips = result.data.availableShips;
                    _field = _updateField(result.data.field);
                }).catch(function (error) {
                    console.log(error);
                });
        };

        return {
            fire: _fire,
            isOpenToFire: _isOpenToFire,
            isInConnection: _isInConnection,
            isInPlacement: _isInPlacement,
            connect: _connect,
            updateField: _updateField,
            placeBoat: _placeBoat,
            getCellContent: _getCellContent,
            playerId: function() { return _playerId; },
            gamePhase: function() { return _gamePhase; },
            field: function() { return _field; },
            ships: function() { return _availableShips; }
        }

    })
    .controller('BoatsCtrl', function($scope, $http, Player) {
        $scope.player = Player;
        $scope.placeBoat = function() {
            $scope.player.placeBoat($scope.bow, $scope.stern);
            $scope.bow = '';
            $scope.stern = '';
        }
    });
