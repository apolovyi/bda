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
			exitCode = pgd.run(argv);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		System.exit(exitCode);
	}
}
