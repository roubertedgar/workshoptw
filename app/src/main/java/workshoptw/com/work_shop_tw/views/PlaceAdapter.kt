package workshoptw.com.work_shop_tw.views

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import workshoptw.com.work_shop_tw.R
import workshoptw.com.work_shop_tw.models.Place

class PlaceAdapter(private var items: List<Place>) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(place: Place) {
            itemView.findViewById<TextView>(R.id.item_name).text = place.name
            itemView.findViewById<TextView>(R.id.item_description).text = place.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(items[position])
    }

}