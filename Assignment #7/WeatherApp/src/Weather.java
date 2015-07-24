import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import org.geonames.PostalCodeSearchCriteria;
import org.geonames.InvalidParameterException;
import org.geonames.PostalCode;
import org.geonames.WeatherObservation;
import org.geonames.WebService;

public class Weather 
{

	public static void main(String[] args) 
	{
		
		// Request username	
		String user = JOptionPane.showInputDialog("Enter username");
		
		// Populate username based on input	
		WebService.setUserName(user); 
		
		
		PostalCodeSearchCriteria searchCriteria = new PostalCodeSearchCriteria();
		
		// Request postalcode and set postalcode as searchCriteria
		String postalCode = JOptionPane.showInputDialog("Enter your zip code");
		searchCriteria.setPostalCode(postalCode); 
		
		try {
			// Set country code as searchCrtieria
			String countryCode = "US";
			searchCriteria.setCountryCode(countryCode);
		} catch (InvalidParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			// Use search criteria to obtain nearest latitude and longitude of postal code
			PostalCode searchResult = WebService.findNearbyPostalCodes(searchCriteria).get(0);
			double latitude = searchResult.getLatitude(); 
			double longitude = searchResult.getLongitude();
			
			// Use longitude and latitude coordinates to search for most recent weather observation
			WeatherObservation weather = new WeatherObservation(); 
			weather = WebService.findNearByWeather(latitude, longitude);
			
			// Set default decimal format 
			DecimalFormat df = new DecimalFormat("##.###");
			
			// Print out weather information 
			System.out.println("****** Weather Information ******");
			System.out.println("Latitude: "+df.format(weather.getLatitude()));
			System.out.println("Longitude: "+df.format(weather.getLatitude()));
			System.out.println("Last Observation: "+weather.getObservationTime());
			System.out.println("Temperature: "+weather.getTemperature()+" Degrees Celcius");
			System.out.println("Humidity: "+weather.getHumidity()+"%");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}//end try	
	}//end main
}//end class

