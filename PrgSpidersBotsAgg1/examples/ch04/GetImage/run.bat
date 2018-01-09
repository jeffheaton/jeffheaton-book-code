@echo off
rem Example *Windows* run script for the book:
rem Programming Spiders, Bots and Aggregators
rem by Jeff Heaton(heatonj@heat-on.com)
echo This run script assumes that the file 'bot.jar'
echo is in the CLASSPATH. You may also try runNOJAR to
echo use the 'bot.jar' located in this directory.
java GetImage %1
