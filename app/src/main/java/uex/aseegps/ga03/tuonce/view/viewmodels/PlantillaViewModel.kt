package uex.aseegps.ga03.tuonce.view.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import uex.aseegps.ga03.tuonce.model.Repository
import kotlinx.coroutines.launch
import uex.aseegps.ga03.tuonce.TuOnceApplication
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User
import java.lang.Thread.sleep

class PlantillaViewModel(
    private val repository: Repository
) : ViewModel() {
    var user : User? = null
    var equipoUsuario = repository.equipoUsuario
    var futbolistasDelEquipoUsuario = repository.futbolistasDelEquipoUsuario

    fun initialize(){
        repository.setUserid(user?.userId!!)
    }

    fun initializeEquipo(){
        repository.setEquipoId(equipoUsuario?.value?.equipoId!!)
    }

    fun venderFutbolistaDelequipo(futbolistaVendido : Futbolista, equipoUsuario : Equipo?, usuario : User?){
        viewModelScope.launch {
            repository.venderFutbolistaDelequipo(futbolistaVendido, equipoUsuario, usuario)
        }
    }
    fun moveral11(futbolista: Futbolista){
        viewModelScope.launch {
            repository.moveral11(futbolista)
        }
        sleep(100)
    }
    fun obtenerFutbolistasDelEquipoOrdenados(posicion: String?): List<Futbolista> {
        var futbolistasDelEquipo = mutableListOf<Futbolista>()
        futbolistasDelEquipoUsuario?.value?.forEach {
            if (posicion != null) {
                if (it.posicion == posicion) {
                    futbolistasDelEquipo.add(it)
                }
            } else
                futbolistasDelEquipo.add(it)
        }
       return futbolistasDelEquipo.sortedBy { it.estaEnel11 }
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
                return PlantillaViewModel(
                    (application as TuOnceApplication).appContainer.repository,
                ) as T
            }
        }
    }
}
