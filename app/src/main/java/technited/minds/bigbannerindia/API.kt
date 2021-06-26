package technited.minds.bigbannerindia

enum class API(private val text: String) {
    // URL("http://192.168.43.96/Bigbannerindia.com/admin/"),
    URL("http://bigbannerindia.com/admin/"),
    BASE_URL(URL.toString() + "api/"),
    ASSETS_URL(URL.toString() + "assets/uploads/"),
    VIDEO_SLIDER_FOLDER(ASSETS_URL.toString() + "slider/"),
    IMAGE_SLIDER_FOLDER(ASSETS_URL.toString() + "slider_image/"),
    BANNER_FOLDER(ASSETS_URL.toString() + "banner/"),
    OFFER_FOLDER(ASSETS_URL.toString() + "offer/"),
    PRODUCT_FOLDER(ASSETS_URL.toString() + "product/"),
    SERVICE_FOLDER(ASSETS_URL.toString() + "service/");

    override fun toString(): String {
        return text
    }
}