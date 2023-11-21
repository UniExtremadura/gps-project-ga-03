package uex.aseegps.ga03.tuonce.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Equipo::class,
            parentColumns = ["equipoId"],
            childColumns = ["equipo_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Futbolista(
    @PrimaryKey(autoGenerate = true) var futbolistaId: Long?,
    var nombreJugador: String,
    var años: String,
    var posicion: String,
    var varor: Int,
    var minutoJugados: Int,
    var goles: Int,
    var asistencias: Int,
    var balonAlArea: Int,
    var parada: Int,
    var tarjetaAmarilla: Int,
    var tarjetaRoja: Int,
    var media: Int,
    var puntosAportados: Int,
    var faltacometidas: Int,
    @ColumnInfo(name = "equipo_id")
    var equipoId: Long?
) : Serializable

