package gameClient;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import utils.Point3D;

/**
 * This class helps us to export a KML file from our game.
 * The Class implemented by Singelton Design Pattern.
 * @author Alon Perlmuter.
 * @author Shlomi Daari.
 */
public class KML_Logger {

    private int scenario;
    private StringBuilder content;
    
    private final String FOLDER = "data/"; 	// folder name.
    private final String _KML = ".kml"; 	// file format.
    
    private static KML_Logger kml_logger = null; // for Singelton use.

    /**
     * Private constructor for Singelton use and initialize the beginning of the file.
     * @param scenario number.
     */
    private KML_Logger(int scenario) {
        this.scenario = scenario;
        content = new StringBuilder();
        start();
    }
    
    /**
     * Initiate the beginning of the file by scenario number.
     */
    public void start()
    {
        content.append(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                        "<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" +
                        "  <Document>\r\n" +
                        "    <name>Scenario number " + this.scenario + "</name>" +
                        " <Style id=\"node\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/pushpin/wht-pushpin.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>" +
                        " <Style id=\"banana\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/pal5/icon57.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>" +
                        " <Style id=\"apple\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/pal5/icon56.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>" +
                        " <Style id=\"robot\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/pal4/icon57.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>\r\n"
        );
	}
    
    /**
     * Add placemark in KML file.
     * @param coordinate - the position.
     * @param obj_type - the icon type.
     */
	public void addPlacemark(Point3D coordinate, String obj_type) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
		String time_stamp = sdf.format(date);
		
		content.append("<Placemark>\r\n" + 
				"      <TimeStamp>\r\n" + 
				"        <when>"+time_stamp+"</when>\r\n" + 
				"      </TimeStamp>\r\n" + 
				"      <styleUrl>#"+obj_type+"</styleUrl>\r\n" + 
				"      <Point>\r\n" + 
				"        <coordinates>"+coordinate.toString()+"</coordinates>\r\n" + 
				"      </Point>\r\n" + 
				"    </Placemark>"
				);

	}
	
	/**
	 * Closing the file.
	 */
    public void end() {
    	content.append(
    			"  </Document>\r\n" + 
    			"</kml>"
    			);
    }
    
    /**
     * Writing to a file.
     */
    public void export() {
    	File f = new File(FOLDER+scenario+_KML);
    	try {
			PrintWriter pw = new PrintWriter(f);
			pw.write(content.toString());
			pw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Static function that creates a only one object of KML_Logger type. (Singelton Design Pattern)
     * @param scenario number.
     * @return KML_Logger object
     */
	public static KML_Logger getInstance(int scenario) {
		if (kml_logger == null) {
					kml_logger = new KML_Logger(scenario);
		}
		return kml_logger;
	}
	
	
}
