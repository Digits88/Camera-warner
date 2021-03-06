package org.jugendhackt.camera_warner.Data.Providers;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jugendhackt.camera_warner.Data.Model.Camera;
import org.jugendhackt.camera_warner.Utils.LocationUtils;
import org.jugendhackt.camera_warner.Utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class provides data from local MySQL Database that is accessible via a php script.
 * It therefore is blocking.
 */
public class DatabaseDataProvider implements DataProvider {


    //the url where this database is located at
    public final static String URL = "http://172.16.107.61/cameras.php";

    //to avoid having to fetch the data every time
    private static List<Camera> camerasCache = new LinkedList<>();

    /**
     * Actually loads data from the data source
     * @return the data that has been loaded
     */
    private List<Camera> forceFetch() {
        try {
            return parseFromJSONArray(new JSONArray(NetworkUtils.getResponseFromHttpUrl(URL)));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void fetchData() {
        camerasCache = forceFetch();
    }

    @Override
    public boolean hasData() {
        return !camerasCache.isEmpty();
    }

    @Override
    public List<Camera> getAllCameras() {
        if (camerasCache.isEmpty()) {
            camerasCache = forceFetch();
        }
        return camerasCache;
    }

    @Override
    public Camera getNearestCamera(Location location) {
        if (camerasCache.isEmpty()) {
            camerasCache = forceFetch();
        }
        return LocationUtils.getNearestTo(location, camerasCache);

    }

    @Override
    public float distanceToNearestCamera(Location location) {
        Camera nearestCamera = getNearestCamera(location);
        return LocationUtils.distanceBetween(location, nearestCamera);
    }

    @Override
    public List<Camera> getCamerasInRange(double latitude, double longitude, int radius) {
        return null;
    }

    /**
     * Parses the format specific to this database's output and return it as List of Cameras
     * @param array the json array that contains the different camera object
     * @return the list of cameras
     */
    public List<Camera> parseFromJSONArray(JSONArray array) {
        List<Camera> cameras = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject dieseCamera = array.getJSONObject(i);
                Camera camera = new Camera(dieseCamera.getDouble("latitude"), dieseCamera.getDouble("longitude"));
                cameras.add(camera);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cameras;
    }
}
