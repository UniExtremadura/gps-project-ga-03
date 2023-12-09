package uex.aseegps.ga03.tuonce.view.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.model.Futbolista
import android.widget.TextView
import uex.aseegps.ga03.tuonce.databinding.ActivityDetalleFutbolistaBinding

class DetalleFutbolistaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleFutbolistaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleFutbolistaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        escribirFutbolista()

        // Configura el OnClickListener para el botón de retroceso
        binding.buttonBack.setOnClickListener {
            // Navega hacia atrás cuando se hace clic en el botón
            onBackPressed()
        }
    }


    fun escribirFutbolista(){
        val futbolista = intent.getSerializableExtra("nom") as Futbolista

        val imgFutbolista = findViewById<ImageView>(R.id.imgFutbolistaAlOnce)
        imgFutbolista.setImageResource(futbolista.image)
        val idNombreFutbolista = findViewById<TextView>(R.id.idNombreFutbolista)
        idNombreFutbolista.text = getString(R.string.nombreEtiqueta, futbolista.nombreJugador)
        val idPosicionFutbolista = findViewById<TextView>(R.id.idPosicionFutbolista)
        idPosicionFutbolista.text = getString(R.string.posicionEtiqueta, futbolista.posicion)
        val idPrecioFutbolista = findViewById<TextView>(R.id.idPrecioFutbolista)
        idPrecioFutbolista.text = getString(R.string.valorEtiqueta, futbolista.varor.toString())
        val idAños = findViewById<TextView>(R.id.idAños)
        idAños.text = getString(R.string.añosEtiqueta, futbolista.años)
        val idminutosJugados = findViewById<TextView>(R.id.idminutosJugados)
        idminutosJugados.text = getString(R.string.minutosJugadosEtiqueta, futbolista.minutoJugados.toString())
        val idgoles = findViewById<TextView>(R.id.idgoles)
        idgoles.text = getString(R.string.golesEtiqueta, futbolista.goles.toString())
        val idasistencias = findViewById<TextView>(R.id.idasistencias)
        idasistencias.text = getString(R.string.asistenciasEtiqueta, futbolista.asistencias.toString())
        val idbalonAlArea = findViewById<TextView>(R.id.idbalonAlArea)
        idbalonAlArea.text = getString(R.string.balonAlAreaEtiqueta, futbolista.balonAlArea.toString())
        val idparada = findViewById<TextView>(R.id.idparada)
        idparada.text = getString(R.string.paradaEtiqueta, futbolista.parada.toString())
        val idtarjetaAmarilla = findViewById<TextView>(R.id.idtarjetaAmarilla)
        idtarjetaAmarilla.text = getString(R.string.tarjetaAmarillaEtiqueta, futbolista.tarjetaAmarilla.toString())
        val idtarjetaRoja = findViewById<TextView>(R.id.idtarjetaRoja)
        idtarjetaRoja.text = getString(R.string.tarjetaRojaEtiqueta, futbolista.tarjetaRoja.toString())
        val idmedia = findViewById<TextView>(R.id.idmedia)
        idmedia.text = getString(R.string.mediaEtiqueta, futbolista.media.toString())
        val idpuntosAportados = findViewById<TextView>(R.id.idpuntosAportados)
        idpuntosAportados.text = getString(R.string.puntosAportadosEtiqueta, futbolista.puntosAportados.toString())
        val idfaltasCometidas = findViewById<TextView>(R.id.idfaltasCometidas)
        idfaltasCometidas.text = getString(R.string.faltasCometidasEtiqueta, futbolista.faltacometidas.toString())
    }

}