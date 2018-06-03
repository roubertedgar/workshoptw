package workshoptw.com.work_shop_tw.views.place

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_place_form.*
import workshoptw.com.work_shop_tw.R
import workshoptw.com.work_shop_tw.models.FactoryDAO
import workshoptw.com.work_shop_tw.models.place.Place

class PLaceFormActivity : AppCompatActivity() {
    private lateinit var placeViewModel: PlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_form)
        placeViewModel = PlaceViewModel(FactoryDAO.getPlaceDatabase(applicationContext))

        doneButton.setOnClickListener {
            val name = placeName.text.toString()
            val description = placeDescription.text.toString()
            val place = Place(name, description)

            savePlace(place)
        }
    }

    private fun savePlace(place: Place) = placeViewModel.savePlace(place).subscribe { finish() }
}
