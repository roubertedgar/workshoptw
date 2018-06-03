package workshoptw.com.work_shop_tw.models

import android.arch.persistence.room.Room
import android.content.Context
import workshoptw.com.work_shop_tw.models.place.PlaceDAO


class FactoryDAO {
    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private fun getAppDatabase(applicationContext: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: newInstance(applicationContext).also { INSTANCE = it }
                }

        private fun newInstance(applicationContext: Context) =
                Room.databaseBuilder(applicationContext,
                        AppDatabase::class.java, "workshop.db").build()


        fun getPlaceDatabase(applicationContext: Context): PlaceDAO {
            return getAppDatabase(applicationContext).placeDAO()
        }
    }
}
