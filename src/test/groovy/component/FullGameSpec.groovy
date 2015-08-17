package component

import model.FieldState
import model.GamePhase
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.test.ServerBackedApplicationUnderTest
import ratpack.test.http.TestHttpClient
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

/**
 * This Spec describes a complete game
 * BEWARE: each test changes the state of the game and the tests rely on their execution order and the preceding state changes made by the other tests
 */
@Stepwise
class FullGameSpec extends Specification {
    @Shared
    ServerBackedApplicationUnderTest aut = new GroovyRatpackMainApplicationUnderTest()

    @Shared
    Player player1 = new Player(client: TestHttpClient.testHttpClient(aut))

    @Shared
    Player player2 = new Player(client: TestHttpClient.testHttpClient(aut))

    List<Player> players = [player2, player1]
    def 'connect phase'() {
        when: 'two players connect to the game'
        player1.connect()
        player2.connect()

        then: 'each player will have a unique playerId'
        player1.playerId && player2.playerId && (player2.playerId != player1.playerId)
    }

    def 'should be in PLACEMENT phase of game'() {
        given: 'a polling result'
        Collection pollingResults = players.collect { it.poll() }

        expect: "both state the game to be in PLACEMENT phase"
        pollingResults.every { it['gamePhase'] == 'PLACEMENT' }
    }

    def "should be no players' turn before placement is complete"() {
        given: 'a polling result'
        Collection pollingResults = players.collect { it.poll() }

        expect: "it should be nobody's turn"
        pollingResults.every { !it['myTurn'] }
    }

    def 'both fleets should be completely available'() {
        given: 'a polling result'
        Collection pollingResults = players.collect { it.poll() }

        expect: "both fleets should be available"
        pollingResults.every { (it['availableShips'] as Map) == ['5': 1, '4': 2, '3': 3, '2': 4] }
    }

    def 'should be in SHOOTOUT phase after both fleets have been deployed'() {
        given:
        player1.deployFleet()
        player2.deployFleet()

        when:
        Collection pollingResults = players.collect { it.poll() }

        then:
        pollingResults.every { it['gamePhase'] == 'SHOOTOUT' }
    }

    def 'should be exactly one players turn'() {
        given: 'a polling result'
        Collection pollingResults = players.collect { it.poll() }

        expect: "exactly one player can shoot"
        pollingResults.find { it['myTurn' == true] } && pollingResults.find {
            it['myTurn'] == false
        } && pollingResults.size() == 2
    }

    def 'should receive an error if the wrong player shoots'() {
        given: 'a polling result'
        Collection pollingResults = players.collect { it.poll() }

        expect:
        false
    }

    def 'should receive a MISS if shot misses'() {
        when:
        FieldState shellingResult = activePlayer.shootAt([x: '1', y: 'b']).get()

        then:
        shellingResult == FieldState.MISS
    }

    def 'should receive a HIT if shot hits'() {
        when:
        FieldState shellingResult = activePlayer.shootAt([x: '1', y: 'a']).get()

        then:
        shellingResult == FieldState.HIT
    }

    def 'should reveive a SUNK if ship is sunk'() {
        given:
        FieldState shellingResult = activePlayer.shootAt([x: '1', y: 'a']).get()
        assert shellingResult == FieldState.HIT

        when:
        shellingResult = players.find { it.isActive() }.shootAt([x: '2', y: 'a']).get()

        then:
        shellingResult == FieldState.SUNK
    }

    def 'should be WON or LOST at the end of the game respectively'() {
        when:'a game is played until the end'
        while(players.every{it.victoryCounter>0}){
            activePlayer.shootRandomly()
        }

        then:
        players.find{it.victoryCounter>0}.poll()['gamePhase']==GamePhase.LOST
        players.find{it.victoryCounter==0}.poll()['gamePhase']==GamePhase.VICTORY
    }
    //NOTE: explain closure as last parameter explain 'as'

    Player getActivePlayer(){
        [player1, player2].find { it.isActive() }
    }
}
