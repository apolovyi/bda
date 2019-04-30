package hm.bda.ex02;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpAddressAndHostname extends Configured implements Tool{
	public static class StatusMapper extends Mapper<Object, Text, Text, IntWritable>{
		private final static Text        HOST_KNOWN   = new Text("KNOWN HOST");
		private final static Text        HOST_UNKNOWN = new Text("UNKNOWN HOST");
		private final static IntWritable ONE          = new IntWritable(1);

		private Text           country  = new Text();
		private DatabaseReader dbReader = null;

		private void setIpDatabase() throws IOException{
			String      dbLocation = "/GeoLite2-Country.mmdb";
			InputStream in         = getClass().getResourceAsStream(dbLocation);
			this.dbReader = new DatabaseReader.Builder(in).build();
			System.out.println(dbLocation);
			System.out.println(dbReader.toString());
		}

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException{
			String dataRow = value.toString();
			String host    = dataRow.split("\\t")[0];

			if(dbReader == null){
				System.out.println("Setting DB Reader for IP lookup");
				setIpDatabase();
			}
			//Get ip location
			try{
				InetAddress     ip          = InetAddress.getByName(host);
				CountryResponse response    = dbReader.country(ip);
				String          countryName = response.getCountry().getName();
				country.set(countryName);
				context.write(country, ONE);
				context.write(HOST_KNOWN, ONE);

			}
			catch(UnknownHostException e){
				System.out.println("Unknown host" + host);
				context.write(HOST_UNKNOWN, ONE);
				e.printStackTrace();
			}
			catch(GeoIp2Exception e){
				System.out.println("Unknown IP");
				e.printStackTrace();
			}
		}
	}


	public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException{
			int sum = 0;
			for(IntWritable val: values){
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}

	}

	public static void main(String[] args) throws Exception{
		ToolRunner.run(new Configuration(), new IpAddressAndHostname(), args);
	}

	@Override
	public int run(String[] args) throws Exception{
		Configuration conf = new Configuration();

		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if(otherArgs.length != 2){
			System.err.println("Usage: ip-hostname-count <in> <out>");
			return 0;
		}
		setConf(conf);

		Job job = Job.getInstance(conf, "ip and hostname classification");
		job.setJarByClass(IpAddressAndHostname.class);
		job.setMapperClass(StatusMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		boolean result = job.waitForCompletion(true);
		return result ? 0 : 1;
	}
}

