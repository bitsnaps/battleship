package component

import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.http.client.ReceivedResponse
import ratpack.test.ServerBackedApplicationUnderTest
import ratpack.test.http.TestHttpClient
import spock.lang.Specification

class PlacementSpec extends Specification {

    ServerBackedApplicationUnderTest aut = new GroovyRatpackMainApplicationUnderTest()

    Player player1 = new Player(client: TestHttpClient.testHttpClient(aut))
    Player player2 = new Player(client: TestHttpClient.testHttpClient(aut))

    def 'POSTing to the placement resource without playerId header results in 401 Unauthorized'() {
        when:
        ReceivedResponse response= player1.place(1, 'x')

        then:
        response.statusCode==401
    }
}
