import handlers.BoatHandler
import handlers.ConnectHandler
import handlers.GameHandler
import handlers.ShootingHandler
import model.Game
import modules.GameModule
import ratpack.groovy.template.MarkupTemplateModule
import ratpack.handling.RequestId
import ratpack.registry.Registry

import static ratpack.groovy.Groovy.groovyMarkupTemplate
import static ratpack.groovy.Groovy.ratpack


//Erklärung das es sich um ein Script und nicht eine Klasse handelt
ratpack {

  bindings {
    //.class wird ergänzt "Erklären"
    module(GameModule)
  }

  handlers {

    all(RequestId.bindAndLog())

    all { Game game ->
      next(Registry.single(Game, game))
    }

    post('connect', new ConnectHandler())

    post('ship', new BoatHandler())

    post('shot', new ShootingHandler())

    //get('/game/field', new GameHandler())

    get('turn', new GameHandler())


  }


}
