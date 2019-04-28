package hm.bda.ex02;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class KnownHostname {
	public static class StatusMapper extends Mapper<Object, Text, Text, IntWritable> {
		private final static IntWritable    one         = new IntWritable(1);
		private              Text           hostKnown   = new Text("known");
		private              Text           hostUnknown = new Text("known");

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String dataRow = value.toString();
			String host    = dataRow.split("\\t")[0];
			try {
				InetAddress ip = InetAddress.getByName(host);
				//ipText.set(ip.toString());
				context.write(hostKnown, one);

			}
			catch (UnknownHostException e) {
				context.write(hostUnknown, one);
				System.out.println("Unknown host" + host);
				e.printStackTrace();
			}
		}
	}


	public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values,
				Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}

	}

	public static void main(String[] args) throws Exception {
		Configuration conf      = new Configuration();
		String[]      otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: known-hostname-count <in> <out>");
			System.exit(2);
		}
		Job job = Job.getInstance(conf, "known hostname count");

		job.setJarByClass(KnownHostname.class);
		job.setMapperClass(KnownHostname.StatusMapper.class);
		job.setCombinerClass(KnownHostname.IntSumReducer.class);
		job.setReducerClass(KnownHostname.IntSumReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
