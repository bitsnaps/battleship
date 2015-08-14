package component

import ratpack.http.client.ReceivedResponse
import ratpack.test.http.TestHttpClient

import static groovy.json.JsonOutput.toJson


class Player {
    private final static String CONNECT = 'connect'
    private final static String SHIP = 'ship'

    TestHttpClient client

    ReceivedResponse connect() {
        client.post(CONNECT)
    }

    /**
     * Coordinates to place a ship between
     * @param bow e.g. "a2"
     * @param stern e.g. "d2"
     * @return the response object
     */
    ReceivedResponse place(String bow, String stern){
        Map<String,Map<String,String>> coordinates = [bow:[y: "${bow[0]}", x:"${bow.substring(1)}"],stern:[y: "${ster[0]}", x:"${ster.substring(1)}"]]

        place coordinates
    }

    ReceivedResponse place(Map<String,Map<String, String>> coordinates) {
        client.requestSpec { requestSpec ->
            requestSpec.body.text(
                    toJson(coordinates))
        }
        client.post(SHIP)
    }
}
