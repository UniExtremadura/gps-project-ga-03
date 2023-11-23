package uex.aseegps.ga03.tuonce.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uex.aseegps.ga03.tuonce.model.Liga
import uex.aseegps.ga03.tuonce.model.User

@Dao
interface LigaDao {

    @Insert
    suspend fun insertarLiga(liga: Liga): Long

    @Query("""
        SELECT * FROM Liga
        WHERE user_id = :userId
    """)
    suspend fun obtenerLigaPorUsuario(userId: Long): Liga?

    @Query("""
        SELECT User.* FROM User
        INNER JOIN Equipo ON User.userId = Equipo.user_id
        INNER JOIN Liga ON Equipo.liga_id = Liga.ligaId
        WHERE Liga.ligaId = :ligaId
    """)
    fun obtenerUsuariosPorLiga(ligaId: Long): List<User>

    @Update
    suspend fun actualizarLiga(liga: Liga)

    @Query("SELECT * FROM Liga")
    suspend fun obtenerLigas(): List<Liga>

    @Query("SELECT * FROM Liga WHERE ligaId = :id")
    suspend fun obtenerLigaPorId(id: Long): Liga?

    @Query("DELETE FROM Liga WHERE ligaId = :id")
    suspend fun eliminarLiga(id: Long)
}