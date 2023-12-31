import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.any
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.Repository
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.view.viewmodels.MercadoViewModel


@ExperimentalCoroutinesApi
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

    @Test
    fun `test obtenerJugadoresOrdenadosPorPuntuacion`() = runBlocking {

        val result =  mercadoViewModel.obtenerJugadoresOrdenadosPorPuntuacion()

        Assert.assertEquals(listaFutbolistas.sortedByDescending { it.puntosAportados }, result)
    }
    @Before
    fun setUp() {
        val futbolistasLiveData = MutableLiveData(listaFutbolistas)
        `when`(mockRepository.futbolistas).thenReturn(futbolistasLiveData)

        mercadoViewModel = MercadoViewModel(mockRepository)
    }

    private val usuario = User(
    userId = 1,
    image = 0,
    name = "UsuarioEjemplo",
    password = "contraseña123",
    points = 100,
    conectado = 1
    )

    private val fut = Futbolista(futbolistaId = 2,
        image = 0,
        nombreJugador = "Lionel Messi",
        años = "34",
        posicion = "Centrocampista",
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


    private val equipo =  MutableLiveData(Equipo(
        equipoId = 1,
        name = "EquipoEjemplo",
        presupuesto = 50000,
        userId = 1,
        ligaId = 2
    ))

        @Test
    fun `comprarFutbolista con presupuesto suficiente`() = runBlocking {

        val futbolista = fut

        mercadoViewModel.user = usuario

        mercadoViewModel.equipoUsuario =  equipo

        `when`(mockRepository.eliminarFutbolistaDelMercado(any(), any(), any())).thenReturn(null)

        val resultado = mercadoViewModel.comprarFutbolista(futbolista)

        assertTrue(resultado)
    }



    @Test
    fun `comprarFutbolista con presupuesto insuficiente`() = runBlocking {
        val futbolista = fut

        mercadoViewModel.user = usuario

        mercadoViewModel.equipoUsuario =  equipo
        mercadoViewModel.equipoUsuario.value?.presupuesto = futbolista.varor - 100

        val resultado = mercadoViewModel.comprarFutbolista(futbolista)

        assertFalse(resultado)
        verify(mockRepository, never()).eliminarFutbolistaDelMercado(any(), any(), any())
    }

    @Test
    fun `comprarFutbolista con presupuesto lanza llamada del repositorio correctamente`() = runBlocking {
        val futbolista = fut
        mercadoViewModel.user = usuario

        mercadoViewModel.equipoUsuario =  equipo
        mercadoViewModel.comprarFutbolista(futbolista)
        verify(mockRepository, times(1)).eliminarFutbolistaDelMercado(any(), any(), any())
    }

    @Test
    fun `comprarFutbolista sin presupuesto lanza llamada del repositorio correctamente`() = runBlocking {
        val futbolista = fut

        mercadoViewModel.user = usuario

        mercadoViewModel.equipoUsuario =  equipo
        mercadoViewModel.equipoUsuario.value?.presupuesto = futbolista.varor - 100

        mercadoViewModel.comprarFutbolista(futbolista)

        verify(mockRepository, never()).eliminarFutbolistaDelMercado(any(), any(), any())
    }
}
