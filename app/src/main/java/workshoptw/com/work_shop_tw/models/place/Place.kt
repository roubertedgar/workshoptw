package workshoptw.com.work_shop_tw.models.place

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity
data class Place(@PrimaryKey @ColumnInfo var name: String, @ColumnInfo var description: String) : Serializable