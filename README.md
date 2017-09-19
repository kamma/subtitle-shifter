Simple tool for add/sub time shift in subtitles

at this moment only SRT format is supported

tool is written in Java/Swing

binary distribution is located in /target directory

![alt text](/ss-ss.png)

To use atomatic translation with Google Translate behind proxy, set these environment properties (windows: set, linux/unix: export)

shifter.proxy.host

shifter.proxy.port

shifter.proxy.user

shifter.proxy.password


create eclipse project
======================
mvn eclipse:eclipse

build
=====
mvn clean install

run
===
mvn exec:java

or

java -jar shifter-0.0.1-SNAPSHOT.jar