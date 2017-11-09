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

approx: $(APPROXFILES)
	$(JC) $(JFLAGS) $(APPROXFILES) $(BNFILES)

clean:
	rm -f ./build/exact/*.class
	rm -f ./build/approx/*.class
	rm -f ./build/bn/*/*.class
