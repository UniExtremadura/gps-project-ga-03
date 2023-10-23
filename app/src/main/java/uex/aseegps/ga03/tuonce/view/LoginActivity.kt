package uex.aseegps.ga03.tuonce.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import uex.aseegps.ga03.tuonce.databinding.ActivityLoginBinding
import android.os.Bundle
import android.widget.Toast
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.utils.CredentialCheck
import uex.aseegps.ga03.tuonce.view.Home.HomeActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //view binding and set content view
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpListeners()
    }

    private fun setUpListeners() {
        with(binding) {

            btLogin.setOnClickListener {
                val check = CredentialCheck.login(etUsername.text.toString(), etPassword.text.toString())

                if (check.fail) notifyInvalidCredentials(check.msg)
                else navigateToHomeActivity(User(etUsername.text.toString(), etPassword.text.toString()), check.msg)
            }
        }
    }

    private fun navigateToHomeActivity(user: User, msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        //MainActivity.start(this, user)
    }


    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}