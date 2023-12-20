package uex.aseegps.ga03.tuonce.view.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giiis.asee.tiviclone.data.Repository
import kotlinx.coroutines.launch
import uex.aseegps.ga03.tuonce.TuOnceApplication
import uex.aseegps.ga03.tuonce.database.dummyFutbolista
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User
import kotlin.random.Random

class JoinViewModel (
    private val repository: Repository
) : ViewModel() {
    var futbolistas = repository.futbolistas

    suspend fun insertarNuevoUsuario(usuario : User) : Long
    {
        return repository.insertarUsuario(usuario)
    }

    suspend fun insertarEquipo(equipo : Equipo) : Long
    {
        return repository.insertarEquipo(equipo)
    }

    suspend fun actualizarFutbolista(futbolista: Futbolista){
        repository.actualizarFutbolista(futbolista)
    }


    suspend fun insertarFutbolista(nuevoFutbolista: Futbolista)
    {
        repository.insertarFutbolista(nuevoFutbolista)
    }


    fun crearEquipo(user : User, id : Long?){
        val random = Random
        val randomNumber = random.nextInt(50_000, 75_000)

        viewModelScope.launch{
            insertarEquipoUsuario(Equipo(
                null,
                name = user.name,
                userId = id,
                ligaId = null,
                presupuesto = randomNumber,
            ))
        }
    }

    private suspend fun insertarEquipoUsuario(nuevoEquipo : Equipo){
        val equipoId = insertarEquipo(nuevoEquipo)
        var portero = 0
        var defensa = 0
        var centrocampista = 0
        var delantero = 0

        dummyFutbolista.shuffled().forEachIndexed{ index, futbolista ->
            futbolista.equipoId = null
            if (futbolista.posicion == "Portero" && portero < 1){
                portero++
                futbolista.equipoId = equipoId
                futbolista.estaEnel11 = 1
            }
            if (futbolista.posicion == "Defensa" && defensa < 4) {
                defensa++
                futbolista.equipoId = equipoId
                futbolista.estaEnel11 = 1
            }
            if (futbolista.posicion == "Centrocampista" && centrocampista < 3) {
                centrocampista++
                futbolista.equipoId = equipoId
                futbolista.estaEnel11 = 1
            }
            if (futbolista.posicion == "Delantero" && delantero < 3) {
                delantero++
                futbolista.equipoId = equipoId
                futbolista.estaEnel11 = 1
            }

            insertarFutbolista(futbolista)
        }
        ponerFutbolistasAlEquipo(equipoId)
    }

    private suspend fun ponerFutbolistasAlEquipo(equipoId : Long){
        var futbolistas: List<Futbolista>? = futbolistas.value
        futbolistas?.shuffled()?.take(7)?.forEachIndexed{ index, futbolista ->
            if (futbolista.estaEnel11 == 0 && futbolista.equipoId == null) {
                futbolista.equipoId = equipoId
                actualizarFutbolista(futbolista)
            }
        }
    }

    suspend fun credencialesCorrectas(fondo : Int, nombreUsuario: String, passOne : String) : Long{

            val user = User(
                null,
                fondo,
                nombreUsuario,
                passOne
            )
            val id = insertarNuevoUsuario(user)
            crearEquipo(user, id)
            return id
    }

    fun obtenerEscudos(): List<Int> {
        return repository.escudos
    }

    fun obtenerEquipaciones(): List<Int> {
        return repository.equipaciones
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
                return JoinViewModel(
                    (application as TuOnceApplication).appContainer.repository,
                ) as T
            }
        }
    }
}