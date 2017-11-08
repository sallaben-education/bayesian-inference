#
# File: Makefile
# Creator: Steve Allaben
# Last modified: <Tue, 07 Nov 2017 sallaben>
#

PROGRAMS = inference

JC = javac
JFLAGS = -g -cp . -d ./build
EXACTFILES = ./source/exact/*.java
APPROXFILES = ./source/approx/*.java
BNFILES = ./source/bn/*/*.java

exact: $(EXACTFILES)
	$(JC) $(JFLAGS) $(EXACTFILES) $(BNFILES)
	#java -cp ./build exact/App aima-alarm.xml B J true M true

approx: $(APPROXFILES)
	$(JC) $(JFLAGS) $(APPROXFILES) $(BNFILES)
	#java -cp ./build approx/App 10000 aima-alarm.xml B J true M true

clean:
	rm -f ./build/exact/*.class
	rm -f ./build/approx/*.class
	rm -f ./build/bn/*/*.class