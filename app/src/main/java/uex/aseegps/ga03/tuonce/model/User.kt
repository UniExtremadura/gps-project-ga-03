package uex.aseegps.ga03.tuonce.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) var userId: Long?,
    val name: String = "",
    val password: String = ""
) : Serializable
