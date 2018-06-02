package workshoptw.com.work_shop_tw.models

import android.arch.persistence.room.Room
import android.content.Context
import workshoptw.com.work_shop_tw.models.place.PlaceDAO


class FactoryDAO {
    companion object {
        private fun newInstance(applicationContext: Context) =
                Room.databaseBuilder(applicationContext,
                        AppDatabase::class.java, "workshop.db").build()

        fun getPlaceDatabase(applicationContext: Context): PlaceDAO {
            return newInstance(applicationContext).placeDAO()
        }
    }
}