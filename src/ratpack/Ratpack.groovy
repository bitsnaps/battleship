import handlers.BoatHandler
import handlers.ConnectHandler
import handlers.GameHandler
import handlers.PlayerIdHandler
import handlers.ShootingHandler
import model.Game
import model.PlayerId
import modules.GameModule
import ratpack.groovy.template.MarkupTemplateModule
import ratpack.groovy.template.TextTemplateModule
import ratpack.handling.RequestId
import ratpack.jackson.guice.JacksonModule
import ratpack.registry.Registry

import static ratpack.groovy.Groovy.groovyMarkupTemplate
import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack


//Erklärung das es sich um ein Script und nicht eine Klasse handelt
ratpack {

  bindings {
    //.class wird ergänzt "Erklären"
    module(GameModule)
    module TextTemplateModule, { TextTemplateModule.Config config -> config.staticallyCompile = true }
    module JacksonModule
  }

  handlers { BoatHandler boatHandler ->


    all(RequestId.bindAndLog())

    all { Game game ->
      next(Registry.single(Game, game))
    }

    post('connect', new ConnectHandler())

    all {
      final String playerId = request.headers.get('playerId')
      playerId ? next(Registry.single(new PlayerId(playerId))) : response.status(401).send()
    }

    post('ship', boatHandler)

    put('shoot', new ShootingHandler())

    //get('/game/field', new GameHandler())

    get('myturn', new GameHandler())

    get {
      render groovyTemplate("index.html", title: "Groovy Academy - Battleship-App")
    }

    fileSystem "public", { f -> f.files() }
  }


}
