@echo off
rem Example *Windows* build script for the book:
rem Programming Spiders, Bots and Aggregators
rem by Jeff Heaton(heatonj@heat-on.com)
echo This script will compile a bot example without the
echo 'bot.jar' file being part of the system CLASSPATH.
echo This script assumes that the bot.jar file is located
echo in the current directory.
javac -classpath ./;./bot.jar ParseXML.java

