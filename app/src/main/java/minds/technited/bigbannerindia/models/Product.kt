package minds.technited.bigbannerindia.models

import org.parceler.Parcel

@Parcel
data class Product(
        val requestStatus: String? = null,
        val image: String? = null,
        val flag: String? = null,
        val comments: List<Comment?>? = null,
        val requests: List<Request?>? = null,
        val commentStatus: String? = null,
        val clientId: String? = null,
        val cdt: String? = null,
        val name: String? = null,
        val id: String? = null,
        val stock: String? = null,
        val clientName: String? = null,
        val likes: List<Like?>? = null
)

@Parcel
data class Comment(
        val cdt: String? = null,
        val flag: String? = null,
        val productId: String? = null,
        val comment: String? = null,
        val id: String? = null,
        val customerName: String? = null,
        val customerId: String? = null
)

@Parcel
data class Like(
        val cdt: String? = null,
        val flag: String? = null,
        val like: String? = null,
        val productId: String? = null,
        val id: String? = null,
        val customerName: String? = null,
        val customerId: String? = null
)

@Parcel
data class Request(
        val request: String? = null,
        val cdt: String? = null,
        val flag: String? = null,
        val productId: String? = null,
        val id: String? = null,
        val customerName: String? = null,
        val customerId: String? = null,
        val status: String? = null
)
