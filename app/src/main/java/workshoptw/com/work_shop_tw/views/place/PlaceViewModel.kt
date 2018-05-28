package workshoptw.com.work_shop_tw.views.place

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import workshoptw.com.work_shop_tw.models.place.Place
import workshoptw.com.work_shop_tw.models.place.PlaceDAO

class PlaceViewModel(private val database: PlaceDAO) {

    fun savePlace(place: Place): Completable =
            Completable.fromAction { database.insert(place) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


    fun getAll(): Flowable<List<Place>> =
            database.getAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}

