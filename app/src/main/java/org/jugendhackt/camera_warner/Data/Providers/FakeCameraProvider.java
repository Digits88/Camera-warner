package org.jugendhackt.camera_warner.Data.Providers;

import org.jugendhackt.camera_warner.Data.Model.Camera;

import java.util.LinkedList;
import java.util.List;


/**
 * This is just a DataProvider that provides *fake* and constant data. It should be used for test purposes only!
 * It therefore is not blocking.
 */
public class FakeCameraProvider extends AbstractDataProvider {

    @Override
    protected List<Camera> forceFetch() {
        List<Camera> cameras = new LinkedList<>();
        cameras.add(new Camera(-31, 142));
        cameras.add(new Camera(53.563, 9.971));
        cameras.add(new Camera(0, 0));
        return cameras;
    }
}
