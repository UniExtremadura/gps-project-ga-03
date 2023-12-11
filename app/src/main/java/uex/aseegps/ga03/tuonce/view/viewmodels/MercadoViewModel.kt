package uex.aseegps.ga03.tuonce.view.viewmodels

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.tiviclone.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.TuOnceApplication
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.utils.SortPlayers
import uex.aseegps.ga03.tuonce.view.adapters.MercadoAdapter

class MercadoViewModel(
    private val repository: Repository
) : ViewModel() {
    var user : User? = null
    var futbolistas = repository.futbolistas
    var equipoUsuario = repository.equipoUsuario

    suspend fun obtenerJugadoresOrdenadosPorPuntuacion(): List<Futbolista> {
        var jugadoresLibres = mutableListOf<Futbolista>()
        var jugadoresOrdenados = emptyList<Futbolista>()

        withContext(Dispatchers.Default) {
            futbolistas.value?.forEach {
                if (it.equipoId == null) {
                    jugadoresLibres.add(it)
                }
            }
            jugadoresOrdenados = SortPlayers.clasificarJugadores(jugadoresLibres)
        }

        return jugadoresOrdenados.filter{ it.equipoId == null}
    }

    fun initialize(){
        repository.setUserid(user?.userId!!)
    }

    suspend fun comprarFutbolista(futbolistaComprado : Futbolista)
    : Boolean
    {
        Log.d("comprar", futbolistaComprado.toString())
        return if (equipoUsuario.value?.presupuesto!! >= futbolistaComprado?.varor!!) {
            if (equipoUsuario.value?.presupuesto!! >= futbolistaComprado?.varor!!) {
                // Si lo compra, se va del mercado (con lo que ello implica)
                repository.eliminarFutbolistaDelMercado(
                    futbolistaComprado!!,
                    equipoUsuario.value,
                    user
                )
            }
            true
        } else
            false
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
                return MercadoViewModel(
                    (application as TuOnceApplication).appContainer.repository,
                ) as T
            }
        }
    }
}