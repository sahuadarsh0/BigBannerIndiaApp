package minds.technited.bigbannerindia.models

import org.parceler.Parcel

@Parcel
data class Offer(
        val image: String? = null,
        val cdt: String? = null,
        val flag: String? = null,
        val name: String? = null,
        val id: String? = null,
        val clientId: String? = null
)
