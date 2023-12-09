package uex.aseegps.ga03.tuonce.view.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uex.aseegps.ga03.tuonce.model.User

class HomeViewModel: ViewModel() {
    private val _user = MutableLiveData<User>(null)
    val user: LiveData<User>
        get() = _user

    var userInSession: User? = null
        set(value) {
            field = value
            _user.value = value!!
        }
}
