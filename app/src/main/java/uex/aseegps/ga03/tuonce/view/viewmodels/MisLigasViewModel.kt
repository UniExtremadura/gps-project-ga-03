package uex.aseegps.ga03.tuonce.view.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import uex.aseegps.ga03.tuonce.model.Repository
import kotlinx.coroutines.launch
import uex.aseegps.ga03.tuonce.TuOnceApplication
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.utils.SortPlayers

class MisLigasViewModel (
    private val repository: Repository
) : ViewModel() {
    var user : User? = null
    var equipoUsuario = repository.equipoUsuario
    var futbolistasDelEquipoUsuario = repository.futbolistasDelEquipoUsuario

    var ligaUsuario = repository.ligaUsuario
    var usuariosLiga = repository.usuariosLiga

    var bot1 = repository.bot1
    var bot2 = repository.bot2
    var bot3 = repository.bot3

    var equipoBot1 = repository.equipoBot1
    var equipoBot2 = repository.equipoBot2
    var equipoBot3 = repository.equipoBot3

    var futbolistasEquipoBot1 = repository.futbolistasEquipoBot1
    var futbolistasEquipoBot2 = repository.futbolistasEquipoBot2
    var futbolistasEquipoBot3 = repository.futbolistasEquipoBot3

    fun initialize(){
        if(user != null) {
            repository.setUserid(user?.userId!!)
        }
    }

    fun initializeLiga(ligaId : Long){
        repository.setLigaId(ligaId)
    }

    fun initializeEquipo(eqId : Long)
    {
        repository.setEquipoId(eqId)
    }


    fun marcarActividadCrearLiga(jornada : Int?){
        viewModelScope.launch (Dispatchers.Default) {
            repository.marcarActividadCrearLiga(user, jornada)
        }
    }

    fun marcarActividadTerminarLiga(nombre : String?){
        viewModelScope.launch(Dispatchers.Default)  {
            repository.marcarActividadTerminarLiga(user, nombre)
        }
    }

    fun actualizarFutbolista(futbolista: Futbolista){
        viewModelScope.launch(Dispatchers.Default)  {
            repository.actualizarFutbolista(futbolista)
        }
    }

    fun eliminarEquipo(eq : Equipo){
        viewModelScope.launch(Dispatchers.Default)  {
            repository.eliminarEquipo(eq)
        }
    }

    fun actualizarEquipo(equipo: Equipo?) {
        viewModelScope.launch(Dispatchers.Default)  {
            repository.actualizarEquipo(equipo)
        }
    }

    fun eliminarLiga(){
        viewModelScope.launch(Dispatchers.Default)  {
            repository.eliminarLiga()
        }
    }

    fun eliminarUsuario(id : Long){
        viewModelScope.launch(Dispatchers.Default)  {
            repository.eliminarUsuario(id)
        }
    }

    fun actualizarPuntos(userId : Long, puntos : Int?){
        viewModelScope.launch(Dispatchers.Default)  {
            repository.actualizarPuntos(userId, puntos)
        }
    }

    fun terminarLiga() {
        viewModelScope.launch(Dispatchers.Default)  {
            val usuarioConectado = user
            // recuperar bots
            val bot1 = bot1.value
            val bot2 = bot2.value
            val bot3 = bot3.value
            // recuperar equipos
            val equipo = equipoUsuario.value
            val equipoBot1 = equipoBot1.value
            val equipoBot2 = equipoBot2.value
            val equipoBot3 = equipoBot3.value

            // Recuperar futbolistas
            val futbolistasUsuario = futbolistasDelEquipoUsuario.value
            val futbolistasBot1 = futbolistasEquipoBot1.value
            val futbolistasBot2 = futbolistasEquipoBot2.value
            val futbolistasBot3 = futbolistasEquipoBot3.value

            // Actualizar puntos de usuario conectado en la base de datos
            usuarioConectado?.points = 0
            actualizarPuntos(usuarioConectado?.userId!!, usuarioConectado?.points)

            // Borrar bots
            for (bot in listOf(bot1, bot2, bot3)) {
                if (bot != null) {
                    eliminarUsuario(bot.userId!!)
                }
            }

            // Borrar Liga
            val liga = ligaUsuario
            marcarActividadTerminarLiga(liga?.value?.name)

            if (liga != null) {
                eliminarLiga()
                equipo?.ligaId = null
                actualizarEquipo(equipo)
            }

            for (equipo in listOf(equipoBot1, equipoBot2, equipoBot3)) {
                eliminarEquipo(equipo!!)
            }


            // Borrar referencias a equipo en futbolistas
            for (futbolista in listOf(futbolistasUsuario, futbolistasBot1, futbolistasBot2, futbolistasBot3)) {
                if (futbolista != null) {
                    for (fut in futbolista) {
                        fut.goles = 0
                        fut.asistencias = 0
                        fut.tarjetaRoja = 0
                        fut.tarjetaAmarilla = 0
                        fut.parada = 0
                        fut.balonAlArea = 0
                        fut.faltacometidas = 0
                        fut.minutoJugados = 0
                        fut.puntosAportados = 0
                        actualizarFutbolista(fut)
                    }
                }
            }
            for (futbolista in listOf(futbolistasBot1, futbolistasBot2, futbolistasBot3)) {
                if (futbolista != null) {
                    for (fut in futbolista) {
                        fut.equipoId = null
                        actualizarFutbolista(fut)
                    }
                }
            }


        }
    }


    fun simularPartidosYActualizar(jornada: Int) {
        viewModelScope.launch(Dispatchers.Default)  {
            val usuarioConectado = user
            val bot1 = bot1.value
            val bot2 = bot2.value
            val bot3 = bot3.value

            val equipoUsuario = equipoUsuario.value
            val equipoBot1 = equipoBot1.value
            val equipoBot2 = equipoBot2.value
            val equipoBot3 = equipoBot3.value

            if (equipoUsuario != null && equipoBot1 != null && equipoBot2 != null && equipoBot3 != null) {
                simularPartido(
                    futbolistasDelEquipoUsuario.value!!,
                    futbolistasEquipoBot1.value!!
                )
                simularPartido(
                    futbolistasEquipoBot2.value!!,
                    futbolistasEquipoBot3.value!!
                )

                if (usuarioConectado != null && bot1 != null && bot2 != null && bot3 != null) {
                    calcularPuntuacionUsuario(usuarioConectado, futbolistasDelEquipoUsuario.value!!)
                    calcularPuntuacionUsuario(bot1, futbolistasEquipoBot1.value!!)
                    calcularPuntuacionUsuario(bot2, futbolistasEquipoBot2.value!!)
                    calcularPuntuacionUsuario(bot3, futbolistasEquipoBot3.value!!)
                }


                marcarActividadCrearLiga(jornada)
            }
        }
    }


    private fun simularPartido(equipoLocal: List<Futbolista>, equipoVisitante: List<Futbolista>) {
        var listaLocal = mutableListOf<Futbolista>()
        val listaVisitante = mutableListOf<Futbolista>()
        for (futbolista in equipoLocal) {
            if(futbolista.estaEnel11 == 1){
                listaLocal.add(futbolista)
            }
        }
        for (futbolista in equipoVisitante) {
            if(futbolista.estaEnel11 == 1){
                listaVisitante.add(futbolista)
            }
        }

        for (futbolista in listaLocal) {
            futbolista.goles += (0..2).random()
            futbolista.asistencias += (0..2).random()
            futbolista.tarjetaRoja += (0..1).random()
            futbolista.tarjetaAmarilla += (0..2).random()
            futbolista.parada += (0..1).random()
            futbolista.balonAlArea += (0..3).random()
            futbolista.faltacometidas += (0..3).random()
            futbolista.minutoJugados = (70..90).random()
            futbolista.puntosAportados += SortPlayers.calcularPuntuacion(futbolista)
        }

        for (futbolista in listaVisitante) {
            futbolista.goles += (0..2).random()
            futbolista.asistencias += (0..2).random()
            futbolista.tarjetaRoja += (0..1).random()
            futbolista.tarjetaAmarilla += (0..2).random()
            futbolista.parada += (0..1).random()
            futbolista.balonAlArea += (0..3).random()
            futbolista.faltacometidas += (0..3).random()
            futbolista.minutoJugados += (5..90).random()
            futbolista.puntosAportados += SortPlayers.calcularPuntuacion(futbolista)
        }

        val futbolistas = listaLocal + listaVisitante
        for(futbolista in futbolistas) {
            actualizarFutbolista(futbolista)
        }

    }


    private fun calcularPuntuacionUsuario(usuario: User, pfutbolistas: List<Futbolista>){
        viewModelScope.launch(Dispatchers.Default)  {
            val futbolistas = pfutbolistas
            if (futbolistas != null) {
                for (futbolista in futbolistas) {
                    usuario.points += futbolista.puntosAportados
                }
            }
            val id = usuario.userId
            if (id != null) {
                actualizarPuntos(usuario.userId!!, usuario.points)
            }

        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return MisLigasViewModel(
                    (application as TuOnceApplication).appContainer.repository,
                ) as T
            }
        }
    }
}