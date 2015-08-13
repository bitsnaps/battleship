package component

import groovy.json.JsonSlurper
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.test.ServerBackedApplicationUnderTest
import ratpack.test.http.TestHttpClient
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class ConnectSpec extends Specification {
    ServerBackedApplicationUnderTest aut = new GroovyRatpackMainApplicationUnderTest()
    @Delegate
    TestHttpClient client = TestHttpClient.testHttpClient(aut)

    JsonSlurper jsonSlurper = new JsonSlurper()

    def 'connecting to the game returns a playerId'() {
        when:
        post('connect')

        then:
        response.statusCode == 200

        and:
        jsonSlurper.parse(response.body.inputStream)['playerId'] != null
    }

    def 'a second player can connect and gets different ID'() {
        given:
        post('connect')
        String player1Id = jsonSlurper.parse(response.body.inputStream)['playerId']

        when:
        post('connect')
        String player2Id = jsonSlurper.parse(response.body.inputStream)['playerId']

        then:
        response.statusCode == 200

        and:
        (player2Id != null) && (player1Id != player2Id)
    }

    def 'the third connection attempt is rejected'() {
        given:
        post('connect')
        post('connect')

        when:
        post('connect')

        then:
        response.statusCode == 409

    }
}
