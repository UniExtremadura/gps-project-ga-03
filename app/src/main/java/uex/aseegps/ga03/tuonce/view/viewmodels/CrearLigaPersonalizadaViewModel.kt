package uex.aseegps.ga03.tuonce.view.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giiis.asee.tiviclone.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.TuOnceApplication
import uex.aseegps.ga03.tuonce.database.dummyFutbolista
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.Liga
import uex.aseegps.ga03.tuonce.model.User

class CrearLigaPersonalizadaViewModel (
    private val repository: Repository
) : ViewModel() {
    var user : User? = null
    var equipoUsuario = repository.equipoUsuario

    fun initialize(){
        repository.setUserid(user?.userId!!)
    }

    fun crearLiga(nombreLiga: String, numJornadas: String, imagenesBots: MutableList<Int>) : Boolean {
        val numJornadasInt = numJornadas.toIntOrNull()

        if (nombreLiga.isNotBlank() && numJornadasInt != null) {
            val nuevaLiga = Liga(null, nombreLiga, numJornadasInt, user?.userId, 1)

            viewModelScope.launch {
                withContext(Dispatchers.Default) {
                    // Creamos la liga
                    val idLiga = repository.insertarLiga(nuevaLiga)
                    repository.marcarActividadNuevaLiga(user, nuevaLiga.name)

                    gestionarBots(imagenesBots, idLiga)

                    asignarLigaAlEquipo(idLiga)
                }

            }
            return true
                //findNavController().navigate(uex.aseegps.ga03.tuonce.R.id.action_crearLigaPersonalizada_to_misLigasFragment)
        } else {
            return false
        }
    }

    private suspend fun asignarLigaAlEquipo(idLiga: Long?){
        val equipo: Equipo? = equipoUsuario.value
        equipo?.ligaId = idLiga
        repository.actualizarEquipo(equipo)
    }

    private suspend fun gestionarBots(imagenesBots: MutableList<Int>, idLiga: Long?){
        val bots: List<User> = listOf(
            User(null, imagenesBots[0], "Bot1", "Bot1", 0),
            User(null, imagenesBots[1], "Bot2", "Bot2", 0),
            User(null, imagenesBots[2], "Bot3", "Bot3", 0)
        )

        bots.forEach { bot ->
            val id = repository.insertarBot(bot)
            crearEquipoEnLiga(bot, id, idLiga)
        }
    }

    private suspend fun crearEquipoEnLiga(user : User, id : Long?, idLiga: Long?){
        val nuevoEquipo = Equipo(
            null,
            name = user.name,
            userId = id,
            presupuesto = 1000000,
            ligaId = idLiga
        )
            val equipoId = repository.insertarEquipo(nuevoEquipo)
            val onceJugadores = seleccionar11Jugadores()
            onceJugadores.forEach {
                it.equipoId = equipoId
                it.estaEnel11 = 1
                repository.insertarFutbolista(it)
            }
    }

    private fun seleccionar11Jugadores(): List<Futbolista> {
        return dummyFutbolista.shuffled().take(11)
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
                return CrearLigaPersonalizadaViewModel(
                    (application as TuOnceApplication).appContainer.repository,
                ) as T
            }
        }
    }
}