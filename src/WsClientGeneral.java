/**
 * 
 */
package atul.ws;

import net.webservicex.GeoIP;
import net.webservicex.GeoIPService;
import net.webservicex.GeoIPServiceSoap;

/**
 * @author am0011186
 *
 */
public class WsClientGeneral {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String ipAddress = null;
		if(args.length == 0) {
			System.out.println("You must pass IP address to the program.");
		} else {
			ipAddress = args[0];
		}
		
		//Create new instance of service
		GeoIPService ipService = new GeoIPService();
		//Create soap object which is port name on wsdl
		GeoIPServiceSoap ipServiceSoap = ipService.getGeoIPServiceSoap();
		//Pass param to WS operation being called. Returns custom data type.
		GeoIP geoIp = ipServiceSoap.getGeoIP(ipAddress);
		
		System.out.println("Ouput - code = " + geoIp.getCountryCode() 
			+ ", name = " + geoIp.getCountryName());
	}

}
