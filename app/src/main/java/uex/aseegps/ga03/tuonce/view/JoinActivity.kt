package uex.aseegps.ga03.tuonce.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.ActivityJoinBinding
import uex.aseegps.ga03.tuonce.databinding.ActivityLoginBinding
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.utils.CredentialCheck
import uex.aseegps.ga03.tuonce.database.dummyFutbolista
import uex.aseegps.ga03.tuonce.model.Futbolista
import kotlin.random.Random

class JoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinBinding
    private lateinit var db: TuOnceDatabase
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
        db = TuOnceDatabase.getInstance(applicationContext)!!
        setUpListeners()
    }

    private fun setUpListeners() {
        with(binding) {
            btJoin.setOnClickListener {
                join()
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
            if (check.fail) notifyInvalidCredentials(check.msg)
            else {
                lifecycleScope.launch{
                    val user = User(
                        null,
                        R.drawable.ic_launcher_background,
                        etUsername.text.toString(),
                        etPasswordOne.text.toString()
                    )
                    val id = db?.userDao()?.insert(user)
                    crearEquipo(user, id)
                    navigateBackWithResult(User(id,R.drawable.ic_launcher_background, etUsername.text.toString(),etPasswordOne.text.toString()))
                }
            }
        }
    }

    // Funcion que crea un equipo para un usuario, inicialmente el nombre es el del usuario + FC
    private fun crearEquipo(user : User, id : Long?){
        Toast.makeText(this, "Creandote un equipo...", Toast.LENGTH_SHORT).show()
        // Crear una instancia de la clase Random
        val random = Random
        // Generar un número aleatorio entre 15,000,000 (inclusive) y 20,000,000 (inclusive)
        val randomNumber = random.nextInt(15_000_000, 20_000_001)
        val nuevoEquipo = Equipo(
            null,
            name = user.name,
            userId = id,
            ligaId = null,
            presupuesto = randomNumber,
        )
        lifecycleScope.launch{
            val equipoId = db?.equipoDao()?.insert(nuevoEquipo)
            var jugadoresSeleccionados = 0
            // Inserto todos los jugadores en la base de datos
            dummyFutbolista.shuffled().forEachIndexed{ index, futbolista ->
                if (index < 11) {
                    futbolista.equipoId = equipoId
                    futbolista.estaEnel11 = 1
                } else {
                    futbolista.equipoId = null
                }
                db?.futbolistaDao()?.insert(futbolista)
            }
            var futbolistas: List<Futbolista>? = db?.futbolistaDao()?.findAll()
            futbolistas?.shuffled()?.take(7)?.forEachIndexed{ index, futbolista ->
                if (futbolista.estaEnel11 == 0 && futbolista.equipoId == null) {
                    futbolista.equipoId = equipoId
                    db?.futbolistaDao()?.update(futbolista)
                }
            }
        }
    }


    private fun navigateBackWithResult(user: User) {
        val intent = Intent().apply {
            putExtra(USERNAME,user.name)
            putExtra(PASS,user.password)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}