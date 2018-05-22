package workshoptw.com.work_shop_tw.models

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import workshoptw.com.work_shop_tw.models.place.Place
import workshoptw.com.work_shop_tw.models.place.PlaceDAO

@Database(entities = [Place::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeDAO(): PlaceDAO
}