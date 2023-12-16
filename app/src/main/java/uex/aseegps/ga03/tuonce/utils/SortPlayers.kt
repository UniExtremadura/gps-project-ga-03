package uex.aseegps.ga03.tuonce.utils

import uex.aseegps.ga03.tuonce.model.Futbolista

object SortPlayers {

    fun calcularPuntuacion(futbolista: Futbolista): Int {
        var puntuacion = 0
        puntuacion += futbolista.goles * 5
        puntuacion += futbolista.asistencias * 3
        puntuacion += futbolista.minutoJugados / 10
        puntuacion += futbolista.parada * 4
        puntuacion += futbolista.balonAlArea * 2
        puntuacion -= futbolista.tarjetaAmarilla * 2
        puntuacion -= futbolista.tarjetaRoja * 5
        puntuacion += futbolista.media
        puntuacion += futbolista.puntosAportados
        puntuacion -= futbolista.faltacometidas * 1
        return puntuacion/11
    }

    fun  clasificarJugadores(listaFutbolistas: List<Futbolista>): List<Futbolista> {
        return listaFutbolistas.sortedByDescending { calcularPuntuacion(it) }
    }

}
