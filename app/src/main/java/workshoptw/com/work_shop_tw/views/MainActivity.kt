package workshoptw.com.work_shop_tw.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_main.*
import workshoptw.com.work_shop_tw.R
import workshoptw.com.work_shop_tw.models.place.Place
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import workshoptw.com.work_shop_tw.models.FactoryDAO
import workshoptw.com.work_shop_tw.models.place.PlaceDAO


class MainActivity : AppCompatActivity() {
    private lateinit var placeDAO: PlaceDAO

    companion object {
        const val DETAILS_ACTIVITY_ID = 1
    }

    val placeList: MutableList<Place> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        placeDAO = FactoryDAO.getPlaceDatabase(applicationContext)

        addButton.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            startActivityForResult(intent, DETAILS_ACTIVITY_ID)
        }

        recyclerPlaceList.layoutManager = LinearLayoutManager(this)
        recyclerPlaceList.adapter = PlaceAdapter(placeList)

        loadPlaces()
    }

    private fun insertPlace(place: Place) {
        Completable.fromAction { placeDAO.insert(place) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    loadPlaces()
                    Snackbar.make(mainContainer, place.name, Toast.LENGTH_SHORT).show()
                }
    }


    private fun loadPlaces() {
        FactoryDAO.getPlaceDatabase(applicationContext).getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe {
                    placeList.clear()
                    placeList.addAll(it)
                    recyclerPlaceList.adapter.notifyDataSetChanged()
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val place = data?.getSerializableExtra("place") as Place
            insertPlace(place)
        }
    }
}