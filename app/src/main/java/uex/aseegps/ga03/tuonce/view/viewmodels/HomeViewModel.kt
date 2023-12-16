package uex.aseegps.ga03.tuonce.view.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import uex.aseegps.ga03.tuonce.model.Repository
import uex.aseegps.ga03.tuonce.TuOnceApplication
import uex.aseegps.ga03.tuonce.model.User

class HomeViewModel (
    private val repository: Repository
) : ViewModel() {
    private val _user = MutableLiveData<User>(null)

    var user = repository.usuarioConectado
    /*
    val user: LiveData<User>
        get() = _user

    var userInSession: User? = null
        set(value) {
            Log.d("usuariosss", "Me llegaa"+value.toString())
            field = value
            _user.value = value!!
        }
        /
     */

    init{
        Log.d("usuariosss", "Me creo con "+user.value.toString())
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
                return HomeViewModel(
                    (application as TuOnceApplication).appContainer.repository,
                ) as T
            }
        }
    }
}
