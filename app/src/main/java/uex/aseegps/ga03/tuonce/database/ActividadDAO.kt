package uex.aseegps.ga03.tuonce.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uex.aseegps.ga03.tuonce.model.Actividad
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User

@Dao
interface ActividadDao {

    @Insert
    suspend fun insertar(actividad: Actividad)
    @Query("SELECT * FROM Actividad")
    suspend fun findAll(): List<Actividad>
    @Query("SELECT * FROM Actividad WHERE user_id = :usuarioId")
    suspend fun findAllByUser(usuarioId : Long): List<Actividad>

}