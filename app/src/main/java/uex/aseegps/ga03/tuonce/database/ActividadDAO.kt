import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uex.aseegps.ga03.tuonce.model.Actividad
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User

@Dao
interface ActividadDao {

    @Insert
    suspend fun insertCompraMercado(compraMercado: Actividad)

    @Query("SELECT * FROM Actividad")
    suspend fun getAllComprasMercado(): List<Actividad>
    @Query("SELECT usuarioActividad FROM Actividad WHERE actividadId=:compraMercadoId")
    suspend fun obtenerUsuarioMercado(compraMercadoId: Long): User

    @Query("SELECT futbolistaActividad FROM Actividad WHERE actividadId=:compraMercadoId")
    suspend fun obtenerFutbolistaMercado(compraMercadoId: Long): Futbolista

    @Query("DELETE FROM Actividad WHERE actividadId = :id")
    suspend fun deleteCompraMercadoById(id: Long)

    @Query("DELETE FROM Actividad")
    suspend fun deleteAllComprasMercado()

}