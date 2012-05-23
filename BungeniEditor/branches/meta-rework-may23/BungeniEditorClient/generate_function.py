import sys
import os

def make_function(fname):
	print "static "+fname+" "+fname+"(Object obj){"
	print "	"+fname+" xObj = ("+fname+")UnoRuntime.queryInterface("+fname+".class, obj);"
	print "	return xObj;"
	print "}"

	

if __name__ == "__main__":
	if len(sys.argv) > 1:
		funcs = sys.argv[1:]
		for elem in funcs:
			make_function(elem)

