#!/bin/bash

echo "Deleting old build ..."
rm ./bin/*
echo "Compiling ... "
javac ./src/Init.java -d ./bin/ -cp ./src/:./lib/*
echo "Running ... "
cd bin
java Init
