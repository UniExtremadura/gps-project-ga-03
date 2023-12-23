package uex.aseegps.ga03.tuonce.view.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.any
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.Liga
import uex.aseegps.ga03.tuonce.model.Repository
import uex.aseegps.ga03.tuonce.model.User


@ExperimentalCoroutinesApi
class MisLigasViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var mockRepository: Repository

    private lateinit var misLigasViewModel: MisLigasViewModel

    private val usuario = User(
        userId = 1L,
        image = 0,
        name = "UsuarioEjemplo",
        password = "12345",
        points = 100,
        conectado = 1
    )

    fun createBot(userId: Long?, name: String, password: String): User {
        return User(userId, name.hashCode(), name, password, 0, 0)
    }

    fun createEquipo(equipoId: Long?, name: String, presupuesto: Int, userId: Long?, ligaId: Long?): Equipo {
        return Equipo(equipoId, name, presupuesto, userId, ligaId)
    }

    fun createFutbolista(
        futbolistaId: Long?,
        image: Int,
        nombreJugador: String,
        años: String,
        posicion: String,
        valor: Int,
        minutoJugados: Int,
        goles: Int,
        asistencias: Int,
        balonAlArea: Int,
        parada: Int,
        tarjetaAmarilla: Int,
        tarjetaRoja: Int,
        estaEnel11: Int,
        media: Int,
        puntosAportados: Int,
        faltacometidas: Int
    ): Futbolista {
        return Futbolista(
            futbolistaId,
            image,
            nombreJugador,
            años,
            posicion,
            valor,
            minutoJugados,
            goles,
            asistencias,
            balonAlArea,
            parada,
            tarjetaAmarilla,
            tarjetaRoja,
            estaEnel11,
            media,
            puntosAportados,
            faltacometidas,
            1
        )
    }

    @After
    fun tearDown() = runBlocking{
        Dispatchers.resetMain()
    }
    @Before
    fun setUp() = runBlocking{

        Dispatchers.setMain(Dispatchers.Default)

        misLigasViewModel = MisLigasViewModel(mockRepository)
        misLigasViewModel.user = usuario


        `when`(mockRepository.usuarioConectado).thenReturn(MutableLiveData(usuario))
        misLigasViewModel.ligaUsuario = MutableLiveData(Liga(ligaId = 1,
            name="LigaTest",
            partidos = 3,
            userId = 1,
            activa = 1))

        misLigasViewModel.equipoUsuario = MutableLiveData(Equipo(
            equipoId = 1,
            name= "EqTest",
            presupuesto = 10000,
            ligaId = 1,
            userId = 1
        ))
        misLigasViewModel.bot1= MutableLiveData(createBot(1L, "Bot1", "password1"))
        misLigasViewModel.bot2= MutableLiveData(createBot(2L, "Bot2", "password2"))
        misLigasViewModel.bot3= MutableLiveData(createBot(3L, "Bot3", "password3"))

        misLigasViewModel.equipoBot1=
            MutableLiveData(createEquipo(1L, "EquipoBot1", 10000, 1L, 1L))

        misLigasViewModel.equipoBot2=
            MutableLiveData(createEquipo(2L, "EquipoBot2", 10000, 2L, 2L))

        misLigasViewModel.equipoBot3=
            MutableLiveData(createEquipo(3L, "EquipoBot3", 10000, 3L, 3L))

        misLigasViewModel.futbolistasDelEquipoUsuario=
            MutableLiveData(
                listOf(
                    createFutbolista(
                        1L,
                        1,
                        "Jugador1",
                        "25",
                        "Delantero",
                        100,
                        90,
                        20,
                        10,
                        5,
                        2,
                        0,
                        0,
                        0,
                        1,
                        80,
                        15,
                    ),
                )
            )

        misLigasViewModel.futbolistasEquipoBot1=
            MutableLiveData(
                listOf(
                    createFutbolista(
                        1L,
                        1,
                        "Jugador1",
                        "25",
                        "Delantero",
                        100,
                        90,
                        20,
                        10,
                        5,
                        2,
                        0,
                        0,
                        0,
                        1,
                        80,
                        15,
                    ),
                )
            )

        misLigasViewModel.futbolistasEquipoBot2=
            MutableLiveData(
                listOf(
                    createFutbolista(
                        2L,
                        2,
                        "Jugador2",
                        "27",
                        "Centrocampista",
                        120,
                        85,
                        15,
                        25,
                        10,
                        3,
                        1,
                        0,
                        0,
                        1,
                        85,
                        20
                    )
                )
            )


        misLigasViewModel.futbolistasEquipoBot3=
            MutableLiveData(
                listOf(
                    createFutbolista(
                        3L,
                        3,
                        "Jugador3",
                        "23",
                        "Defensa",
                        80,
                        95,
                        5,
                        5,
                        8,
                        1,
                        0,
                        0,
                        0,
                        1,
                        75,
                        10
                    ),
                )
            )
    }


    @Test
    fun `test incremento de jornada en puntuacion liga`() = runBlocking {
        var jornada = 1
        doAnswer {
            jornada += 1
            null
        }.`when`(mockRepository).setLigaId(1L)
        misLigasViewModel.initializeLiga(1L)
        misLigasViewModel.simularPartidosYActualizar(jornada)


        assertEquals(2, jornada)
    }

    @Test
    fun `test sistema de puntuacion liga`() = runBlocking {
        misLigasViewModel.initializeLiga(1L)
        misLigasViewModel.initialize()

        misLigasViewModel.simularPartidosYActualizar(1)


        assertNotNull(misLigasViewModel.equipoUsuario.value)
        assertNotNull(misLigasViewModel.ligaUsuario.value)
    }

    @Test
    fun `test setear se llama correctamente al inicializar viewModel`() =
        runBlocking {
            val userId = usuario.userId!!

            misLigasViewModel.initialize()

            Mockito.verify(mockRepository).setUserid(userId)
        }

    @Test
    fun `test setear la liga se llama correctamente al inicializar viewModel`() =
        runBlocking {
            misLigasViewModel.initializeLiga(1)

            Mockito.verify(mockRepository).setLigaId(1)
        }

    @Test
    fun `test setear inicializa el usuario al inicializar viewModel`() =
        runBlocking {

            misLigasViewModel.initialize()

            assertEquals(1L, mockRepository.usuarioConectado.value?.userId)
        }

    @Test
    fun `test terminar liga`() = runBlocking {
        doAnswer {
            misLigasViewModel.equipoUsuario = MutableLiveData(null)
            misLigasViewModel.ligaUsuario = MutableLiveData(null)
            null // Devuelve null, ya que doAnswer requiere un tipo de retorno
        }.`when`(mockRepository).eliminarLiga()


        misLigasViewModel.initializeLiga(1L)
        misLigasViewModel.initialize()

        misLigasViewModel.eliminarLiga()
        misLigasViewModel.terminarLiga()

        assertNull(misLigasViewModel.equipoUsuario.value)
        assertNull(misLigasViewModel.ligaUsuario.value)
    @Test
    fun `test llamadas del sistema de puntuacion liga`() = runBlocking {
        misLigasViewModel.initializeLiga(1L)
        misLigasViewModel.initialize()

        misLigasViewModel.simularPartidosYActualizar(1)

        // Verifico que se haya simulado el partido de verdad y haya tenido cambios en la base de datos
        Mockito.verify(mockRepository).setLigaId(1L);
        Mockito.verify(mockRepository).setUserid(1L);
        Mockito.verify(mockRepository).marcarActividadCrearLiga(
            User(
                userId = 1,
                image = 0,
                name = usuario.name,
                password = "12345",
                points = 180,
                conectado = 1
            ),
            1
        );
    }

}