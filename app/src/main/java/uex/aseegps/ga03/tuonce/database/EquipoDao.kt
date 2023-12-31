package uex.aseegps.ga03.tuonce.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uex.aseegps.ga03.tuonce.model.Equipo

@Dao
interface EquipoDao {

    @Query("SELECT * FROM equipo WHERE name LIKE :first LIMIT 1")
    suspend fun findByName(first: String): Equipo

    @Query("SELECT * FROM equipo WHERE user_id = :userId LIMIT 1")
    suspend fun findByUserId(userId: Long?): Equipo?

    @Query("SELECT * FROM equipo WHERE user_id = :userId LIMIT 1")
    fun findByUserIdLD(userId: Long?): LiveData<Equipo?>

    @Query("SELECT * FROM equipo WHERE user_id = :userId LIMIT 1")
    fun findEquipoByUserId(userId: Long?): LiveData<Equipo>?

    @Query("SELECT * FROM equipo WHERE equipoId = :eqid LIMIT 1")
    suspend fun findById(eqid: Long?): Equipo?
    @Insert
    suspend fun insert(equipo: Equipo): Long

    @Update
    suspend fun update(equipo: Equipo?)

    @Delete
    suspend fun delete(equipo: Equipo)
}
