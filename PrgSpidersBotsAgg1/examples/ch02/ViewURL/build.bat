@echo off
rem Example *Windows* build script for the book:
rem Programming Spiders, Bots and Aggregators
rem by Jeff Heaton(heatonj@heat-on.com)
echo This build script assumes that the file 'bot.jar'
echo is in the CLASSPATH. You may also try buildNOJAR to
echo use the 'bot.jar' located in this directory.
javac ViewURL.java
