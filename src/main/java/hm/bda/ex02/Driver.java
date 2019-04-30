package hm.bda.ex02;

import org.apache.hadoop.util.ProgramDriver;

public class Driver {
	public static void main(String[] argv)
	{
		int           exitCode = -1;
		ProgramDriver pgd      = new ProgramDriver();
		try
		{
			pgd.addClass("status-count", HttpStatus.class, "A map/reduce program that counts status in log file.");
			pgd.addClass("country-count", IpAddress.class, "A map/reduce program that counts ip requests by countries in log file.");
			pgd.addClass("resolve-hostname-count", KnownHostname.class, "A map/reduce program that computes how many host names could be resolved in log file.");
			pgd.addClass("ip-hostname-count", IpAddressAndHostname.class, "A map/reduce program that computes the origin of IP address and how many host names could be resolved in log file.");
			exitCode = pgd.run(argv);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		System.exit(exitCode);
	}
}
