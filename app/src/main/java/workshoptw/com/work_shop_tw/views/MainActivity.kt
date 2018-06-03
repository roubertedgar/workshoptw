package workshoptw.com.work_shop_tw.views

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import workshoptw.com.work_shop_tw.R
import workshoptw.com.work_shop_tw.models.FactoryDAO
import workshoptw.com.work_shop_tw.models.place.Place
import workshoptw.com.work_shop_tw.models.place.PlaceDAO
import workshoptw.com.work_shop_tw.views.place.PLaceFormActivity
import workshoptw.com.work_shop_tw.views.place.PlaceAdapter
import workshoptw.com.work_shop_tw.views.place.PlaceViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var placeViewModel: PlaceViewModel
    private val placeList: MutableList<Place> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        placeViewModel = PlaceViewModel(FactoryDAO.getPlaceDatabase(applicationContext))

        setListeners()
        initRecyclerView()
        loadPlaces()
    }

    private fun setListeners() {
        addButton.setOnClickListener {
            startActivity(Intent(this, PLaceFormActivity::class.java))
        }
    }

    private fun initRecyclerView() {
        recyclerPlaceList.layoutManager = LinearLayoutManager(this)
        recyclerPlaceList.adapter = PlaceAdapter(placeList)
    }

    private fun loadPlaces() = placeViewModel.getAll().subscribe(::updatePlaceList)

    private fun updatePlaceList(places: List<Place>) {
        placeList.clear()
        placeList.addAll(places)
        recyclerPlaceList.adapter.notifyDataSetChanged()
    }
}
