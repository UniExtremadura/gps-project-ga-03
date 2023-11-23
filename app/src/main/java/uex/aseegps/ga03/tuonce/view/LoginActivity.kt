package uex.aseegps.ga03.tuonce.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import uex.aseegps.ga03.tuonce.databinding.ActivityLoginBinding
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.utils.CredentialCheck
import uex.aseegps.ga03.tuonce.view.Home.HomeActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: TuOnceDatabase
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
                        "New user ($name/$password) created",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //view binding and set content view
        binding = ActivityLoginBinding.inflate(layoutInflater)
        db = TuOnceDatabase.getInstance(applicationContext)!!
        setContentView(binding.root)
        setUpListeners()
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
            lifecycleScope.launch{
                db?.userDao()?.desconectarTodos()
                val user = db?.userDao()?.findByName(binding.etUsername.text.toString())
                if (user != null) {
                    val check =
                        CredentialCheck.passwordOk(binding.etPassword.text.toString(),
                            user.password)
                    if (check.fail) notifyInvalidCredentials(check.msg)
                    else{
                        db?.userDao()?.conectar(binding.etUsername.text.toString())
                        navigateToHomeActivity(user!!, check.msg)
                    }
                }
                else notifyInvalidCredentials("Invalid username")
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

}