package com.nevermore.mapasignala.ui;

import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

public class TileProvider extends UrlTileProvider {

    private int provider;

    TileProvider(int provider) {
        super(256, 256);
        this.provider = provider;
    }

    @Override
    public URL getTileUrl(int x, int y, int z) {
        double k = Math.pow(2, z - 12);
        if (x >= 2280 * k && x < 2281 * k && y >= 1476 * k && y < 1477 * k) {
            try {
                return new URL("https://kocka.dilfa.com:53729/tile/" + z + "/" + x + "/" + y + "/0/2200" + provider);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
