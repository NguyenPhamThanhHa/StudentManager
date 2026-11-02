@echo off
if not exist out mkdir out
javac -encoding UTF-8 -cp "lib/*;src" -d out src/Main.java src/view/*.java src/model/*.java src/Data_Access_Object/*.java
if errorlevel 1 pause & exit /b
java -cp "lib/*;out" Main
pause