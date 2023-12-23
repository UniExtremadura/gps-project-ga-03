package uex.aseegps.ga03.tuonce.view.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import es.unex.giiis.asee.tiviclone.data.Repository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User
import androidx.lifecycle.Observer
import org.mockito.Mockito.`when`


@ExperimentalCoroutinesApi
class MoverAl11ViewNodelTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var viewModel: MoverAl11ViewNodel

    @Mock
    private lateinit var mockRepository: Repository
    private val usuario = User(
        userId = 1L,
        image = 0,
        name = "UsuarioEjemplo",
        password = "12345",
        points = 100,
        conectado = 1
    )
    private val equipo = Equipo(
        equipoId = 1,
        name = "EqTest",
        presupuesto = 10000,
        ligaId = 1,
        userId = 1
    )
    private val futbolista = Futbolista(futbolistaId = 1,
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
        equipoId = null)
    private val jugador = Futbolista( futbolistaId = 2,
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
        equipoId = null)
    private val futbolista2 = Futbolista( futbolistaId = 3,
        image = 0,
        nombreJugador = "Liiing",
        años = "34",
        posicion = "Portero",
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
        equipoId = null)
    val futbolistasEnElEquipo = listOf(jugador, futbolista2, futbolista2)
    @Before
    fun setUp() {
        viewModel = MoverAl11ViewNodel(mockRepository)
        viewModel.user = usuario
        viewModel.equipoUsuario = MutableLiveData(equipo)
        viewModel.futbolistasDelEquipoUsuario = MutableLiveData(futbolistasEnElEquipo)
        `when`(mockRepository.futbolistasDelEquipoUsuario).thenReturn(MutableLiveData(futbolistasEnElEquipo))
        Dispatchers.setMain(Dispatchers.Unconfined)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun `initializeEquipo  equipoId `() = runBlocking {
        viewModel.initializeEquipo()
        verify(mockRepository).setEquipoId(equipo.equipoId!!)
    }
    @Test
    fun `test setear se llama correctamente al inicializar viewModel`() =
    runBlocking {
        val userId = usuario.userId!!
        viewModel.initialize()

        verify(mockRepository).setUserid(userId)
    }
    @Test
    fun `obtenerFutbolistasDelEquipoOrdenados returns ordenado lista de jugadores`() {
        val observer = Mockito.mock(Observer::class.java) as Observer<List<Futbolista>>
        viewModel.futbolistasDelEquipoUsuario.observeForever(observer)
        viewModel.futbolistasDelEquipoUsuario = MutableLiveData(futbolistasEnElEquipo)
        verify(observer).onChanged(futbolistasEnElEquipo)
    }
    @Test
    fun `testeo de modificar los jugadores`() = runBlocking {

        viewModel.modificarDatos(futbolista, jugador)

        verify(mockRepository).modificarDatos(futbolista, jugador)
    }

    @Test
    fun `testeo de no cambio`() = runBlocking {
        viewModel.noCambio(futbolista, jugador)

        verify(mockRepository).noCambio(futbolista, jugador)
    }


}