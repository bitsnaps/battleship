package component

import groovy.json.JsonSlurper
import ratpack.http.client.ReceivedResponse
import ratpack.test.http.TestHttpClient

import static groovy.json.JsonOutput.toJson


class Player {
    private final static String CONNECT = 'connect'
    private final static String SHIP = 'ship'
    private final static String POLLING = 'turn'
    private final static JsonSlurper jsonSlurper = new JsonSlurper()
    String playerId

    TestHttpClient client

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

    Map poll() {
        assert playerId
        client.requestSpec { requestSpec ->
            requestSpec.headers.set('playerId', playerId)
        }
        ReceivedResponse response = client.get(POLLING)
        assert response.statusCode == 200

        Map result = jsonSlurper.parse(response.body.inputStream)
        assert result.myTurn != null
        result
    }

    /**
     * Coordinates to place a ship between
     * @param bow e.g. "a2"
     * @param stern e.g. "d2"
     * @return the response object
     */
    ReceivedResponse place(String bow, String stern) {
        Map<String, Map<String, String>> coordinates = [bow: [y: "${bow[0]}", x: "${bow.substring(1)}"], stern: [y: "${ster[0]}", x: "${ster.substring(1)}"]]

        place coordinates
    }

    ReceivedResponse place(Map<String, Map<String, String>> coordinates) {
        client.requestSpec { requestSpec ->
            requestSpec.body.text(
                    toJson(coordinates))
        }
        client.post(SHIP)
    }
}
