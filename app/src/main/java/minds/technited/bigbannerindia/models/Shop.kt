package minds.technited.bigbannerindia.models

import org.parceler.Parcel

@Parcel
data class Shop(
        val product: List<Product?>? = null,
        val address: String? = null,
        val gender: String? = null,
        val bannerId: String? = null,
        val city: String? = null,
        val mobile: String? = null,
        val locality: String? = null,
        val gst: String? = null,
        val banner: String? = null,
        val srn: String? = null,
        val offer: List<Offer?>? = null,
        val categoryId: String? = null,
        val name: String? = null,
        val id: String? = null,
        val state: String? = null,
        val category: String? = null,
        val postalCode: String? = null,
        val email: String? = null,
        val status: String? = null
)
