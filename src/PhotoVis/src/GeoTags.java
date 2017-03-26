/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;


import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.InfoWindowOptions;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.MapViewOptions;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.i;
import com.teamdev.jxmaps.swing.MapView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
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

  private  String getBase64ImageString(BufferedImage image) {
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image,"png", os);
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
  
//   private final  String chennai = "<table cellpadding=\"5\"><tr><td><img src=\""+getBase64ImageString() + "\" /></td><td valign='top'><p><b>Chennai</b></p>" +
//            "</td></tr></table>";
//   
//    private final  String ankara = "<table cellpadding=\"5\"><tr><td><img src=\""+getBase64ImageString() + "\" /></td><td valign='top'><p><b>Ankara</b></p>" +
//            "</td></tr></table>";
//    
//    private final  String eindhoven = "<table cellpadding=\"5\"><tr><td><img src=\""+getBase64ImageString() + "\" /></td><td valign='top'><p><b>Eindhoven</b></p>" +
//            "</td></tr></table>";


    public GeoTags(final ArrayList<LatLng> geoLocs, final HashMap<LatLng, ArrayList<src.Image>> geoImageMap,MapViewOptions options) {
        super(options);
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
                    map.setCenter(new LatLng(40.736946, -9.142685));
                    // Setting initial zoom value
                    map.setZoom(4.0);
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
                    
                    
                    for(LatLng geo: geoLocs){
                        String geoTable = "<table cellpadding=\"2\"><tr>";
                        int col =0;
                        for(Image image: geoImageMap.get(geo)){
                            BufferedImage img = PhotoViewer.getScaledImage(image.getOriginal_img(), 100, 100);
                            geoTable+= "<td><img src=\""+getBase64ImageString(img) + "\" /></td>";
                            col++;
                            if(col>3){
                                geoTable+="</tr><tr>";
                                col=0;
                            }
                        }
                        geoTable+= "</tr></table>";
                        System.out.println(geoTable);
                        Marker marker = new Marker(map);
                        marker.setPosition(geo);
                        map.setCenter(geo);
//                        InfoWindowOptions winOptions = new InfoWindowOptions(map);
                        InfoWindow window = new InfoWindow(map);
//                        winOptions.setDisableAutoPan(true);
//                        window.setOptions(winOptions);
                        InfoWindowOptions winOps = new InfoWindowOptions();
                        winOps.setMaxWidth(500);
                        window.setOptions(winOps);
                        window.setContent(geoTable);
                        window.open(map, marker); 
                        //wait
                        long start = new Date().getTime();
                        while (new Date().getTime() - start < 100L) {
                        }
                    }
                    
                    map.setCenter(new LatLng(40.736946, -9.142685));
                    // Setting initial zoom value
                    map.setZoom(2.0);
                }
            }
        });

    }

    public static void main(String[] args) {
        PhotoViewer pv = new PhotoViewer();
        PhotoViewer.images = PhotoViewer.readImages();
        PhotoViewer.GeoTag();
        MapViewOptions options = new MapViewOptions();
        options.setApiKey("AIzaSyA7woFnkPF68xxL2TOukwln76fFNgq1-ps");
        final GeoTags sample = new GeoTags(PhotoViewer.getGeoLocs(),PhotoViewer.getGeoImageMap(),options);

        JFrame frame = new JFrame("Info window");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(sample, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
