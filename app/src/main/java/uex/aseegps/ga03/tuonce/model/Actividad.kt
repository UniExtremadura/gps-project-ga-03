package uex.aseegps.ga03.tuonce.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Actividad (
    @PrimaryKey(autoGenerate = true) var actividadId: Long?,

    val accion: AccionActividad,

    val usuarioActividad: User? = null,

    val futbolistaActividad: Futbolista? = null,

    val ligaActividad: Liga? = null,

    val jornadaActividad: Int? = null

) : Serializable

enum class AccionActividad {
    INICIAR_LIGA,
    ACABAR_LIGA,
    COMPRAR_FUTBOLISTA,
    VENDER_FUTBOLISTA,
    INICIAR_JORNADA
}