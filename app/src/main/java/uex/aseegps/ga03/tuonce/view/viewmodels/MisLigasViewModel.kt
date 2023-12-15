package uex.aseegps.ga03.tuonce.view.viewmodels

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

class MisLigasViewModel (
    private val repository: Repository
) : ViewModel() {
    var user : User? = null
    var equipoUsuario = repository.equipoUsuario
    var futbolistasDelEquipoUsuario = repository.futbolistasDelEquipoUsuario

    var ligaUsuario = repository.ligaUsuario
    var usuariosLiga = repository.usuariosLiga

    val bot1 = repository.bot1
    val bot2 = repository.bot2
    val bot3 = repository.bot3

    val equipoBot1 = repository.equipoBot1
    val equipoBot2 = repository.equipoBot2
    val equipoBot3 = repository.equipoBot3

    val futbolistasEquipoBot1 = repository.futbolistasEquipoBot1
    val futbolistasEquipoBot2 = repository.futbolistasEquipoBot2
    val futbolistasEquipoBot3 = repository.futbolistasEquipoBot3

    fun initialize(){
        if(user != null)
        repository.setUserid(user?.userId!!)
    }

    fun initializeLiga(ligaId : Long){
        repository.setLigaId(ligaId)
    }

    fun initializeEquipo(eqId : Long)
    {
        repository.setEquipoId(eqId)
    }


    fun marcarActividadCrearLiga(jornada : Int?){
        viewModelScope.launch {
            repository.marcarActividadCrearLiga(user, jornada)
        }
    }

    fun marcarActividadTerminarLiga(nombre : String?){
        viewModelScope.launch {
            repository.marcarActividadTerminarLiga(user, nombre)
        }
    }

    fun actualizarFutbolista(futbolista: Futbolista){
        viewModelScope.launch {
            repository.actualizarFutbolista(futbolista)
        }
    }

    fun eliminarEquipo(eq : Equipo){
        viewModelScope.launch {
            repository.eliminarEquipo(eq)
        }
    }

    fun actualizarEquipo(equipo: Equipo?) {
        viewModelScope.launch {
            repository.actualizarEquipo(equipo)
        }
    }

    fun eliminarLiga(){
        viewModelScope.launch {
            repository.eliminarLiga()
        }
    }

    fun eliminarUsuario(id : Long){
        viewModelScope.launch {
            repository.eliminarUsuario(id)
        }
    }

    fun actualizarPuntos(puntos : Int?){
        viewModelScope.launch {
            repository.actualizarPuntos(user?.userId!!, puntos)
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