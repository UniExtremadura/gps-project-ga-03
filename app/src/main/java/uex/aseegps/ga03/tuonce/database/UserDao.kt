package uex.aseegps.ga03.tuonce.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uex.aseegps.ga03.tuonce.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>
    @Query("SELECT * FROM user WHERE name LIKE :first LIMIT 1")
    suspend fun findByName(first: String): User
    @Insert
    suspend fun insert(user: User): Long
}
