@echo off
rem build the bot package
rem Copyright 2001 by Jeff Heaton

javac .\com\heaton\bot\*.java
javac .\com\heaton\bot\catbot\*.java
jar cvf ..\lib\bot.jar .\com\heaton\bot\*.class
jar uvf ..\lib\bot.jar .\com\heaton\bot\catbot\*.class

