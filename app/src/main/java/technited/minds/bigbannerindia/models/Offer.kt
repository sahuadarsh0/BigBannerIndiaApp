package technited.minds.bigbannerindia.models

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class Offer(
        val image: String? = null,
        val cdt: String? = null,
        val flag: String? = null,
        val name: String? = null,
        val id: String? = null,

        @field:SerializedName("client_id")
        val clientId: String? = null
)
