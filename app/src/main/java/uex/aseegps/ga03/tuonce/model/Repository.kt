package es.unex.giiis.asee.tiviclone.data


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import uex.aseegps.ga03.tuonce.database.ActividadDao
import uex.aseegps.ga03.tuonce.database.EquipoDao
import uex.aseegps.ga03.tuonce.database.FutbolistaDao
import uex.aseegps.ga03.tuonce.database.UserDao
import uex.aseegps.ga03.tuonce.model.AccionActividad
import uex.aseegps.ga03.tuonce.model.Actividad
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User

class Repository private constructor(
    private val userDao: UserDao,
    private val futbolistaDao: FutbolistaDao,
    private val equipoDao: EquipoDao,
    private val actividadDao: ActividadDao
) {

    val futbolistas = futbolistaDao.findAllFutbolistas()

    private val userFilter = MutableLiveData<Long>()

    val actividades: LiveData<List<Actividad>> =
        userFilter.switchMap{ userid -> actividadDao.findAllByUser(userid) }
    fun setUserid(userid: Long) {
        userFilter.value = userid
    }
    suspend fun eliminarFutbolistaDelMercado(futbolistaComprado : Futbolista, equipoUsuario : Equipo?, usuario : User?)
    {
        actualizarValorEquipo(equipoUsuario, futbolistaComprado.varor)
        actualizarEquipoDelFutbolista(futbolistaComprado, equipoUsuario)
        marcarActividadCompra(usuario, futbolistaComprado.nombreJugador)
    }

    suspend fun actualizarEquipoDelFutbolista(futbolistaComprado : Futbolista, equipoUsuario : Equipo?)
    {
        futbolistaComprado?.equipoId = equipoUsuario?.equipoId
        futbolistaDao.update(futbolistaComprado)
    }

    suspend fun actualizarValorEquipo(equipoUsuario : Equipo?, valorFutbolista : Int)
    {
        equipoUsuario?.presupuesto = equipoUsuario?.presupuesto!! - valorFutbolista!!
        equipoDao.update(equipoUsuario)
    }

    suspend fun marcarActividadCompra(usuarioConectado : User?, futbolistaId : String?)
    {
        val actividadCompra = Actividad(
            actividadId = null,
            accion = AccionActividad.COMPRAR_FUTBOLISTA,
            usuarioActividad = usuarioConectado?.userId,
            futbolistaActividad = futbolistaId,
            ligaActividad = null,
            jornadaActividad = null
        )
        actividadDao.insertar(actividadCompra)
    }

    companion object {

        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(
            userDao: UserDao,
            futbolistaDao: FutbolistaDao,
            equipoDao: EquipoDao,
            actividadDao: ActividadDao
        ): Repository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(userDao, futbolistaDao, equipoDao, actividadDao).also { INSTANCE = it }
            }
        }
    }
}