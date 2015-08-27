package component

import component.support.Player
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.http.client.ReceivedResponse
import ratpack.test.ServerBackedApplicationUnderTest
import ratpack.test.http.TestHttpClient
import spock.lang.Specification

class PlacementSpec extends Specification {

    ServerBackedApplicationUnderTest aut = new GroovyRatpackMainApplicationUnderTest()

    Player player1 = new Player(client: TestHttpClient.testHttpClient(aut), playerId: "")
    Player player2 = new Player(client: TestHttpClient.testHttpClient(aut))

    def 'POSTing to the placement resource without playerId header results in 401 Unauthorized'() {
        when:
        ReceivedResponse response= player1.place('A1', 'A5')

        then:
        response.statusCode==401
    }

    def 'should not be possible to place two ships right next to each other'(){

    }
    //benachbart
    //übereck

}
