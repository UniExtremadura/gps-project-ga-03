package uex.aseegps.ga03.tuonce.utils

import org.junit.Test
import org.junit.Assert.assertEquals
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.utils.SortPlayers.calcularPuntuacion

class CalculoPuntuacionTest {

    @Test
    fun `calcularPuntuacion retorna el valor correcto`() {
        // Crear un futbolista con estadísticas específicas
        val futbolista = Futbolista(futbolistaId = 2,
            image = 0,
            nombreJugador = "Lionel Messi",
            años = "34",
            posicion = "Centrocampista",
            varor = 100,
            minutoJugados = 90, // 90 / 10 = 9
            goles = 2, // 2 * 5 = 10
            asistencias = 1, // 1 * 3 = 3
            balonAlArea = 5, // 5 * 2 = 10
            parada = 0, // 0 * 4 = 0
            tarjetaAmarilla = 0, // 0 * 2 = 0
            tarjetaRoja = 0, // 0 * 5 = 0
            estaEnel11 = 1,
            media = 85,
            puntosAportados = 4,
            faltacometidas = 3, // 3 * 1 = 3
            equipoId = null
        )

        // Calcular la puntuación esperada
        val puntuacionEsperada = (10 + 3 + 9 + 10 - 3 + 85 + 4) / 11

        // Ejecutar el método a probar
        val puntuacionCalculada = calcularPuntuacion(futbolista)

        // Verificar que la puntuación calculada sea igual a la esperada
        assertEquals(puntuacionEsperada, puntuacionCalculada)
    }
}
