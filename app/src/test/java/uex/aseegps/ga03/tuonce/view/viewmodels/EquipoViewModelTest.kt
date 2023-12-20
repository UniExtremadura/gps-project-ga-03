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


@ExperimentalCoroutinesApi
class EquipoViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var mockRepository: Repository
    private lateinit var viewModel: EquipoViewModel
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

    @Before
    fun setUp() {
        viewModel = EquipoViewModel(mockRepository)
        viewModel.user = usuario
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test setear se llama correctamente al inicializar viewModel` () =
    runBlocking {
        val userId = usuario.userId!!

        viewModel.initialize()

        verify(mockRepository).setUserid(userId)
    }
    @Test
    fun `initializeEquipo sets equipoId correctly`() =
    runBlocking {
        viewModel.initializeEquipo(equipo.equipoId!!)
        verify(mockRepository).setEquipoId(equipo.equipoId!!)
    }
    @Test
    fun `actualizarEquipo calls repository update method`() = runBlocking {
        viewModel.actualizarEquipo(equipo)
        verify(mockRepository).actualizarEquipo(equipo)
    }
}