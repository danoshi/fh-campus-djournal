package fh.campus.djournal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fh.campus.djournal.models.Journal
import fh.campus.djournal.models.Note

@Database(entities = arrayOf(Journal::class, Note::class), version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val journalDao: JournalDao
    abstract val noteDao: NoteDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "dJournal_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}