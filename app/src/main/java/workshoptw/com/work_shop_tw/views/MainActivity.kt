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
        const val PLACE_FORM_REQUEST_CODE = 200
    }

    private val placeList: MutableList<Place> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        placeDAO = FactoryDAO.getPlaceDatabase(applicationContext)

        addButton.setOnClickListener {
            val intent = Intent(this, PlaceFormActivity::class.java)
            startActivityForResult(intent, PLACE_FORM_REQUEST_CODE)
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
        placeDAO.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe {
                    placeList.clear()
                    placeList.addAll(it)
                    recyclerPlaceList.adapter.notifyDataSetChanged()
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PLACE_FORM_REQUEST_CODE) {
                val place = data?.getSerializableExtra("place") as Place
                insertPlace(place)
            }
        }
    }
}
