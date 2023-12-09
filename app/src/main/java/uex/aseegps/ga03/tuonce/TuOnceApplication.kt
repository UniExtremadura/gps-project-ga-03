package uex.aseegps.ga03.tuonce

import android.app.Application
import uex.aseegps.ga03.tuonce.utils.AppContainer

class TuOnceApplication : Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
