package uex.aseegps.ga03.tuonce.view.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import uex.aseegps.ga03.tuonce.model.AccionActividad
import uex.aseegps.ga03.tuonce.model.Actividad
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.Repository
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.view.viewmodels.ActividadViewModel
import uex.aseegps.ga03.tuonce.view.viewmodels.MercadoViewModel


@ExperimentalCoroutinesApi
class ActividadViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var mockRepository: Repository

    private lateinit var actividadViewModel: ActividadViewModel
    private val usuario = User(
        userId = 1L,
        image = 0,
        name = "UsuarioEjemplo",
        password = "12345",
        points = 100,
        conectado = 1
    )

    val actividad1 = Actividad(
        actividadId = null,
        accion = AccionActividad.INICIAR_LIGA,
        usuarioActividad = 1,
        futbolistaActividad = null,
        ligaActividad = "Premier League",
        jornadaActividad = null
    )

    val actividad2 = Actividad(
        actividadId = null,
        accion = AccionActividad.COMPRAR_FUTBOLISTA,
        usuarioActividad = 1,
        futbolistaActividad = "Lionel Messi",
        ligaActividad = null,
        jornadaActividad = null
    )

    val actividad3 = Actividad(
        actividadId = null,
        accion = AccionActividad.ACABAR_LIGA,
        usuarioActividad = 1,
        futbolistaActividad = null,
        ligaActividad = "Premier League",
        jornadaActividad = null
    )

    val actividad4 = Actividad(
        actividadId = null,
        accion = AccionActividad.INICIAR_JORNADA,
        usuarioActividad = 1,
        futbolistaActividad = null,
        ligaActividad = "Serie A",
        jornadaActividad = 5
    )

    val listaActividades = MutableLiveData(listOf(actividad1, actividad2, actividad3, actividad4))

    @Before
    fun setUp() {
        actividadViewModel = ActividadViewModel(mockRepository)
        actividadViewModel.user = usuario
        actividadViewModel.actividades = listaActividades

        `when`(mockRepository.usuarioConectado).thenReturn(MutableLiveData(usuario))
    }


    @Test
    fun `test setear se llama correctamente al inicializar viewModel`() {
        runBlocking {
            val userId = usuario.userId!!

            actividadViewModel.initialize()

            Mockito.verify(mockRepository).setUserid(userId)
        }
    }

    @Test
    fun `test setear inicializa el usuario al inicializar viewModel`() {
        runBlocking {
            val userId = usuario.userId!!

            actividadViewModel.initialize()

            assertEquals(1L, mockRepository.usuarioConectado.value?.userId)
        }
    }

    @Test
    fun `test actividades LiveData`() {
        val observer = Mockito.mock(Observer::class.java) as Observer<List<Actividad>>
        actividadViewModel.actividades.observeForever(observer)

        actividadViewModel.actividades = listaActividades

        Mockito.verify(observer).onChanged(listaActividades.value!!)
    }

}