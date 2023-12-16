package uex.aseegps.ga03.tuonce.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import uex.aseegps.ga03.tuonce.databinding.ActivityLoginBinding
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import kotlinx.coroutines.launch
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.utils.CredentialCheck
import uex.aseegps.ga03.tuonce.view.viewmodels.HomeViewModel
import uex.aseegps.ga03.tuonce.view.viewmodels.LoginViewModel
import uex.aseegps.ga03.tuonce.view.viewmodels.MisLigasViewModel
import java.lang.Thread.sleep

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel : LoginViewModel by viewModels { LoginViewModel.Factory }
    private val homeViewModel: HomeViewModel by viewModels() {HomeViewModel.Factory}

    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                with(result.data) {
                    val name = this?.getStringExtra(JoinActivity.USERNAME).orEmpty()
                    val password = this?.getStringExtra(JoinActivity.PASS).orEmpty()


                    with(binding) {
                        etPassword.setText(password)
                        etUsername.setText(name)
                    }

                    Toast.makeText(
                        this@LoginActivity,
                        "Tu usuario ($name/$password) ha sido creado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setUpListeners()
        viewModel.usuario.observe(this) {     user->
            if (user != null) {
            val check =
                CredentialCheck.passwordOk(binding.etPassword.text.toString(),
                    user.password)
            if (check.fail) notifyInvalidCredentials(check.msg)
            else{
                navigateToHomeActivity(user!!, check.msg)
            }
        }
            else notifyInvalidCredentials("Nombre de usuario incorrecto")
        }
        readSettings()
    }


    private fun setUpListeners() {
        with(binding) {
            btLogin.setOnClickListener {
                checkLogin()
            }
            btJoin.setOnClickListener {
               navigateToJoin()
            }
        }
    }

    private fun navigateToHomeActivity(user: User, msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
    private fun checkLogin(){
        val check = CredentialCheck.login(binding.etUsername.text.toString(),
            binding.etPassword.text.toString())
        if (!check.fail){
            Toast.makeText(this, "Comprobando credenciales...", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch{
                viewModel.initializeNombre(binding.etUsername.text.toString())
            }
        }
        else notifyInvalidCredentials(check.msg)
    }

    private fun navigateToJoin() {
        JoinActivity.start(this, responseLauncher)
    }

    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun readSettings() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this).all
        val rememberme = preferences["guardarsesion"] as Boolean? ?: false
        val username = preferences["nombreusuario"] as String? ?: ""
        val password = preferences["contraseña"] as String? ?: ""

        if (rememberme) { binding.etUsername.setText(username)
            binding.etPassword.setText(password)
        }
    }

}