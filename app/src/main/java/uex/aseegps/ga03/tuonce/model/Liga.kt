package uex.aseegps.ga03.tuonce.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Liga (

    @PrimaryKey(autoGenerate = true) var ligaId: Long?,
    val name: String = "",
    val partidos: Int,
    @ColumnInfo(name = "user_id")
    var userId: Long?

) : Serializable