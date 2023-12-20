package uex.aseegps.ga03.tuonce.view.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.Repository
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.view.viewmodels.PlantillaViewModel

@ExperimentalCoroutinesApi
class PlantillaViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var mockRepository: Repository

    private lateinit var viewModel: PlantillaViewModel

    @Before
    fun setUp() {
        viewModel = PlantillaViewModel(mockRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    private val usuario = User(
        userId = 1,
        image = 0,
        name = "UsuarioEjemplo",
        password = "contraseña123",
        points = 100,
        conectado = 1
    )

    private val futbolista = Futbolista(futbolistaId = 2,
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
    fun `al vender un futbolista, el presupuesto del equipo aumenta en el valor del futbolista`() = runBlocking {
        // Configura un futbolista y un equipo con presupuesto inicial
        val valorFutbolista = 1000 // El valor del futbolista
        val presupuestoInicialEquipo = 5000 // Presupuesto inicial del equipo
        val futbolista = futbolista.copy(varor = valorFutbolista)
        val equipoModificado = equipo.value!!.copy(presupuesto = presupuestoInicialEquipo + valorFutbolista)

        // Configura el LiveData del equipo con el equipo modificado
        val equipoLiveData = MutableLiveData<Equipo>()
        equipoLiveData.value = equipoModificado

        // Configura el ViewModel con el usuario y el LiveData del equipo
        viewModel.user = usuario
        viewModel.equipoUsuario = equipoLiveData

        // Simula la venta del futbolista
        viewModel.venderFutbolistaDelequipo(futbolista, equipoModificado, usuario)

        // Verifica que el presupuesto del equipo ha aumentado en el valor del futbolista
        val presupuestoEsperado = presupuestoInicialEquipo + valorFutbolista
        val presupuestoActual = viewModel.equipoUsuario.value!!.presupuesto
        assertEquals(presupuestoEsperado, presupuestoActual)

        // Verifica que se llamó al método del repositorio
        verify(mockRepository).venderFutbolistaDelequipo(futbolista, equipoModificado, usuario)
    }

    @Test
    fun `venderFutbolistaDelequipo llama al método del repositorio`() = runTest {

        // Crear un futbolista, equipo y usuario ficticios para la prueba
        val futbolista = futbolista
        viewModel.user = usuario
        viewModel.equipoUsuario = equipo

        // Llamar al método del ViewModel
        viewModel.venderFutbolistaDelequipo(futbolista, equipo.value, usuario)

        // Verificar que el método correspondiente del Repository fue llamado
        verify(mockRepository, times(1)).venderFutbolistaDelequipo(any(), any(), any())
    }

    @Test
    fun `venderFutbolistaDelequipo no se llama si el equipo es nulo`() = runBlocking {
        println("Iniciando test para vender futbolista cuando el equipo es nulo")

        // Configura el ViewModel con un usuario válido y un equipo nulo
        viewModel.user = usuario
        viewModel.equipoUsuario = MutableLiveData(null) // Asignando equipo nulo

        // Crear un futbolista ficticio para la prueba
        val futbolista = futbolista

        println("Intentando vender futbolista con equipo nulo")
        // Intenta vender un futbolista con un equipo nulo
        viewModel.venderFutbolistaDelequipo(futbolista, null, usuario)

        println("Verificando que el método del repositorio no fue llamado debido a equipo nulo")
        // Verifica que el método del repositorio NO fue llamado debido a equipo nulo
        verify(mockRepository, never()).venderFutbolistaDelequipo(any(), any(), any())

        println("Test finalizado correctamente")
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
