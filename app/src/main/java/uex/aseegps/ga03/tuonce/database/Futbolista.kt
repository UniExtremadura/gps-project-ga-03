package uex.aseegps.ga03.tuonce.database

import java.io.Serializable

data class Futbolista(
    var nombreJugador: String,
    var años: String,
    var posicion: String,
    var valor: Int,
    var minutoJugados: Int,
    var goles: Int,
    var asistencias: Int,
    var balonAlArea: Int,
    var parada: Int,
    var tarjetaAmarilla: Int,
    var tarjetaRoja: Int,
    var media: Int,
    var puntosAportados: Int,
    var faltacometidas: Int,
) : Serializable
