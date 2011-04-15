find src \( -name *.java -o -name *.xml -o -name *.js -o -name *.html -o -name *.css -o -name *.properties -o -name *.jsp \) -exec cat {} \; | wc -l
