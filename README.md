Implemented jobs:
status-count
country-count
known-hostname-count
ip-hostname-count


yarn jar ex02.jar status-count "hdfs://master:9000/bda_course/exercise02/nasa.tsv" hdfs://master:9000/output-nasa-status-count/
hdfs dfs -cat hdfs://master:9000/output-nasa-status-count/part-r-00000

yarn jar ex02.jar country-count "hdfs://master:9000/bda_course/exercise02/nasaS1.tsv" hdfs://master:9000/output-nasa-country-count/
hdfs dfs -cat hdfs://master:9000/output-nasa-country-count/part-r-00000

yarn jar ex02.jar known-hostname-count "hdfs://master:9000/bda_course/exercise02/nasaS1.tsv" hdfs://master:9000/output-nasa-known-hostname-count/
hdfs dfs -cat hdfs://master:9000/output-nasa-known-hostname-count/part-r-00000

yarn jar ex02.jar ip-hostname-count "hdfs://master:9000/bda_course/exercise02/nasaS1.tsv" hdfs://master:9000/output-nasa-ip-hostname-count/
hdfs dfs -cat hdfs://master:9000/output-nasa-ip-hostname-count/part-r-00000

yarn jar ex02.jar ip-hostname-count "hdfs://master:9000/bda_course/exercise02/nasa1000.tsv" hdfs://master:9000/output-nasa-ip-hostname-count1/
hdfs dfs -cat hdfs://master:9000/output-nasa-ip-hostname-count1/part-r-00000



sudo chown -R hduser:hdgroup /home/hduser/

sudo cp /vagrant/files/nasaS1.tsv /home/hduser
sudo cp /vagrant/files/ex02.jar /home/hduser

hdfs dfs -mkdir -p /bda_course/exercise02

hdfs dfs -put nasa.tsv /bda_course/exercise02
