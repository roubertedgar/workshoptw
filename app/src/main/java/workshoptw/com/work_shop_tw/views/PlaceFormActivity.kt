package workshoptw.com.work_shop_tw.views

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_place_form.*
import workshoptw.com.work_shop_tw.R
import workshoptw.com.work_shop_tw.models.Place

class PlaceFormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_form)
        doneButton.setOnClickListener {
            val name = placeName.text.toString()
            val description = placeDescription.text.toString()
            val place = Place(name, description)

            intent.putExtra("place", place)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
