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
}