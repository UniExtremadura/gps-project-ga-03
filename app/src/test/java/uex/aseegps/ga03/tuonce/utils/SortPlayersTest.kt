package uex.aseegps.ga03.tuonce.utils

import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.utils.SortPlayers.clasificarJugadores

class SortPlayersTest {
    @Test
    fun `clasificarJugadores ordena correctamente jugadores con diferentes puntuaciones`() {
        val futbolista1 = Futbolista(null, R.drawable.messi, "Lionel Messi", "34", "Delantero", 1500, 0, 0, 0, 0, 0, 0, 0, 0, 99, 15,0,null) // Puntuación 15
        val futbolista2 = Futbolista(null, R.drawable.cristiano,"Cristiano Ronaldo", "36", "Delantero", 12000, 0, 0, 0, 0, 0, 0, 0, 0, 98,50, 0,null) // Puntuación 50
        val futbolista3 = Futbolista(null, R.drawable.neymar,"Neymar Jr.", "29", "Centrocampista", 1000, 0, 0, 0, 0, 0, 0, 0, 0, 97,100,0, null) // Puntuación 100

        val listaFutbolistas = listOf(futbolista3, futbolista2, futbolista1)
        val listaClasificada = clasificarJugadores(listaFutbolistas)

        // Comprobamos que el orden es correcto
        assertTrue(listaClasificada[0] == futbolista3)
        assertTrue(listaClasificada[1] == futbolista2)
        assertTrue(listaClasificada[2] == futbolista1)
    }

    @Test
    fun `clasificarJugadores maneja correctamente jugadores con puntuaciones iguales`() {
        val futbolista1 = Futbolista(null, R.drawable.messi, "Lionel Messi", "34", "Delantero", 1500, 0, 0, 0, 0, 0, 0, 0, 0, 99, 100,0,null)
        val futbolista2 = Futbolista(null, R.drawable.cristiano,"Cristiano Ronaldo", "36", "Delantero", 12000, 0, 0, 0, 0, 0, 0, 0, 0, 98,100, 0,null)
        val futbolista3 = Futbolista(null, R.drawable.neymar,"Neymar Jr.", "29", "Centrocampista", 1000, 0, 0, 0, 0, 0, 0, 0, 0, 97,100,0, null)

        val listaFutbolistas = listOf(futbolista2, futbolista1, futbolista3)
        val listaClasificada = clasificarJugadores(listaFutbolistas)

        // Comprobamos que al ser los mismos puntos se ordenan por nombre
        assertEquals(listaFutbolistas, listaClasificada)
    }
}