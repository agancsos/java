from debian:11
run apt-get update -y
run apt-get install -y curl vim openjdk-11-jdk git make
run mkdir -p /root/stuff/scripts/java/debian-cleaner/src
run mkdir -p /root/stuff/scripts/java/debian-cleaner/dist
run mkdir -p /root/stuff/scripts/java/debian-cleaner/bin
run mkdir -p /root/stuff/scripts/java/debian-cleaner/lib
copy src /root/stuff/scripts/java/debian-cleaner/src
copy lib /root/stuff/scripts/java/debian-cleaner/lib
copy Makefile /root/stuff/scripts/java/debian-cleaner
copy Manifest.txt /root/stuff/scripts/java/debian-cleaner
entrypoint ["bash", "-c", "(cd /root/stuff/scripts/java/debian-cleaner; make); java -jar /root/stuff/scripts/java/debian-cleaner/dist/debian-cleaner.jar"]
