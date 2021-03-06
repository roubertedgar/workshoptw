package workshoptw.com.work_shop_tw.views.place

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_details.*
import workshoptw.com.work_shop_tw.R
import workshoptw.com.work_shop_tw.models.FactoryDAO
import workshoptw.com.work_shop_tw.models.place.Place

class PLaceFormActivity : AppCompatActivity() {
    private lateinit var placeViewModel: PlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        placeViewModel = PlaceViewModel(FactoryDAO.getPlaceDatabase(applicationContext))

        buttonDone.setOnClickListener {
            val name = textName.text.toString()
            val description = textDescription.text.toString()
            val place = Place(name, description)

            savePlace(place)
        }
    }

    private fun savePlace(place: Place) = placeViewModel.savePlace(place).subscribe { finish() }
}
