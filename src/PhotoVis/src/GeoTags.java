/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;


import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.i;
import com.teamdev.jxmaps.swing.MapView;
import java.io.IOException;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * This example demonstrates how to display info windows on the map.
 *
 * @author Vitaly Eremenko
 */
public class GeoTags extends MapView {

    private static String convertImageStreamToString(InputStream is) {
        String result;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[10240];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            result = "data:image/png;base64," + DatatypeConverter.printBase64Binary(buffer.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

  private  String getBase64ImageString() {
        String filename = "images/small/image10.png";
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filename));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(img,"png", os);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        //InputStream is = getClass().getClassLoader().getResourceAsStream("example1.png");
        return convertImageStreamToString(is);
    }

//    private final  String contentString = "<table cellpadding=\"5\"><tr><td><img src=\""+getBase64ImageString() + "\" /></td><td valign='top'><p><b>Paris</b></p>" +
//            "<p>Paris is the home of the most visited art museum in the world.</p>" +
//            "<p style=\"color:#757575\">Use InfoWindow to display custom information, related to a point on a map. InfoWindow layout can be formatted using HTML.</p>" +
//            "</td></tr></table>";
  
   private final  String chennai = "<table cellpadding=\"5\"><tr><td><img src=\""+getBase64ImageString() + "\" /></td><td valign='top'><p><b>Chennai</b></p>" +
            "</td></tr></table>";
   
    private final  String ankara = "<table cellpadding=\"5\"><tr><td><img src=\""+getBase64ImageString() + "\" /></td><td valign='top'><p><b>Ankara</b></p>" +
            "</td></tr></table>";
    
    private final  String eindhoven = "<table cellpadding=\"5\"><tr><td><img src=\""+getBase64ImageString() + "\" /></td><td valign='top'><p><b>Eindhoven</b></p>" +
            "</td></tr></table>";


    public GeoTags() {
        // Setting of a ready handler to MapView object. onMapReady will be called when map initialization is done and
        // the map object is ready to use. Current implementation of onMapReady customizes the map object.
        setOnMapReadyHandler(new MapReadyHandler() {
            @Override
            public void onMapReady(MapStatus status) {
                // Check if the map is loaded correctly
                if (status == MapStatus.MAP_STATUS_OK) {
                    // Getting the associated map object
                    final Map map = getMap();
                    // Setting the map center
                    map.setCenter(new LatLng(28, 3));
                    // Setting initial zoom value
                    map.setZoom(3.0);
                    // Creating a map options object
                    MapOptions options = new MapOptions();
                    // Creating a map type control options object
                    MapTypeControlOptions controlOptions = new MapTypeControlOptions();
                    // Changing position of the map type control
                    controlOptions.setPosition(ControlPosition.TOP_RIGHT);
                    // Setting map type control options
                    options.setMapTypeControlOptions(controlOptions);
                    // Setting map options
                    map.setOptions(options);
                    // Creating a marker object
                    final Marker marker = new Marker(map);
                    // Moving marker to the map center
                    marker.setPosition(new LatLng(13.067439, 80.237617));
                    // Creating an information window
                    final InfoWindow window = new InfoWindow(map);
                    // Setting html content to the information window
                    window.setContent(chennai);
                    // Showing the information window on marker
                    window.open(map, marker);
                    
                    final Marker marker2 = new Marker(map);
                    // Moving marker to the map center
                    marker2.setPosition(new LatLng(39.925533, 32.866287));
                    // Creating an information window
                    final InfoWindow window2 = new InfoWindow(map);
                    // Setting html content to the information window
                    window2.setContent(ankara);
                    // Showing the information window on marker
                    window2.open(map, marker2);
                    
                    final Marker marker3 = new Marker(map);
                    // Moving marker to the map center
                    marker3.setPosition(new LatLng(51.441642,5.4697225));
                    // Creating an information window
                    final InfoWindow window3 = new InfoWindow(map);
                    // Setting html content to the information window
                    window3.setContent(eindhoven);
                    // Showing the information window on marker
                    window3.open(map, marker3);
                }
            }
        });

    }

    public static void main(String[] args) {
        final GeoTags sample = new GeoTags();

        JFrame frame = new JFrame("Info window");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(sample, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
