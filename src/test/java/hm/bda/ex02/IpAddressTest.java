package hm.bda.ex02;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class IpAddressTest {

	@Test
	public void givenIP_whenFetchingCity_thenReturnsCityData() throws IOException {
		String ip         = "uplherc.upl.com";
		String dbLocation = "src/resources/GeoLite2-Country.mmdb";

		File database = new File(dbLocation);
		System.out.println(database.getName());
		DatabaseReader dbReader = new DatabaseReader.Builder(database).build();

		BufferedReader br = new BufferedReader(new FileReader("./nasa.tsv"));

		String line;

		while ((line = br.readLine()) != null) {

			String host = line.split("\\t")[0];
			System.out.println("Host");
			System.out.println(host);
			try {
				InetAddress     ipAddress       = InetAddress.getByName(host);
				System.out.println("IP");
				System.out.println(ipAddress);
				CountryResponse response = dbReader.country(ipAddress);
				String countryName = response.getCountry().getName();
				System.out.println("Country\n");
				System.out.println(countryName);
				System.out.println("------\n");
			}
			catch (UnknownHostException e) {
				System.out.println("Unknown host" + host +"\n");
				//e.printStackTrace();
			}
			catch (GeoIp2Exception e) {
				System.out.println("Unknown IP\n");
				//e.printStackTrace();
			}
		}

		assertNotNull(br);
	}

}