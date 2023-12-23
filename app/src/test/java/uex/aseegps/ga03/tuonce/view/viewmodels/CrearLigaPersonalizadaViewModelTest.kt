package uex.aseegps.ga03.tuonce.view.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.any
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Repository
import uex.aseegps.ga03.tuonce.model.User
import java.util.Arrays


@ExperimentalCoroutinesApi
class CrearLigaPersonalizadaViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var mockRepository: Repository

    private lateinit var crearLigaPersonalizadaViewModel: CrearLigaPersonalizadaViewModel

    private val usuario = User(
        userId = 1L,
        image = 0,
        name = "UsuarioEjemplo",
        password = "12345",
        points = 100,
        conectado = 1
    )

    fun createEquipo(equipoId: Long?, name: String, presupuesto: Int, userId: Long?, ligaId: Long?): Equipo {
        return Equipo(equipoId, name, presupuesto, userId, ligaId)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Default)
        crearLigaPersonalizadaViewModel = CrearLigaPersonalizadaViewModel(mockRepository)
        crearLigaPersonalizadaViewModel.user = usuario
        crearLigaPersonalizadaViewModel.equipoUsuario = MutableLiveData(Equipo(
            equipoId = 1,
            name = "EqTest",
            presupuesto = 10000,
            ligaId = 1,
            userId = 1
        ))

        Mockito.`when`(mockRepository.usuarioConectado).thenReturn(MutableLiveData(usuario))
        Mockito.`when`(mockRepository.equipoUsuario).thenReturn(MutableLiveData(Equipo(
            equipoId = 1,
            name = "EqTest",
            presupuesto = 10000,
            ligaId = 1,
            userId = 1
        )))
        Mockito.`when`(runBlocking { mockRepository.insertarEquipo(any()) } ).thenReturn(1)
    }

    @Test
    fun `test setear se llama correctamente al inicializar viewModel`() =
        runBlocking {
            val userId = usuario.userId!!

            crearLigaPersonalizadaViewModel.initialize()

            Mockito.verify(mockRepository).setUserid(userId)
        }

    @Test
    fun `test setear inicializa el usuario al inicializar viewModel`() =
        runBlocking {

            crearLigaPersonalizadaViewModel.initialize()

            Assert.assertEquals(1L, mockRepository.usuarioConectado.value?.userId)
        }

    @Test
    fun `test llamadas al crear la liga`() =
        runBlocking {
            crearLigaPersonalizadaViewModel.initialize()

            Assert.assertEquals(1L, mockRepository.usuarioConectado.value?.userId)
            crearLigaPersonalizadaViewModel.crearEquipoEnLiga(usuario, 1, 1)

            // Se debe llamar 11 veces, una por jugador
            Mockito.verify(mockRepository, times(11)).insertarFutbolista(any())

       }
    @Test
    fun `test crearLiga cuando NO es correcto`() = runBlocking {
        val nombreLiga = ""
        val numJornadas = "abc" // Mal
        val imagenesBots = mutableListOf(1, 2, 3)

        val result = crearLigaPersonalizadaViewModel.crearLiga(nombreLiga, numJornadas, imagenesBots)

        Assert.assertFalse(result)
    }

    @Test
    fun `test crearLiga cuando SI es correcta`() = runBlocking {
        val nombreLiga = "ligacorrectatest"
        val numJornadas = "2"
        val imagenesBots = mutableListOf(1, 2, 3)

        val result = crearLigaPersonalizadaViewModel.crearLiga(nombreLiga, numJornadas, imagenesBots)

        Assert.assertTrue(result)
    }

    @Test
    fun `test asignarLigaAlEquipo`() = runBlocking {
        // Arrange
        val idLiga = 1L

        crearLigaPersonalizadaViewModel.asignarLigaAlEquipo(idLiga)

        Assert.assertEquals(idLiga,Equipo(
            equipoId = 1,
            name = "EqTest",
            presupuesto = 10000,
            ligaId = 1,
            userId = 1
        ).equipoId)

        Mockito.verify(mockRepository).actualizarEquipo(Equipo(
            equipoId = 1,
            name = "EqTest",
            presupuesto = 10000,
            ligaId = 1,
            userId = 1
        ))
    }
}