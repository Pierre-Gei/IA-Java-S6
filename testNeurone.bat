@echo off
cd src
javac resources/Support/neurone/iNeurone.java
javac resources/Support/neurone/Neurone.java
javac resources/Support/neurone/NeuroneHeaviside.java
javac resources/Support/neurone/NeuroneReLu.java
javac resources/Support/neurone/NeuroneSigmoid.java
javac resources/Support/neurone/NeuroneTanh.java
javac resources/Support/neurone/testNeurone.java
cd ..
java -cp src resources.Support.neurone.testNeurone
pause