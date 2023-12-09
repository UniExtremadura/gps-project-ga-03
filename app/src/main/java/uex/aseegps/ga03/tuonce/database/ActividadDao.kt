package uex.aseegps.ga03.tuonce.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import uex.aseegps.ga03.tuonce.model.Actividad

@Dao
interface ActividadDao {

    @Insert
    suspend fun insertar(actividad: Actividad)
    @Query("SELECT * FROM Actividad")
    suspend fun findAll(): List<Actividad>

    @Transaction
    @Query("SELECT * FROM Actividad WHERE user_id = :usuarioId")
    fun findAllByUser(usuarioId : Long): LiveData<List<Actividad>>

}