package workshoptw.com.work_shop_tw.models.place

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface PlaceDAO {
    @Query("Select * from Place")
    fun getAll(): Flowable<List<Place>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(place: Place)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg place:Place)

    @Delete
    fun delete(place: Place)
}