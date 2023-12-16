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
    var points: Int = 0,
    var conectado: Int = 0 // 0 significa no conectado, 1 significa conectado
) : Serializable
