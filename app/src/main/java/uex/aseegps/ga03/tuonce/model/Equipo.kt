package uex.aseegps.ga03.tuonce.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.ForeignKey.Companion.NO_ACTION
import androidx.room.ForeignKey.Companion.SET_NULL
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
            onDelete = SET_NULL
        ),
    ]
)
data class Equipo(
    @PrimaryKey(autoGenerate = true) var equipoId: Long?,
    var name: String = "",

    var presupuesto : Int,

    @ColumnInfo(name = "user_id")
    val userId: Long?,

    @ColumnInfo(name = "liga_id")
    var ligaId: Long?
) : Serializable