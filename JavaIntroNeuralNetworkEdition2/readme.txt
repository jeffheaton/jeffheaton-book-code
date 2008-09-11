Introduction to Neural Networks with Java, 2nd Edition
by Jeff Heaton
ISBN: 1-60439-008-5
===============================================================================



This archive contains the following examples:

Hopfield Neural Network (Chapter 2)
-----------------------------------
/Ch2/Hopfield
Hopfield Neural network, allows you to train a 4X4 Hopfield network.


XorExample (Chapter 3)
----------------------
/Ch3/XorExample
Train a neural network to recognize the XOR function.


Delta Rule Example (Chapter 4)
------------------------------
/ch4/delta
Train using the delta rule.


Kohonen Neural Network (Chapter 6)
----------------------------------
/ch6/TestKohonen
Demonstrates the Kohonen neural network.


Optical Character Recognition (Chapter 7)
-----------------------------------------
/ch7/ocr
Handwriting recognition program tells you what letter you drew.


Traveling Salesman with Genetic Algorithms (Chapter 8)
------------------------------------------------------
/ch8/TravelingSalesman
Solve the traveling salesman problem with a genetic algorithm.


Traveling Salesman with Simulated Annealing (Chapter 9)
-------------------------------------------------------
/ch9/TravelingSalesman2
Solve the traveling salesman problem with simulated annealing.


XOR with Genetic Algorithms and Simulated Annealing (Chapter 10)
----------------------------------------------------------------
/ch10/XorMinimaExample
Search for local minimia in the XOR solution network.


Selective Prune Neural Network (Chapter 11)
-------------------------------------------
/ch11/IncreasePrune
Prune a JOONE neural network with a selective prune algorithm.


Increase Prune Neural Network (Chapter 11)
------------------------------------------
/ch11/SelectPrune
Prune a JOONE neural network with a selective prune algorithm.


Fuzzy Logic Graph (Chapter 12)
------------------------------
/ch12/Graph
Fuzzy logic example that plots charts of fuzzy sets.


Fuzzy Temperature (Chapter 12)
------------------------------
/ch12/Temperature
Fuzzy logic example that sorts temperatures into fuzzy sets.


===============================================================================

This archive contains the Java source code from the book "Introduction to 
Neural Networks with Java". If you would like to purchase this book you may
do so at the following URL:

http://www.heatonresearch.com/book/

You can also view much of the book online, at the following URL:

http://www.heatonresearch.com/articles/series/1/

===============================================================================
Table of Contents from "Introduction to Neural Networks with Java"

Programming Neural Networks in Java will show the intermediate to advanced Java 
programmer how to create neural networks. This book attempts to teach neural 
network programming through two mechanisms. First the reader is shown how to 
create a reusable neural network package that could be used in any Java program. 
Second, this reusable neural network package is applied to several real world 
problems that are commonly faced by IS programmers. This book covers such 
topics as Kohonen neural networks, multi layer neural networks, training, back 
propagation, and many other topics. 

Chapter 1: Introduction to Neural Networks
Computers can perform many operations considerably faster than a human being. 
Yet there are many tasks where the computer falls considerably short of its 
human counterpart. There are numerous examples of this. Given two pictures a 
preschool child could easily tell the difference between a cat and a dog. Yet 
this same simple problem would confound today's computers.

Chapter 2: Understanding Neural Networks
The neural network has long been the mainstay of Artificial Intelligence (AI) 
programming. As programmers we can create programs that do fairly amazing 
things. Programs can automate repetitive tasks such as balancing checkbooks or 
calculating the value of an investment portfolio. While a program could easily 
maintain a large collection of images, it could not tell us what any of those 
images are of. Programs are inherently unintelligent and uncreative. Ordinary 
computer programs are only able to perform repetitive tasks.

Chapter 3: Using Multilayer Neural Networks
In this chapter you will see how to use the feed-forward multilayer 
neural network. This neural network architecture has become the mainstay of 
modern neural network programming. In this chapter you will be shown two ways 
that you can implement such a neural network. 

