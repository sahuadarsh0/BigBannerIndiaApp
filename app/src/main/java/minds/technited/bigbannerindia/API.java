package minds.technited.bigbannerindia;

public enum API {
    //    BASE_URL("http://bigbannerindia.com/admin/api/"),
//    ASSETS_URL("http://bigbannerindia.com/admin/assets/uploads/"),
    BASE_URL("http://192.168.43.102/Bigbannerindia.com/admin/api/"),
    ASSETS_URL("http://192.168.43.102/Bigbannerindia.com/admin/assets/uploads/"),
    BANNER_FOLDER(ASSETS_URL + "banner/"),
    OFFER_FOLDER(ASSETS_URL + "offer/"),
    PRODUCT_FOLDER(ASSETS_URL + "product/"),
    VIDEO_SLIDER_FOLDER(ASSETS_URL + "slider/");

    private final String text;

    API(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}