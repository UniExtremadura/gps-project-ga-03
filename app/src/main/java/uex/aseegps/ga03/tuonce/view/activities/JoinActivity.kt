package uex.aseegps.ga03.tuonce.view.activities

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
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

    private var escudoSeleccionado: Int = R.drawable.escudo1

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

        val escudos = viewModel.obtenerEscudos()
        val llEscudos = binding.llEscudos

        escudos.forEachIndexed { index, escudo ->
            val imageView = ImageView(this).apply {
                id = 1000 + index
                layoutParams = LinearLayout.LayoutParams(200, 200)
                scaleType = ImageView.ScaleType.CENTER_CROP
                setPadding(8, 8, 8, 8)
                setImageResource(escudo)
                setOnClickListener {
                    // Manejar clic en el escudo
                    resaltarEscudoSeleccionado(this)
                    escudoSeleccionado = escudo
                }
            }
            llEscudos.addView(imageView)
            //Mostrar en el logcat el id de todos los escudos
            Log.d("JoinActivity", "Escudo ${imageView.id}")
        }
        setUpListeners()
    }

    private fun resaltarEscudoSeleccionado(view: View) {
        val llEscudos: LinearLayout = binding.llEscudos

        // Eliminar el recuadro verde de todos los elementos del LinearLayout
        for (i in 0 until llEscudos.childCount) {
            llEscudos.getChildAt(i).setBackgroundResource(0)
        }

        // Agregar recuadro verde al elemento seleccionado
        view.setBackgroundResource(R.drawable.border_green) // Asegúrate de tener un drawable con un borde verde
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
                    val id = viewModel.credencialesCorrectas(escudoSeleccionado,
                        etUsername.text.toString(),
                        etPasswordOne.text.toString())
                    navigateBackWithResult(
                        User(
                            id,
                            escudoSeleccionado,
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
                escudoSeleccionado,
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