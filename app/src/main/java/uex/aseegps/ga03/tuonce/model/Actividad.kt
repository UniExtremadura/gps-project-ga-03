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
    ])
data class Actividad(
    @PrimaryKey(autoGenerate = true) var actividadId: Long?,

    var accion: AccionActividad,

    @ColumnInfo(name = "user_id")
    var usuarioActividad: Long?,

    @ColumnInfo(name = "futbolista")
    var futbolistaActividad: String?,

    @ColumnInfo(name = "liga_id")
    var ligaActividad: String?,

    var jornadaActividad: Int? = null

) : Serializable

enum class AccionActividad {
    INICIAR_LIGA,
    ACABAR_LIGA,
    COMPRAR_FUTBOLISTA,
    VENDER_FUTBOLISTA,
    INICIAR_JORNADA
}