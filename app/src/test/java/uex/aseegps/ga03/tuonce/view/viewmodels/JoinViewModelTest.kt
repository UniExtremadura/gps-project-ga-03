package uex.aseegps.ga03.tuonce.view.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import es.unex.giiis.asee.tiviclone.data.Repository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mock
import uex.aseegps.ga03.tuonce.R
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

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
    }

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
}