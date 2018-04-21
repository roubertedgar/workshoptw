package workshoptw.com.work_shop_tw.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import workshoptw.com.work_shop_tw.R
import workshoptw.com.work_shop_tw.models.Place

class MainActivity : AppCompatActivity() {
    companion object {
        const val DETAILS_ACTIVITY_ID = 1
    }

    val placeList: MutableList<Place> = mutableListOf(
            Place("Podrao", "Mió de Recife"),
            Place("La em casa", "Mió de BH"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            startActivityForResult(intent, DETAILS_ACTIVITY_ID)
        }

        recyclerPlaceList.layoutManager = LinearLayoutManager(this)
        recyclerPlaceList.adapter = PlaceAdapter(placeList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val place = data?.getSerializableExtra("place") as Place

            placeList.add(place)
            recyclerPlaceList.adapter.notifyDataSetChanged()

            Snackbar.make(mainContainer , place.name, Toast.LENGTH_SHORT).show()
        }
    }
}