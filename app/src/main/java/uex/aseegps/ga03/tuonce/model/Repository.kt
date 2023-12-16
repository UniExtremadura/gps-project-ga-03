package uex.aseegps.ga03.tuonce.model


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
    private val userDao: UserDao,
    private val ligaDao: LigaDao,
    private val futbolistaDao: FutbolistaDao,
    private val equipoDao: EquipoDao,
    private val actividadDao: ActividadDao
) {


    private val userFilter = MutableLiveData<Long>()
    private val equipoFilter = MutableLiveData<Long>()
    private val ligaFilter = MutableLiveData<Long>()
    private val userName = MutableLiveData<String>()

    val usuarioConectado : LiveData<User?> =
        userName.switchMap {nombre -> userDao.findByNameLD(nombre) }

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

    val bot1 = userDao.findByNameLD("Bot1")
    val bot2 = userDao.findByNameLD("Bot2")
    val bot3 = userDao.findByNameLD("Bot3")

    val equipoBot1: LiveData<Equipo?> = bot1.switchMap { bot1 ->
        if (bot1 != null) {
            equipoDao.findByUserIdLD(bot1.userId)
        } else {
            MutableLiveData<Equipo?>() // LiveData vacío si bot1 es nulo
        }
    }

    val equipoBot2: LiveData<Equipo?> = bot2.switchMap { bot2 ->
        if (bot2 != null) {
            equipoDao.findByUserIdLD(bot2.userId)
        } else {
            MutableLiveData<Equipo?>() // LiveData vacío si bot2 es nulo
        }
    }

    val equipoBot3: LiveData<Equipo?> = bot3.switchMap { bot3 ->
        if (bot3 != null) {
            equipoDao.findByUserIdLD(bot3.userId)
        } else {
            MutableLiveData<Equipo?>() // LiveData vacío si bot3 es nulo
        }
    }

    val futbolistasEquipoBot1 : LiveData<List<Futbolista>> =
        equipoBot1.switchMap { eq -> futbolistaDao.findByEquipoIdLD(eq?.equipoId) }

    val futbolistasEquipoBot2 : LiveData<List<Futbolista>> =
        equipoBot2.switchMap { eq -> futbolistaDao.findByEquipoIdLD(eq?.equipoId) }

    val futbolistasEquipoBot3 : LiveData<List<Futbolista>> =
        equipoBot3.switchMap { eq -> futbolistaDao.findByEquipoIdLD(eq?.equipoId) }

    fun setUserid(userid: Long) {
        userFilter.value = userid
    }

    fun setUserName(nombre : String){
        userName.value = nombre
    }

    fun setEquipoId(eqId: Long) {
        equipoFilter.value = eqId
    }

    fun setLigaId(ligaId: Long) {
        ligaFilter.value = ligaId
    }


    suspend fun actualizarFutbolista(futbolista: Futbolista){
        futbolistaDao.update(futbolista)
    }

    suspend fun actualizarEquipo(equipo: Equipo?) {
        equipoDao.update(equipo)
    }

    suspend fun venderFutbolistaDelequipo(futbolistaVendido : Futbolista, equipoUsuario : Equipo?, usuario : User?){
        actualizarValorEquipoSumar(equipoUsuario, futbolistaVendido.varor)
        actualizarFutbolistaSinEquipo(futbolistaVendido)
        marcarActividadVenta(usuario, futbolistaVendido.nombreJugador)
    }
    suspend fun eliminarUsuario(id : Long)
    {
        userDao.delete(id)
    }

    suspend fun eliminarEquipo(eq : Equipo){
        equipoDao.delete(eq)
    }

    suspend fun eliminarLiga(){
        ligaDao.eliminarLiga()
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
    suspend  fun actualizarPuntos(usuarioId : Long, puntos : Int?){
        userDao.updatePoints(usuarioId, puntos!!)
    }


    suspend fun marcarActividadCrearLiga(usuarioConectado : User?, jornada : Int?)
    {
        val actividad = Actividad(
            actividadId = null,
            accion = AccionActividad.INICIAR_JORNADA,
            usuarioActividad = usuarioConectado?.userId,
            futbolistaActividad = null,
            ligaActividad = null,
            jornadaActividad = jornada!! - 1
        )
        actividadDao.insertar(actividad)
    }

    suspend fun marcarActividadTerminarLiga(usuarioConectado : User?, nombre : String?)
    {
        val actividadAcabarLiga = Actividad(
            actividadId = null,
            accion = AccionActividad.ACABAR_LIGA,
            usuarioActividad = usuarioConectado?.userId,
            futbolistaActividad = null,
            ligaActividad = nombre,
            jornadaActividad = null
        )
        actividadDao.insertar(actividadAcabarLiga)
    }

    suspend fun insertarLiga(nuevaLiga : Liga) : Long
    {
        return ligaDao.insertarLiga(nuevaLiga)
    }

    suspend fun marcarActividadNuevaLiga(usuarioConectado : User?, liga : String?)
    {
        val actividadIniciarLiga = Actividad(
            actividadId = null,
            accion = uex.aseegps.ga03.tuonce.model.AccionActividad.INICIAR_LIGA,
            usuarioActividad = usuarioConectado?.userId,
            futbolistaActividad = null,
            ligaActividad = liga,
            jornadaActividad = null
        )
        actividadDao.insertar(actividadIniciarLiga)
    }

    suspend fun insertarBot(bot : User) : Long
    {
        return userDao.insert(bot)
    }

    suspend fun insertarFutbolista(nuevoFutbolista: Futbolista){
        futbolistaDao.insert(nuevoFutbolista)
    }

    suspend fun insertarEquipo(nuevoEquipo : Equipo) : Long{
        return equipoDao.insert(nuevoEquipo)
    }

    suspend fun insertarUsuario(usuario : User) : Long
    {
        return userDao.insert(usuario)
    }
}