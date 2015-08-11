package modules

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import model.Game

/**
 * Created by sven on 11.08.15.
 */
//GuiceModule erklären - Einführung
class GameModule extends  AbstractModule {
    @Override
    protected void configure() {
        bind(Game).in(Scopes.SINGLETON)
    }
}
