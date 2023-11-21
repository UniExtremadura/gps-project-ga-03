package uex.aseegps.ga03.tuonce.model

data class Futbolista(
    val nombreJugador: String,
    val años: String,
    val posicion: String,
    val valor: Int,
    val minutoJugados: Int,
    val goles: Int,
    val asistencias: Int,
    val balonAlArea: Int,
    val parada: Int,
    val tarjetaAmarilla: Int,
    val tarjetaRoja: Int,
    val media: Int,
    val puntosAportados: Int,
    val faltacometidas: Int
)