#!/bin/sh
# *UNIX* Run script from the book:
# Programming Spiders, Bots and Aggregators in Java
# This script is designed to run the example contained in
# this directory. This file assumes that the required
# bot.jar file is located in the current directory.
# if the bot.jar file is part of the system CLASSPATH
# then the classpath portion of the below command can
# be removed.
java -classpath ./:./bot.jar ParseQIF $1
