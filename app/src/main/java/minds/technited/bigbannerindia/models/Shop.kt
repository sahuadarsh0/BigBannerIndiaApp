package minds.technited.bigbannerindia.models

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class Shop(
        val product: List<Product?>? = null,
        val address: String? = null,
        val gender: String? = null,

        @field:SerializedName("banner_id")
        val bannerId: String? = null,

        val city: String? = null,
        val mobile: String? = null,
        val locality: String? = null,
        val gst: String? = null,
        val banner: String? = null,
        val srn: String? = null,
        val offer: List<Offer?>? = null,

        @field:SerializedName("category_id")
        val categoryId: String? = null,

        val name: String? = null,
        val id: String? = null,
        val state: String? = null,
        val category: String? = null,

        @field:SerializedName("postal_code")
        val postalCode: String? = null,

        val email: String? = null,
        val status: String? = null
)
