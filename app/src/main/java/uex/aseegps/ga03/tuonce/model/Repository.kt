package es.unex.giiis.asee.tiviclone.data


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.database.ActividadDao
import uex.aseegps.ga03.tuonce.database.EquipoDao
import uex.aseegps.ga03.tuonce.database.FutbolistaDao
import uex.aseegps.ga03.tuonce.database.LigaDao
import uex.aseegps.ga03.tuonce.database.UserDao
import uex.aseegps.ga03.tuonce.model.AccionActividad
import uex.aseegps.ga03.tuonce.model.Actividad
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.Liga
import uex.aseegps.ga03.tuonce.model.User

class Repository(
    private val ligaDao: LigaDao,
    private val futbolistaDao: FutbolistaDao,
    private val equipoDao: EquipoDao,
    private val actividadDao: ActividadDao
) {


    private val userFilter = MutableLiveData<Long>()
    private val equipoFilter = MutableLiveData<Long>()
    private val ligaFilter = MutableLiveData<Long>()
    // Futbolistas de la base de datos
    val futbolistas = futbolistaDao.findAllFutbolistas()

    // Actividades del usuario, que cuando se compra, vende, crea liga, etc. se actualizan
    val actividades: LiveData<List<Actividad>> =
        userFilter.switchMap{ userid -> actividadDao.findAllByUser(userid) }

    val equipoUsuario: LiveData<Equipo> =
        userFilter.switchMap{ userid -> equipoDao.findEquipoByUserId(userid) }

    val ligaUsuario: LiveData<Liga> =
        userFilter.switchMap{ userid -> ligaDao.findLigaPorUsuario(userid) }

    val usuariosLiga: LiveData<List<User>> =
        ligaFilter.switchMap{ ligaId ->  ligaDao.findUsuariosPorLiga(ligaId) }

    val futbolistasDelEquipoUsuario: LiveData<List<Futbolista>> =
        equipoFilter.switchMap{ eqId -> futbolistaDao.findFutbolistasByEquipoId(eqId) }

    fun setUserid(userid: Long) {
        userFilter.value = userid
    }

    fun setEquipoId(eqId: Long) {
        equipoFilter.value = eqId
    }

    fun setLigaId(ligaId: Long) {
        ligaFilter.value = ligaId
    }


    suspend fun actualizarEquipo(equipo: Equipo?) {
        equipoDao.update(equipo)
    }

    suspend fun venderFutbolistaDelequipo(futbolistaVendido : Futbolista, equipoUsuario : Equipo?, usuario : User?){
        actualizarValorEquipoSumar(equipoUsuario, futbolistaVendido.varor)
        actualizarFutbolistaSinEquipo(futbolistaVendido)
        marcarActividadVenta(usuario, futbolistaVendido.nombreJugador)
    }
    suspend fun eliminarFutbolistaDelMercado(futbolistaComprado : Futbolista, equipoUsuario : Equipo?, usuario : User?)
    {
        actualizarValorEquipo(equipoUsuario, futbolistaComprado.varor)
        actualizarEquipoDelFutbolista(futbolistaComprado, equipoUsuario)
        marcarActividadCompra(usuario, futbolistaComprado.nombreJugador)
    }

    suspend fun actualizarFutbolistaSinEquipo(futbolistaVendido: Futbolista){
        futbolistaVendido?.equipoId = null
        futbolistaDao.update(futbolistaVendido)
    }
    suspend fun actualizarEquipoDelFutbolista(futbolistaComprado : Futbolista, equipoUsuario : Equipo?)
    {
        futbolistaComprado?.equipoId = equipoUsuario?.equipoId
        futbolistaDao.update(futbolistaComprado)
    }
    suspend fun moveral11(futbolista : Futbolista){
        futbolista?.estaEnel11 = 2
        futbolistaDao.update(futbolista)
    }
    suspend fun modificarDatos(futbolista: Futbolista, jugador : Futbolista){
        futbolista?.estaEnel11 = 0
        jugador?.estaEnel11 = 1
        futbolistaDao.update(futbolista)
        futbolistaDao.update(jugador)
    }
    suspend fun noCambio(futbolista: Futbolista,jugador: Futbolista){
        futbolista?.estaEnel11 = 1
        jugador?.estaEnel11 = 0
        futbolistaDao.update(futbolista)
        futbolistaDao.update(jugador)
    }
    suspend fun actualizarValorEquipoSumar(equipoUsuario: Equipo?, valorFutbolista: Int){
        equipoUsuario?.presupuesto = equipoUsuario?.presupuesto!! + valorFutbolista!!
        equipoDao.update(equipoUsuario)
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

    suspend fun marcarActividadVenta(usuarioConectado: User?,futbolistaId: String?)
    {
        val actividadVenta = Actividad(
            actividadId = null,
            accion = AccionActividad.VENDER_FUTBOLISTA,
            usuarioActividad = usuarioConectado?.userId,
            futbolistaActividad = futbolistaId,
            ligaActividad = null,
            jornadaActividad = null
        )
        actividadDao.insertar(actividadVenta)
    }
}