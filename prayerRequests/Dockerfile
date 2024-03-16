from debian:11
run apt-get update -y
run apt-get install -y curl vim openjdk-11-jdk make maven
copy . /root
entrypoint ["bash", "-c", "(cd /root; mvn clean && mvn package && mvn exec:java -Dexec.mainClass=MainClass -Dexec.args='-f config.json')"]
