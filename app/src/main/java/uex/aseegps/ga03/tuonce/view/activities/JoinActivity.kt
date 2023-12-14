package uex.aseegps.ga03.tuonce.view.activities

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.databinding.ActivityJoinBinding
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.utils.CredentialCheck
import uex.aseegps.ga03.tuonce.view.viewmodels.JoinViewModel


class JoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinBinding

    private val viewModel : JoinViewModel by viewModels { JoinViewModel.Factory }


    companion object {

        const val USERNAME = "JOIN_USERNAME"
        const val PASS = "JOIN_PASS"
        fun start(
            context: Context,
            responseLauncher: ActivityResultLauncher<Intent>
        ) {
            val intent = Intent(context, JoinActivity::class.java)
            responseLauncher.launch(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setUpListeners()
    }

    private fun setUpListeners() {
        with(binding) {
            btJoin.setOnClickListener {
                join()
            }

            loginText.paintFlags = loginText.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            loginText.setOnClickListener {
                val intent = Intent(this@JoinActivity, LoginActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun join() {
        with(binding) {
            val check = CredentialCheck.join(
                etUsername.text.toString(),
                etPasswordOne.text.toString(),
                etPasswordTwo.text.toString()
            )
            if (check.fail) notificarCredencialesIncorrectas(check.msg)
            else {
                lifecycleScope.launch{
                    val id = viewModel.credencialesCorrectas(R.drawable.ic_launcher_background,
                        etUsername.text.toString(),
                        etPasswordOne.text.toString())
                    navigateBackWithResult(
                        User(
                            id,
                            R.drawable.ic_launcher_background,
                            etUsername.text.toString(),
                            etPasswordOne.text.toString()
                        )
                    )
                }
            }
        }
    }
    private suspend fun credencialesCorrectas() : Long{
        with(binding) {
            val user = User(
                null,
                R.drawable.ic_launcher_background,
                etUsername.text.toString(),
                etPasswordOne.text.toString()
            )
            val id = viewModel.insertarNuevoUsuario(user)
            viewModel.crearEquipo(user, id)
            return id
        }
    }

    // Funcion que crea un equipo para un usuario, inicialmente el nombre es el del usuario + FC


    private fun navigateBackWithResult(user: User) {
        val intent = Intent().apply {
            putExtra(USERNAME,user.name)
            putExtra(PASS,user.password)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun notificarCredencialesIncorrectas(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}