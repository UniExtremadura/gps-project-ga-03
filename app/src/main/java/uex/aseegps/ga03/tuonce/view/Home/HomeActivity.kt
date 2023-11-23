package uex.aseegps.ga03.tuonce.view.Home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.graphics.Color
import android.util.AttributeSet
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

    private val blanco = Color.WHITE
    private val negro = Color.BLACK

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

        val botonBack = findViewById<ImageButton>(R.id.toolbar_icon_back)
        botonBack.setOnClickListener {
            onBackPressed()
        }

        val botonPreferences = findViewById<ImageButton>(R.id.toolbar_icon_preferencias)
        botonPreferences.setOnClickListener {
            navController.navigate(R.id.action_home_to_settingsFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->

            val title = findViewById<TextView>(R.id.toolbar_title)
            title.text = destination.label
            title.setTextColor(Color.parseColor("#D70000"))

            if(destination.id == R.id.settingsFragment) {
                binding.bottomNavigation.visibility = View.GONE
                binding.toolbarIconPreferencias.visibility = View.GONE
                binding.toolbarIconBack.visibility = View.GONE
                binding.toolbar.setBackgroundColor(blanco)
                title.text = ""
            } else if(destination.id == R.id.preguntasFrecuentesFragment) {
                binding.bottomNavigation.visibility = View.GONE
                binding.toolbarIconPreferencias.visibility = View.GONE
                binding.toolbarIconBack.visibility = View.GONE
                binding.toolbar.setBackgroundColor(blanco)
                title.text = ""
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
                binding.toolbarIconPreferencias.visibility = View.VISIBLE
                binding.toolbarIconBack.visibility = View.VISIBLE
                binding.toolbar.setBackgroundColor(negro)
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
}