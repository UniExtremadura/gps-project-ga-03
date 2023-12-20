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
import uex.aseegps.ga03.tuonce.api.RetrofitServiceFactory
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.Liga
import uex.aseegps.ga03.tuonce.model.Repository
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.model.api.Article
import uex.aseegps.ga03.tuonce.model.api.Source
import uex.aseegps.ga03.tuonce.view.fragments.MisLigasFragment


@ExperimentalCoroutinesApi
class MisLigasViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var mockRepository: Repository

    private lateinit var misLigasViewModel: MisLigasViewModel

    @Mock
    private lateinit var fragmentoMisLigas : MisLigasFragment

    @Mock
    private lateinit var retrofitServiceFactory : RetrofitServiceFactory

    private val usuario = User(
        userId = 1L,
        image = 0,
        name = "UsuarioEjemplo",
        password = "12345",
        points = 100,
        conectado = 1
    )

    val noticias = listOf(
        Article(
            author = "Sports Journalist 1",
            content = "Lionel Messi scores a hat-trick as Argentina secures a victory in the international friendly.",
            description = "Argentina wins in a thrilling football match.",
            publishedAt = "2023-02-15T20:00:00Z",
            source = Source("Sports News Argentina", "https://www.sportsnewsargentina.com"),
            title = "Messi's Hat-trick Leads Argentina to Victory",
            url = "https://www.example.com/messi-hat-trick",
            urlToImage = "https://www.example.com/messi-image.jpg"
        ),
        Article(
            author = "Football Analyst 2",
            content = "River Plate emerges as the champion of the national football league after a nail-biting final.",
            description = "Exciting moments as River Plate clinches the title.",
            publishedAt = "2023-02-20T18:30:00Z",
            source = Source("Football Gazette Argentina", "https://www.footballgazetteargentina.com"),
            title = "River Plate Wins National Football League Title",
            url = "https://www.example.com/river-plate-champion",
            urlToImage = "https://www.example.com/river-plate-image.jpg"
        ),
        Article(
            author = "Soccer Reporter 3",
            content = "Boca Juniors signs a promising young talent, boosting their squad for the upcoming season.",
            description = "Excitement grows as Boca Juniors makes a strategic signing.",
            publishedAt = "2023-02-25T14:45:00Z",
            source = Source("Soccer Times Argentina", "https://www.soccertimesargentina.com"),
            title = "Boca Juniors Signs Young Star for Bright Future",
            url = "https://www.example.com/boca-juniors-signing",
            urlToImage = "https://www.example.com/boca-juniors-image.jpg"
        )
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
        var articles = emptyList<Article>()
        doAnswer {
            retrofitServiceFactory.makeRetrofitService()
            articles = listOf(
                Article(
                    author = "Sports Journalist 1",
                    content = "Lionel Messi scores a hat-trick as Argentina secures a victory in the international friendly.",
                    description = "Argentina wins in a thrilling football match.",
                    publishedAt = "2023-02-15T20:00:00Z",
                    source = Source("Sports News Argentina", "https://www.sportsnewsargentina.com"),
                    title = "Messi's Hat-trick Leads Argentina to Victory",
                    url = "https://www.example.com/messi-hat-trick",
                    urlToImage = "https://www.example.com/messi-image.jpg"
                ),
                Article(
                    author = "Football Analyst 2",
                    content = "River Plate emerges as the champion of the national football league after a nail-biting final.",
                    description = "Exciting moments as River Plate clinches the title.",
                    publishedAt = "2023-02-20T18:30:00Z",
                    source = Source("Football Gazette Argentina", "https://www.footballgazetteargentina.com"),
                    title = "River Plate Wins National Football League Title",
                    url = "https://www.example.com/river-plate-champion",
                    urlToImage = "https://www.example.com/river-plate-image.jpg"
                ),
                Article(
                    author = "Soccer Reporter 3",
                    content = "Boca Juniors signs a promising young talent, boosting their squad for the upcoming season.",
                    description = "Excitement grows as Boca Juniors makes a strategic signing.",
                    publishedAt = "2023-02-25T14:45:00Z",
                    source = Source("Soccer Times Argentina", "https://www.soccertimesargentina.com"),
                    title = "Boca Juniors Signs Young Star for Bright Future",
                    url = "https://www.example.com/boca-juniors-signing",
                    urlToImage = "https://www.example.com/boca-juniors-image.jpg"
                )
            )
            null
        }.`when`(fragmentoMisLigas).fetchArticles()

        fragmentoMisLigas.fetchArticles()

        Mockito.verify(retrofitServiceFactory).makeRetrofitService()
        assertEquals(noticias, articles)
    }


}