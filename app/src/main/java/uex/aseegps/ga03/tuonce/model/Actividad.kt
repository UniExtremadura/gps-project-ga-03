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
        ),
        ForeignKey(
            entity = Futbolista::class,
            parentColumns = ["futbolistaId"],
            childColumns = ["futbolista_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Liga::class,
            parentColumns = ["ligaId"],
            childColumns = ["liga_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ])
data class Actividad (
    @PrimaryKey(autoGenerate = true) var actividadId: Long?,

    var accion: AccionActividad,

    @ColumnInfo(name = "user_id")
    var usuarioActividad: Long?,

    @ColumnInfo(name = "futbolista_id")
    var futbolistaActividad: Long?,

    @ColumnInfo(name = "liga_id")
    var ligaActividad: Long?,

    var jornadaActividad: Int? = null

) : Serializable

enum class AccionActividad {
    INICIAR_LIGA,
    ACABAR_LIGA,
    COMPRAR_FUTBOLISTA,
    VENDER_FUTBOLISTA,
    INICIAR_JORNADA
}