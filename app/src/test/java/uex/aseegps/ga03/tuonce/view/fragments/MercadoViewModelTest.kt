import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.Repository
import uex.aseegps.ga03.tuonce.view.viewmodels.MercadoViewModel

class MercadoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var mockRepository: Repository

    private lateinit var mercadoViewModel: MercadoViewModel

    val futbolista1 = Futbolista(
        futbolistaId = 1,
        image = 0,
        nombreJugador = "Cristiano Ronaldo",
        años = "36",
        posicion = "Delantero",
        varor = 100,
        minutoJugados = 90,
        goles = 2,
        asistencias = 1,
        balonAlArea = 5,
        parada = 0,
        tarjetaAmarilla = 0,
        tarjetaRoja = 0,
        estaEnel11 = 1,
        media = 85,
        puntosAportados = 4,
        faltacometidas = 3,
        equipoId = null
    )

    val futbolista2 = Futbolista(
        futbolistaId = 2,
        image = 0,
        nombreJugador = "Lionel Messi",
        años = "34",
        posicion = "Delantero",
        varor = 120,
        minutoJugados = 90,
        goles = 3,
        asistencias = 2,
        balonAlArea = 4,
        parada = 0,
        tarjetaAmarilla = 0,
        tarjetaRoja = 0,
        estaEnel11 = 1,
        media = 88,
        puntosAportados = 2,
        faltacometidas = 2,
        equipoId = null
    )

    val futbolista3 = Futbolista(
        futbolistaId = 3,
        image = 0,
        nombreJugador = "Neymar Jr",
        años = "29",
        posicion = "Extremo",
        varor = 80,
        minutoJugados = 90,
        goles = 1,
        asistencias = 3,
        balonAlArea = 6,
        parada = 0,
        tarjetaAmarilla = 0,
        tarjetaRoja = 0,
        estaEnel11 = 1,
        media = 82,
        puntosAportados = 3,
        faltacometidas = 4,
        equipoId = null
    )
    val listaFutbolistas = listOf(futbolista1, futbolista2, futbolista3)
    @Before
    fun setUp() {
        val futbolistasLiveData = MutableLiveData(listaFutbolistas)
        `when`(mockRepository.futbolistas).thenReturn(futbolistasLiveData)

        mercadoViewModel = MercadoViewModel(mockRepository)
    }

    @Test
    fun `test obtenerJugadoresOrdenadosPorPuntuacion`() = runBlocking {

        val result =  mercadoViewModel.obtenerJugadoresOrdenadosPorPuntuacion()

        assertEquals(listaFutbolistas.sortedByDescending { it.puntosAportados }, result)
    }

}