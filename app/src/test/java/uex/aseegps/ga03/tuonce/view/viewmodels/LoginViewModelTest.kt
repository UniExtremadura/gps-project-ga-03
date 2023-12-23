package uex.aseegps.ga03.tuonce.view.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import uex.aseegps.ga03.tuonce.model.Repository
import uex.aseegps.ga03.tuonce.model.User


@ExperimentalCoroutinesApi
class LoginViewModelTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var mockRepository: Repository

    private lateinit var loginViewModel: LoginViewModel

    private val usuario = User(
        userId = 1L,
        image = 0,
        name = "UsuarioEjemplo",
        password = "12345",
        points = 100,
        conectado = 1
    )

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(mockRepository)
        loginViewModel.usuario = MutableLiveData(usuario)

        `when`(mockRepository.usuarioConectado).thenReturn(MutableLiveData(usuario))
    }


    @Test
    fun `test se lanza el repositorio a recuperar el usuario al inicializar su nombre`() {
        runBlocking {
            loginViewModel.initializeNombre(usuario.name!!)

            Mockito.verify(mockRepository).setUserName(usuario.name!!)

            assertNotNull(mockRepository.usuarioConectado)
        }
    }

    @Test
    fun `test obtenemos la llamada y el nombre del usuario correctamente`() {
        runBlocking {
            val nombre = usuario.name!!

            loginViewModel.initializeNombre(nombre)

            assertEquals(1L, mockRepository.usuarioConectado.value?.userId)

            assertNotNull(mockRepository.usuarioConectado)
        }
    }


}