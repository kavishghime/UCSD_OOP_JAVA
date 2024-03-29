package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;


/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }
	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);
	    int blue = color(0, 0, 255);
	    int red = color(255, 0, 0);
	    
	    //TODO: Add code here as appropriate
	    
	    //create a simplepointmarker object for every earthquake feature location
	    //and add it to the markers list
	    for(PointFeature eq: earthquakes){
	    	//markers.add(new SimplePointMarker(eq.getLocation()));
	    	markers.add(createMarker(eq));	    	
	    }
	    
	  //add the markers list to map
	    map.addMarkers(markers);
	    
	    
	    
	    //change the radius of every marker
	    //setRadius is a simplepointmarker method
	    //markers list is of type List
	    //therefore typecasting every markers to SimplePointMarker 
	    for(Marker m: markers){
	    	((SimplePointMarker)m).setRadius(10f);	
	    	
	    	if((float) m.getProperty("magnitude") < 4.0){
	    		m.setColor(blue);
	    		//m.set
	    	}else if ((float) m.getProperty("magnitude") >= 4.0 && (float) m.getProperty("magnitude") <= 4.9){
	    		m.setColor(yellow);
	    	}else{
	    		m.setColor(red);
	    	}
	    }
	  //add the markers list to map
	    map.addMarkers(markers);
	}
			
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// finish implementing and use this method, if it helps.
		return new SimplePointMarker(feature.getLocation(), feature.getProperties());
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		//rectangle
		fill(255);
		rect(50, 50, 130, 250);
		//text
		fill(0, 0, 0);
		
		text("Earthquake Key", 70, 70);
		
		fill(255, 0, 0);
		ellipse(70, 120, 15, 15);
		fill(0, 0, 0);
		text("5.0+ Magnitude", 80, 120);
		fill(255, 255, 0);
		ellipse(70, 140, 10, 10);
		fill(0, 0, 0);
		text("4.0+ Magnitude", 80, 140);
		fill(0, 0, 255);
		ellipse(70, 160, 5, 5);
		fill(0, 0, 0);
		text("Below 4.0", 80, 160);
	
	}
}
