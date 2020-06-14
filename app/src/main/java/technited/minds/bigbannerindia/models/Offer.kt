package technited.minds.bigbannerindia.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Offer(
        val image: String? = null,
        val cdt: String? = null,
        val flag: String? = null,
        val name: String? = null,
        val id: String? = null,

        @field:SerializedName("client_id")
        val clientId: String? = null
) : Parcelable
