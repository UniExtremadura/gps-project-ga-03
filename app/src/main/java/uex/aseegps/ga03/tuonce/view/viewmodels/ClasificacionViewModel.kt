package uex.aseegps.ga03.tuonce.view.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import uex.aseegps.ga03.tuonce.model.Repository
import uex.aseegps.ga03.tuonce.TuOnceApplication
import uex.aseegps.ga03.tuonce.model.User

class ClasificacionViewModel (
    private val repository: Repository
) : ViewModel() {
    var user : User? = null
    var ligaUsuario = repository.ligaUsuario
    var usuariosLiga = repository.usuariosLiga

    fun initialize(){
        repository.setUserid(user?.userId!!)
    }

    fun initializeLiga(ligaId : Long){
        repository.setLigaId(ligaId)
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
                return ClasificacionViewModel(
                    (application as TuOnceApplication).appContainer.repository,
                ) as T
            }
        }
    }
}