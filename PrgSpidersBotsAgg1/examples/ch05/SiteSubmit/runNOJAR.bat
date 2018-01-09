@echo off
rem Example *Windows* run script for the book:
rem Programming Spiders, Bots and Aggregators
rem by Jeff Heaton(heatonj@heat-on.com)
echo This script will run a bot example without the
echo 'bot.jar' file being part of the system CLASSPATH.
echo This script assumes that the bot.jar file is located
echo in the current directory.
java -classpath ./;./bot.jar SiteSubmit %1
sspath ./:./bot.jar SiteSubmit $1
