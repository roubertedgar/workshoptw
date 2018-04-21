package workshoptw.com.work_shop_tw.views

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_details.*
import workshoptw.com.work_shop_tw.R
import workshoptw.com.work_shop_tw.models.Place

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        buttonDone.setOnClickListener {
            val name = textName.text.toString()
            val description = textDescription.text.toString()
            val place = Place(name, description)
            val intent = Intent()

            intent.putExtra("place", place)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
