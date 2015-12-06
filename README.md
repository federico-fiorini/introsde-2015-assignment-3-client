# IntroSDE Assignment #3: SOAP Web Services - Client
**Introduction to Service Design and Engineering - University of Trento**

--------------

A SOAP client based on this [server](https://warm-brook-6204.herokuapp.com/ws/people).

Project based the following [requirements](https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/assignments/assignment-3).


####IMPLEMENTATION


Main packages:
- `introsde.assignment.soap.client`: contains the main class to be run to execute the calls to the server
- `introsde.assignment.soap.ws`: contains the classes imported with the `wsdl import` command


####HOW TO RUN IT
	# Clone the code from this repo
	git clone https://github.com/federico-fiorini/introsde-2015-assignment-3-client.git
	cd introsde-2015-assignment-3-client
	
	# To run the server
	ant execute.client

######Server part on github:
	https://github.com/federico-fiorini/introsde-2015-assignment-3-server
