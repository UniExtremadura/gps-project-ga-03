package uex.aseegps.ga03.tuonce.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity
data class Liga (

    @PrimaryKey(autoGenerate = true) var ligaId: Long?,
    val name: String = "",
    val partidos: Int

) : Serializable