Chapter 4: How a Machine Learns
In the preceding chapters we have seen that a neural network can be taught to 
recognize patterns by adjusting the weights of the neuron connections. Using 
the provided neural network class we were able to teach a neural network to 
learn the XOR problem. We only touched briefly on how the neural network was 
able to learn the XOR problem. In this chapter we will begin to see how a 
neural network learns.

Chapter 5: Understanding Back Propagation
In this chapter we shall examine one of the most common neural network 
architectures-- the feed foreword back propagation neural network. This neural 
network architecture is very popular because it can be applied to many different 
tasks. To understand this neural network architecture we must examine how it is 
trained and how it processes the pattern. The name "feed forward back 
propagation neural network" gives some clue as to both how this network is 
trained and how it processes the pattern. 

Chapter 6: Understanding the Kohonen Neural Network
In the previous chapter you learned about the feed forward back propagation 
neural network. While feed forward neural networks are very common, they are 
not the only architecture for neural networks. In this chapter we will examine 
another very common architecture for neural networks.

Chapter 7: OCR with the Kohonen Neural Network
In the previous chapter you learned how to construct a Kohonen neural network. 
You learned that a Kohonen neural network can be used to classify samples into 
several groups. In this chapter we will closely examine a specific application 
of the Kohonen neural network. The Kohonen neural network will be applied to 
Optical Character Recognition (OCR).

Chapter 8: Understanding Genetic Algorithms
In the previous chapter you saw a practical application of the Kohonen neural 
network. Up to this point the book has focused primarily on neural networks. 
In this and Chapter 9 we will focus on two artificial intelligence technologies 
not directly related to neural networks. We will begin with the genetic 
algorithm. In the next chapter you will learn about simulated annealing. 
Finally Chapter 10 will apply both of these concepts to neural networks. 
Please note that at this time JOONE, which was covered in previous chapters, 
has no support for GAs’ or simulated annealing so we will build it.

Chapter 9: Understanding Simulated Annealing
In this chapter we will examine another technique that allows you to train 
neural networks. In Chapter 8 you were introduced to using genetic algorithms 
to train a neural network. This chapter will show you how you can use another 
popular algorithm, which is named simulated annealing. Simulated annealing has 
become a popular method of neural network training. As you will see in this 
chapter, it can be applied to other uses as well.

Chapter 10: Eluding Local Minima
In Chapter 5 backpropagation was introduced. Backpropagation is a very 
effective means of training a neural network. However, there are some inherent 
flaws in the back propagation training algorithm. One of the most fundamental 
flaws is the tendency for the backpropagation training algorithm to fall into 
a “local minima”. A local minimum is a false optimal weight matrix that 
prevents the backpropagation training algorithm from seeing the true solution.

Chapter 11: Pruning Neural Networks
In chapter 10 we saw that you could use simulated annealing and genetic 
algorithms to better train a neural network. These two techniques employ 
various algorithms to better fit the weights of the neural network to the 
problem that the neural network is to be applied to. These techniques do 
nothing to adjust the structure of the neural network.

Chapter 12: Fuzzy Logic
In this chapter we will examine fuzzy logic. Fuzzy logic is a branch of 
artificial intelligence that is not directly related to the neural networks 
that we have been examining so far. Fuzzy logic is often used to process data 
before it is fed to a neural network, or to process the outputs from the neural 
network. In this chapter we will examine cases of how this can be done. We will 
also look at an example program that uses fuzzy logic to filter incoming SPAM 
emails.

Appendix A.	JOONE Reference
Information about JOONE.

Appendix B.	Mathematical Background
Discusses some of the mathematics used in this book.

Appendix C.	Compiling Examples under Windows
How to install JOONE and the examples on Windows.

Appendix D.	Compiling Examples under Linux/UNIX
How to install JOONE and the examples on UNIX/Linux.
