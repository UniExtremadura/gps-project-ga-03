package uex.aseegps.ga03.tuonce.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["user_id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Liga::class,
            parentColumns = ["ligaId"],
            childColumns = ["liga_id"],
            onDelete = CASCADE
        ),
    ]
)
data class Equipo(
    @PrimaryKey(autoGenerate = true) var equipoId: Long?,
    val name: String = "",

    @ColumnInfo(name = "user_id")
    val userId: Long?,

    @ColumnInfo(name = "liga_id")
    var ligaId: Long?
) : Serializable
