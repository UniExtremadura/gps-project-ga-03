package uex.aseegps.ga03.tuonce.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uex.aseegps.ga03.tuonce.model.Futbolista

@Dao
interface FutbolistaDao {

    @Insert
    suspend fun insert(futbolista: Futbolista): Long

    @Update
    suspend fun update(futbolista: Futbolista?)

    @Query("SELECT * FROM futbolista WHERE futbolistaId = :futbolistaId LIMIT 1")
    suspend fun findById(futbolistaId: Long): Futbolista?

    @Query("SELECT * FROM futbolista WHERE nombreJugador = :futbolistaName LIMIT 1")
    suspend fun findByName(futbolistaName: String): Futbolista?

    @Query("SELECT * FROM futbolista WHERE equipo_id = :equipoId")
    suspend fun findByEquipoId(equipoId: Long?): List<Futbolista>

    @Query("SELECT * FROM futbolista")
    suspend fun findAll(): List<Futbolista>

    @Query("SELECT * FROM futbolista")
    fun findAllFutbolistas(): LiveData<List<Futbolista>>

    @Delete
    suspend fun delete(futbolista: Futbolista)
}
