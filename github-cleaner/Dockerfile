from debian:11
run apt-get update -y
run apt-get install -y curl vim openjdk-11-jdk git make
run mkdir -p /root/stuff/scripts/java/github-cleaner/src
run mkdir -p /root/stuff/scripts/java/github-cleaner/dist
run mkdir -p /root/stuff/scripts/java/github-cleaner/bin
run mkdir -p /root/stuff/scripts/java/github-cleaner/lib
copy src /root/stuff/scripts/java/github-cleaner/src
copy lib /root/stuff/scripts/java/github-cleaner/lib
copy Makefile /root/stuff/scripts/java/github-cleaner
copy Manifest.txt /root/stuff/scripts/java/github-cleaner
entrypoint ["bash", "-c", "(cd /root/stuff/scripts/java/github-cleaner; make); java -jar /root/stuff/scripts/java/github-cleaner/dist/github-cleaner.jar"]
