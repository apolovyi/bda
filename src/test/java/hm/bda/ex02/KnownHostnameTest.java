package hm.bda.ex02;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class KnownHostnameTest {

	@Test
	public void givenIP_whenFetchingCity_thenReturnsCityData() throws IOException {
		String ip         = "uplherc.upl.com";
		BufferedReader br = new BufferedReader(new FileReader("./nasa.tsv"));

		String line;

		while ((line = br.readLine()) != null) {

			String host = line.split("\\t")[0];
			System.out.println("Host");
			System.out.println(host);
			try {
				//InetAddress ipAddress = InetAddress.getByName(host);
				InetAddress ipAddress = Inet4Address.getByName(host);
				System.out.println(ipAddress);
				System.out.println("Host " + host +"\n");
			}
			catch (UnknownHostException e) {
				System.out.println("Unknown host " + host +"\n");
			}
		}

		assertNotNull(br);
	}
}