package uex.aseegps.ga03.tuonce.view.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giiis.asee.tiviclone.data.Repository
import kotlinx.coroutines.launch
import uex.aseegps.ga03.tuonce.TuOnceApplication
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User

class MoverAl11ViewNodel(
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
    fun obtenerFutbolistasDelEquipoOrdenados(): List<Futbolista> {
        var futbolistasDelEquipo = mutableListOf<Futbolista>()
        initializeEquipo()
        futbolistasDelEquipoUsuario?.value?.forEach {
            if (it.estaEnel11 == 1 ) {
                    futbolistasDelEquipo.add(it)
                }
        }
        (futbolistasDelEquipoUsuario?.value?.filter { it.estaEnel11 == 2 }?.get(0) ?: null)?.let {
            futbolistasDelEquipo.add(
                it
            )
        }

        return futbolistasDelEquipo
    }
   fun modificarDatos(futbolista: Futbolista, jugador: Futbolista){
       viewModelScope.launch {
           repository.modificarDatos(futbolista, jugador)
       }
    }
    fun noCambio(futbolista: Futbolista,jugador: Futbolista){
        viewModelScope.launch {
            repository.noCambio(futbolista, jugador)
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
                return MoverAl11ViewNodel(
                    (application as TuOnceApplication).appContainer.repository,
                ) as T
            }
        }
    }
}

