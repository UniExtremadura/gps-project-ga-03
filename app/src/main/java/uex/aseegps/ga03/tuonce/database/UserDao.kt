package uex.aseegps.ga03.tuonce.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uex.aseegps.ga03.tuonce.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE name LIKE :first LIMIT 1")
    suspend fun findByName(first: String): User

    @Query("SELECT * FROM user WHERE name LIKE :first LIMIT 1")
    fun findByNameLD(first: String): LiveData<User?>

    @Insert
    suspend fun insert(user: User): Long

    @Query("DELETE FROM user WHERE userId = :id")
    suspend fun delete(id: Long)

    @Query("UPDATE user SET conectado = 1 WHERE name = :username")
    suspend fun conectar(username: String)

    @Query("UPDATE user SET conectado = 0 WHERE conectado = 1")
    suspend fun desconectarTodos()

    @Query("SELECT * FROM user WHERE conectado = 1 LIMIT 1")
    suspend fun obtenerUsuarioConectado(): User?

    @Query("UPDATE user SET points = :pointsUser WHERE userId = :userId")
    suspend fun updatePoints(userId: Long, pointsUser: Int)

    @Update
    suspend fun update(user : User)
}