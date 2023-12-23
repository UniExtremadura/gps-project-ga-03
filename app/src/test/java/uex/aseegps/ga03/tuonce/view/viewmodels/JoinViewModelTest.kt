package uex.aseegps.ga03.tuonce.view.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.mockito.ArgumentMatchers.any
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mock
import uex.aseegps.ga03.tuonce.R
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.Repository
import uex.aseegps.ga03.tuonce.model.User


@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class JoinViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var mockRepository: Repository

    private lateinit var viewModel: JoinViewModel

    @Before
    fun setUp() {
        viewModel = JoinViewModel(mockRepository)
        viewModel.futbolistas = MutableLiveData(futbolistasEnElEquipo)
        runBlocking {
            `when`(mockRepository.insertarUsuario(usuario)).thenReturn(usuario.userId)
            `when`(mockRepository.insertarEquipo(equipo)).thenReturn(equipo.equipoId)
            `when`(mockRepository.insertarEquipo(equipo)).thenReturn(1L)
            // Simula la respuesta del repositorio
            `when`(mockRepository.insertarEquipo(equipo)).thenReturn(1L)
            futbolistasEnElEquipo.forEach { futbolista ->
                `when`(mockRepository.insertarFutbolista(futbolista)).thenReturn(Unit)
            }
        }
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `obtenerEquipaciones devuelve lista correcta`() {
        val expectedEquipaciones = listOf(R.drawable.equipacion1, R.drawable.equipacion2, R.drawable.equipacion3)
        Mockito.`when`(mockRepository.equipaciones).thenReturn(expectedEquipaciones)

        val actualEquipaciones = viewModel.obtenerEquipaciones()

        assertEquals(expectedEquipaciones, actualEquipaciones)
    }

    @Test
    fun `obtenerEquipaciones maneja lista vacia correctamente`() {
        Mockito.`when`(mockRepository.equipaciones).thenReturn(emptyList())

        val actualEquipaciones = viewModel.obtenerEquipaciones()

        assertTrue(actualEquipaciones.isEmpty())
    }

    val futbolistaa = Futbolista(
        futbolistaId = 1,
        image = 0,
        nombreJugador = "Ling",
        años = "30",
        posicion = "Portero",
        varor = 100,
        minutoJugados = 90,
        goles = 3,
        asistencias = 2,
        balonAlArea = 10,
        parada = 0,
        tarjetaAmarilla = 1,
        tarjetaRoja = 0,
        estaEnel11 = 1,
        media = 85,
        puntosAportados = 10,
        faltacometidas = 2,
        equipoId = 1
    )
    val futbolistaa2 = Futbolista(
        futbolistaId = 2,
        image = 0,
        nombreJugador = "Ling",
        años = "30",
        posicion = "Defensa",
        varor = 100,
        minutoJugados = 90,
        goles = 3,
        asistencias = 2,
        balonAlArea = 10,
        parada = 0,
        tarjetaAmarilla = 1,
        tarjetaRoja = 0,
        estaEnel11 = 1,
        media = 85,
        puntosAportados = 10,
        faltacometidas = 2,
        equipoId = 1
    )
    val futbolistaa3 = Futbolista(
        futbolistaId = 3,
        image = 0,
        nombreJugador = "Ling",
        años = "30",
        posicion = "Defensa",
        varor = 100,
        minutoJugados = 90,
        goles = 3,
        asistencias = 2,
        balonAlArea = 10,
        parada = 0,
        tarjetaAmarilla = 1,
        tarjetaRoja = 0,
        estaEnel11 = 1,
        media = 85,
        puntosAportados = 10,
        faltacometidas = 2,
        equipoId = 1
    )
    val futbolistaa4 = Futbolista(
        futbolistaId = 4,
        image = 0,
        nombreJugador = "Ling",
        años = "30",
        posicion = "Defensa",
        varor = 100,
        minutoJugados = 90,
        goles = 3,
        asistencias = 2,
        balonAlArea = 10,
        parada = 0,
        tarjetaAmarilla = 1,
        tarjetaRoja = 0,
        estaEnel11 = 1,
        media = 85,
        puntosAportados = 10,
        faltacometidas = 2,
        equipoId = 1
    )
    val futbolistaa5 = Futbolista(
        futbolistaId = 5,
        image = 0,
        nombreJugador = "Ling",
        años = "30",
        posicion = "Defensa",
        varor = 100,
        minutoJugados = 90,
        goles = 3,
        asistencias = 2,
        balonAlArea = 10,
        parada = 0,
        tarjetaAmarilla = 1,
        tarjetaRoja = 0,
        estaEnel11 = 1,
        media = 85,
        puntosAportados = 10,
        faltacometidas = 2,
        equipoId = 1
    )
    val futbolistaa6 = Futbolista(
        futbolistaId = 6,
        image = 0,
        nombreJugador = "Ling",
        años = "30",
        posicion = "Centrocampista",
        varor = 100,
        minutoJugados = 90,
        goles = 3,
        asistencias = 2,
        balonAlArea = 10,
        parada = 0,
        tarjetaAmarilla = 1,
        tarjetaRoja = 0,
        estaEnel11 = 1,
        media = 85,
        puntosAportados = 10,
        faltacometidas = 2,
        equipoId = 1
    )
    val futbolistaa7 = Futbolista(
        futbolistaId = 7,
        image = 0,
        nombreJugador = "Ling",
        años = "30",
        posicion = "Centrocampista",
        varor = 100,
        minutoJugados = 90,
        goles = 3,
        asistencias = 2,
        balonAlArea = 10,
        parada = 0,
        tarjetaAmarilla = 1,
        tarjetaRoja = 0,
        estaEnel11 = 1,
        media = 85,
        puntosAportados = 10,
        faltacometidas = 2,
        equipoId = 1
    )
    val futbolistaa8 = Futbolista(
        futbolistaId = 8,
        image = 0,
        nombreJugador = "Ling",
        años = "30",
        posicion = "Centrocampista",
        varor = 100,
        minutoJugados = 90,
        goles = 3,
        asistencias = 2,
        balonAlArea = 10,
        parada = 0,
        tarjetaAmarilla = 1,
        tarjetaRoja = 0,
        estaEnel11 = 1,
        media = 85,
        puntosAportados = 10,
        faltacometidas = 2,
        equipoId = 1
    )
    val futbolistaa9 = Futbolista(
        futbolistaId = 9,
        image = 0,
        nombreJugador = "Ling",
        años = "30",
        posicion = "Delantero",
        varor = 100,
        minutoJugados = 90,
        goles = 3,
        asistencias = 2,
        balonAlArea = 10,
        parada = 0,
        tarjetaAmarilla = 1,
        tarjetaRoja = 0,
        estaEnel11 = 1,
        media = 85,
        puntosAportados = 10,
        faltacometidas = 2,
        equipoId = 1
    )
    val futbolistaa10 = Futbolista(
        futbolistaId = 10,
        image = 0,
        nombreJugador = "Ling",
        años = "30",
        posicion = "Delantero",
        varor = 100,
        minutoJugados = 90,
        goles = 3,
        asistencias = 2,
        balonAlArea = 10,
        parada = 0,
        tarjetaAmarilla = 1,
        tarjetaRoja = 0,
        estaEnel11 = 1,
        media = 85,
        puntosAportados = 10,
        faltacometidas = 2,
        equipoId = 1
    )
    val futbolistaa11 = Futbolista(
        futbolistaId = 11,
        image = 0,
        nombreJugador = "Ling",
        años = "30",
        posicion = "Delantero",
        varor = 100,
        minutoJugados = 90,
        goles = 3,
        asistencias = 2,
        balonAlArea = 10,
        parada = 0,
        tarjetaAmarilla = 1,
        tarjetaRoja = 0,
        estaEnel11 = 1,
        media = 85,
        puntosAportados = 10,
        faltacometidas = 2,
        equipoId = 1
    )
    val futbolistasEnElEquipo = listOf(
        futbolistaa,
        futbolistaa2,
        futbolistaa4,
        futbolistaa5,
        futbolistaa6,
        futbolistaa7,
        futbolistaa8,
        futbolistaa9,
        futbolistaa10,
        futbolistaa11
    )

    val equipo = Equipo(
        equipoId = 1,
        name = "EqTest",
        presupuesto = 10000,
        ligaId = 1,
        userId = 1
    )
    private val usuario = User(
        userId = 1L,
        image = 0,
        name = "UsuarioEjemplo",
        password = "12345",
        points = 100,
        conectado = 1
    )

    @Test
    fun `obtenerEscudos devuelve lista correcta`() {
        val expectedEscudos = listOf(R.drawable.escudo1, R.drawable.escudo2, R.drawable.escudo3)
        Mockito.`when`(mockRepository.escudos).thenReturn(expectedEscudos)

        val actualEscudos = viewModel.obtenerEscudos()

        assertEquals(expectedEscudos, actualEscudos)
    }

    @Test
    fun `obtenerEscudos maneja lista vacia correctamente`() {
        Mockito.`when`(mockRepository.escudos).thenReturn(emptyList())

        val actualEscudos = viewModel.obtenerEscudos()

        assertTrue(actualEscudos.isEmpty())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `insertarNuevoUsuario insertar usuario y que coincida el id`() = runBlocking {

        val resultado = viewModel.insertarNuevoUsuario(usuario)

        verify(mockRepository).insertarUsuario(usuario)

        assertEquals(usuario.userId, resultado)
    }

    @Test
    fun `insertarEquipo insertar equipo y coincida el id`() = runBlocking {

        val resultado = viewModel.insertarEquipo(equipo)

        // Verifica que se haya llamado a insertarEquipo con el equipo correcto
        verify(mockRepository).insertarEquipo(equipo)

        // Verifica el resultado
        assertEquals(equipo.equipoId, resultado)
    }

    @Test
    fun `actualizarFutbolista actualizar futbolista en repositorio`() = runBlocking {

        viewModel.actualizarFutbolista(futbolistaa)

        // Verifica que se haya llamado a actualizarFutbolista del repositorio con el futbolista correcto
        verify(mockRepository).actualizarFutbolista(futbolistaa)
    }

    @Test
    fun `insertarFutbolista insertar futbolista en el repositorio`() = runBlocking {
        // Ejecuta la función bajo prueba
        viewModel.insertarFutbolista(futbolistaa)
        // Verifica que se haya llamado a insertarFutbolista del repositorio con el futbolista correcto
        verify(mockRepository).insertarFutbolista(futbolistaa)
    }

    @Test
    fun `ponerFutbolistasAlEquipo asigna futbolistas a un equipo`() {
        runBlocking {
            // Ejecuta la función que deseas probar.
            viewModel.ponerFutbolistasAlEquipo(equipo.equipoId!!)
            // Verifica que los futbolistas se hayan asignado correctamente al equipo.
            for (futbolista in futbolistasEnElEquipo) {
                if (futbolista.estaEnel11 == 0 && futbolista.equipoId == equipo.equipoId) {
                    verify(mockRepository).actualizarFutbolista(futbolista)
                }
            }
        }
    }

    @Test
    fun `insertarEquipoUsuario inserta un equipo correctamente`() {
        runBlocking {
            // Llamar al método que deseas probar
            viewModel.insertarEquipoUsuario(equipo)
            // Verificar que se llamó a insertarEquipo con el nuevo equipo
            verify(mockRepository, Mockito.times(1)).insertarEquipo(equipo)
        }
    }
}


