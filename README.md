# NeuralNetworkGraph
This is a utility to plot Neural Network of given spec.

neural.network.graph.NeuralNetworkGraph.plot(..) is the public API to use for plotting. The API takes 3 arguments: 
  a) Map Key -> Neurons Layer Number (1,2,3...N), Value -> List of Neurons Names, 
  b) Map Key -> Neuron Name (includes biased neurons), Value -> Neuron object
  c) Map Key -> Biased Neuron Layer Number (1,2,3...N), Value -> Biased Neuron Name
  
Example usage is provided in neural.network.graph.NeuralNetworkGraph.main() 

How to setup the library?
Just use the jars provided under lib. 

How to build the project?
ant package

