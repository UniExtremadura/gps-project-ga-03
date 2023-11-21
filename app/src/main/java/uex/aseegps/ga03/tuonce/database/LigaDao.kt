package uex.aseegps.ga03.tuonce.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uex.aseegps.ga03.tuonce.model.Liga

@Dao
interface LigaDao {

    @Insert
    suspend fun insertarLiga(liga: Liga): Long

    @Update
    suspend fun actualizarLiga(liga: Liga)

    @Query("SELECT * FROM Liga")
    suspend fun obtenerLigas(): List<Liga>

    @Query("SELECT * FROM Liga WHERE ligaId = :id")
    suspend fun obtenerLigaPorId(id: Long): Liga?

    @Query("DELETE FROM Liga WHERE ligaId = :id")
    suspend fun eliminarLiga(id: Long)
}