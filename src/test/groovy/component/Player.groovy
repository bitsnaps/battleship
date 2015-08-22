package component

import groovy.json.JsonSlurper
import groovy.transform.TailRecursive
import model.FieldState
import ratpack.http.client.ReceivedResponse
import ratpack.test.http.TestHttpClient

import static groovy.json.JsonOutput.toJson


class Player {
    private final static String CONNECT = 'connect'
    private final static String SHIP = 'ship'
    private final static String POLLING = 'myturn'
    private final static String FIRE = 'shoot'
    int victoryCounter = 30
    int shotCount = 0
    List<Map<String, String>> alreadyShelled = []

    private final static Random random = new Random()
    private final static JsonSlurper jsonSlurper = new JsonSlurper()
    String playerId

    TestHttpClient client

    ReceivedResponse lastShotResponse = null
    /**
     * Safely connects the player to the game.
     * Asserts that player has not previously connected to the game, that the return code is 201 and a playerId is supplied
     * @return Map containing the response body parsed from JSON
     */
    Map connect() {
        assert !playerId

        ReceivedResponse response = client.post(CONNECT)

        assert response.statusCode == 201

        Map result = jsonSlurper.parse(response.body.inputStream)
        playerId = result.playerId

        assert playerId

        result
    }

    /**
     * Polls the current game state resource
     * @return
     */
    Map poll() {
        assert playerId
        client.requestSpec { requestSpec ->
            requestSpec.headers.set('playerId', playerId)
        }
        ReceivedResponse response = client.get(POLLING)
        assert response.statusCode == 200

        Map result = jsonSlurper.parse(response.body.inputStream)
        assert result.myTurn != null
        assert result.gamePhase != null
        assert result.availableShips != null
        result
    }

    /**
     * Deploys complete Fleet in a default pattern
     */
    def deployFleet() {
        [
                [bow: 'a1', stern: 'a5'],//5
                [bow: 'c1', stern: 'c4'],//4
                [bow: 'e1', stern: 'e4'],//4
                [bow: 'g1', stern: 'g3'],//3
                [bow: 'i1', stern: 'i3'],//3
                [bow: 'a7', stern: 'a9'],//3
                [bow: 'c8', stern: 'c9'],//2
                [bow: 'e8', stern: 'e9'],//2
                [bow: 'g8', stern: 'g9'],//2
                [bow: 'i8', stern: 'i9']//2
        ].each {
            ReceivedResponse response = place(it.bow, it.stern)
            assert response.statusCode == 200
        }
    }

    boolean isActive() {
        Boolean.valueOf(poll()['myTurn'])
    }

    FieldState shootRandomly() {
        Map<String, String> unshelledCoordinates = findUnshelledCoordinates(poll())
        Optional<Map> shellingResult = shootAt(unshelledCoordinates)

        assert shellingResult.present

        return shellingResult.get()
    }

    @TailRecursive
    Map<String, String> findUnshelledCoordinates(Map pollingResult, int counter = 0) {
        assert counter < 100

        String x = 'abcdefghij'[random.nextInt(10)]
        String y = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10].get(random.nextInt(10))

        Map<String, String> coordinates = [x: String.valueOf(x), y: y]

        !alreadyShelled.contains(coordinates) ? coordinates : findUnshelledCoordinates(pollingResult, counter++)
    }

    /**
     * This method prevents from shooting twice at the same set of coordinates
     * @param coordinates
     * @return
     */
    Optional<FieldState> shootAt(Map<String, String> coordinates) {
        shotCount++
        if (!alreadyShelled.contains(coordinates)) {
            client.requestSpec { requestSpec ->
                requestSpec.body.text(
                        toJson(coordinates))
                requestSpec.headers.set('content-type', 'application/json')
                requestSpec.headers.set('playerId', playerId)
            }
            lastShotResponse = client.put("$FIRE")
            if(lastShotResponse.statusCode==200){
                alreadyShelled.add(coordinates)
            }
            adjustVictoryCounter(extractFieldState(lastShotResponse))
        } else {
            Optional.of(fieldStateAt(coordinates))
        }
    }

    FieldState fieldStateAt(Map<String, String> coordinates) {
        Integer position = calculatePosition(coordinates.x, coordinates.y)
        String fieldState = poll()['field']["$position"]
        fieldState ? FieldState.valueOf(fieldState) : FieldState.WATER
        FieldState.valueOf(poll()['field']["$position"])
    }

    Integer calculatePosition(String x, String y) {
        String xAxis = 'abcdefghij'
        (xAxis.indexOf(x) * 10) + Integer.valueOf(y) - 1
    }

    Optional<FieldState> extractFieldState(ReceivedResponse response) {
        response.statusCode == 200 ?
                Optional.of(FieldState.valueOf(jsonSlurper.parse(response.body.inputStream)['shellingResult'])) :
                Optional.empty()
    }

    Optional<FieldState> adjustVictoryCounter(Optional<FieldState> fieldState) {
        if (fieldState.present) {
            if (fieldState.get() == FieldState.HIT || fieldState.get() == FieldState.SUNK) {
                victoryCounter = victoryCounter - 1
            }
        }
        fieldState
    }
    /**
     * Coordinates to place a ship between
     * @param bow e.g. "a2"
     * @param stern e.g. "d2"
     * @return the response object
     */
    ReceivedResponse place(String bow, String stern) {
        Map<String, Map<String, String>> coordinates = [bow: [x: "${bow.substring(0,1)}", y: "${bow.substring(1,2)}"], stern: [x: "${stern.substring(0,1)}", y: "${stern.substring(1,2)}"]]

        place coordinates
    }

    ReceivedResponse place(Map<String, Map<String, String>> coordinates) {
        client.requestSpec { requestSpec ->
            requestSpec.body.text(
                    toJson(coordinates))
            requestSpec.headers.set('content-type', 'application/json')
            requestSpec.headers.set('playerId', playerId)
        }
        client.post(SHIP)
    }
}
