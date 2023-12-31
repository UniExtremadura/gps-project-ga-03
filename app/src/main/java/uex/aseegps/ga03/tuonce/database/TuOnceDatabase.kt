package uex.aseegps.ga03.tuonce.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uex.aseegps.ga03.tuonce.model.Actividad
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.Liga
import uex.aseegps.ga03.tuonce.model.User

@Database(entities = [User::class, Equipo::class, Futbolista::class, Liga::class, Actividad::class], version = 1)
abstract class TuOnceDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun futbolistaDao(): FutbolistaDao
    abstract fun equipoDao(): EquipoDao
    abstract fun ligaDao(): LigaDao

    abstract fun actividadDao(): ActividadDao
    companion object {
        private var INSTANCE: TuOnceDatabase? = null
        fun getInstance(context: Context): TuOnceDatabase? {
            if (INSTANCE == null) {
                synchronized(TuOnceDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        TuOnceDatabase::class.java,
                        "tuonce.db"
                    ).build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}