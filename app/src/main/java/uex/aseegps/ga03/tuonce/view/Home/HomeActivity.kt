package uex.aseegps.ga03.tuonce.view.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUI()
    }

    fun setUpUI() {
        binding.bottomNavigation.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.actividadFragment,
                R.id.clasificacionFragment,
                R.id.equipoFragment,
                R.id.mercadoFragment,
                R.id.misLigasFragment
            )
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Encuentra el TextView por su ID
        val titleTextView = findViewById<TextView>(R.id.toolbar_title)

        val toolbarIconBack: ImageButton = findViewById(R.id.toolbar_icon_back)

        toolbarIconBack.setOnClickListener {
            onBackPressed()
        }

        // Configura un oyente para los cambios de navegación
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Actualiza el título según la opción seleccionada en la barra de navegación inferior
            titleTextView.text = destination.label
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}