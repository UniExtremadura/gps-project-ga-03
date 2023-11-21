package uex.aseegps.ga03.tuonce.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) var userId: Long?,
    val image: Int = 0,
    val name: String = "",
    val password: String = "",
    val points : Int = 0
) : Serializable
