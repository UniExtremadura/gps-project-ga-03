package uex.aseegps.ga03.tuonce.utils

import android.content.Context
import uex.aseegps.ga03.tuonce.model.Repository
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase

class AppContainer(context: Context?) {

    private val db = TuOnceDatabase.getInstance(context!!)
    val repository = Repository(db!!.userDao(), db.ligaDao(),db.futbolistaDao(), db.equipoDao(), db.actividadDao())

}