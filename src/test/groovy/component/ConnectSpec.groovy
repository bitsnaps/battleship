package component

import groovy.json.JsonSlurper
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.test.ServerBackedApplicationUnderTest
import ratpack.test.http.TestHttpClient
import spock.lang.Specification

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
}
