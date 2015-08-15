package component

import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.test.ServerBackedApplicationUnderTest
import ratpack.test.http.TestHttpClient
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

/**
 * This Spec describes a complete game
 */
@Stepwise
class FullGameSpec extends Specification {
    @Shared
    ServerBackedApplicationUnderTest aut = new GroovyRatpackMainApplicationUnderTest()

    @Shared
    Player player1 = new Player(client: TestHttpClient.testHttpClient(aut))

    @Shared
    Player player2 = new Player(client: TestHttpClient.testHttpClient(aut))

    def 'connect phase'() {
        when: 'two players connect to the game'
        player1.connect()
        player2.connect()

        then: 'each player will have a unique playerId'
        player1.playerId && player2.playerId && (player2.playerId != player1.playerId)
    }

    def "should be no players' turn before placement is complete"() {
        expect: "it should be nobody's turn"
        ([player1, player2].collect { it.poll() }).inject(true) { acc, val -> acc && !val['myTurn'] }
    }
}
