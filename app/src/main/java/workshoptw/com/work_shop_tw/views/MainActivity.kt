package workshoptw.com.work_shop_tw.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import workshoptw.com.work_shop_tw.R
import workshoptw.com.work_shop_tw.models.Place

class MainActivity : AppCompatActivity() {
    companion object {
        const val PLACE_FORM_REQUEST_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton.setOnClickListener {
            val intent = Intent(this, PlaceFormActivity::class.java)
            startActivityForResult(intent, PLACE_FORM_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PLACE_FORM_REQUEST_CODE) {
                val place = data?.getSerializableExtra("place") as Place

                Toast.makeText(this,
                        "Receive ${place.name} and ${place.description} as result",
                        Toast.LENGTH_LONG).show()
            }
        }
    }
}
