package technited.minds.bigbannerindia;

import org.jetbrains.annotations.NotNull;

public enum API {
//        URL("http://192.168.43.96/Bigbannerindia.com/admin/"),
    URL("http://bigbannerindia.com/admin/"),

    BASE_URL(URL + "api/"),
    ASSETS_URL(URL + "assets/uploads/"),

    VIDEO_SLIDER_FOLDER(ASSETS_URL + "slider/"),
    BANNER_FOLDER(ASSETS_URL + "banner/"),
    OFFER_FOLDER(ASSETS_URL + "offer/"),
    PRODUCT_FOLDER(ASSETS_URL + "product/"),
    SERVICE_FOLDER(ASSETS_URL + "service/");

    private final String text;

    API(final String text) {
        this.text = text;
    }

    @NotNull
    @Override
    public String toString() {
        return text;
    }
}