package workshoptw.com.work_shop_tw.views

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import workshoptw.com.work_shop_tw.R

class MainActivity : AppCompatActivity() {
    companion object {
        const val DETAILS_ACTIVITY_ID = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            startActivityForResult(intent, DETAILS_ACTIVITY_ID)
        }
    }
}