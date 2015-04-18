main: build/JavaParser.java src/JavaCompiler.java
	javac src/JavaCompiler.java -d build -classpath lib/java-cup-11a.jar -sourcepath src:build

build/JavaParser.java: build/JavaLexer.java src/JavaParser.cup
	java -jar lib/java-cup-11a.jar -interface -parser JavaParser src/JavaParser.cup
	mv JavaParser.java sym.java build

build/JavaLexer.java: src/JavaLexer.jflex
	jflex src/JavaLexer.jflex -d build

clean:
	find build/* -not -name '.keep' | xargs rm -rf
