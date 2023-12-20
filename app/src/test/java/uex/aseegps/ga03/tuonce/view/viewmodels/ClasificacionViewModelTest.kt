package uex.aseegps.ga03.tuonce.view.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import es.unex.giiis.asee.tiviclone.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import uex.aseegps.ga03.tuonce.model.Liga
import uex.aseegps.ga03.tuonce.model.User

@ExperimentalCoroutinesApi
class ClasificacionViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var viewModel: ClasificacionViewModel

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

    private val liga = Liga(
        ligaId = 1L,
        name = "Liga Ejemplo",
        partidos = 10,
        userId = 123L,
        activa = 1L
    )

    @Before
    fun setUp() {
        viewModel = ClasificacionViewModel(mockRepository)
        viewModel.user = usuario
        Dispatchers.setMain(Dispatchers.Unconfined)

    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `testsetear se llama correctamente al inicializar ClasificacionViewModel`() =
    runBlocking {
        val userId = usuario.userId!!

        viewModel.initialize()

        verify(mockRepository).setUserid(userId)
    }

    @Test
    fun `initializeLiga llama a setLigaId en el repositorio con el ID correcto`() {
        val ligaId = liga.ligaId!!

        viewModel.initializeLiga(ligaId)

        verify(mockRepository).setLigaId(ligaId)
    }

}