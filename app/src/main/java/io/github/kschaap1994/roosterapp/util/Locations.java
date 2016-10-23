package io.github.kschaap1994.roosterapp.util;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kevin on 23-10-2016.
 */

public final class Locations {

    //Wibauthuis building
    private final static LatLng WBH = new LatLng(52.3589071, 4.9097675);

    //Benno Premselahuis building
    private final static LatLng BPH = new LatLng(52.3588616, 4.9097358);

    //Theo Thijssenhuis building
    private final static LatLng TTH = new LatLng(52.357867, 4.9088264);

    //Default location, center of all these buildings
    private final static LatLng DEFAULT = new LatLng(52.3588616, 4.9097358);

    public static LatLng translatePosition(final String key) {
        final String loc = key.toLowerCase();

        switch (loc) {
            case "wbh":
                return WBH;
            case "bph":
                return BPH;
            case "tth":
                return TTH;
            default:
                return DEFAULT;
        }
    }
}
