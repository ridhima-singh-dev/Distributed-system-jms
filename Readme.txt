This Assignment involves implementing the LifeCo system using Message-Oriented Middleware (MOM) i.e, Java Message Service (JMS), for interaction between the three quotation services, the broker, and the client. 

Quotation Services: 
In the LifeCo system, there are three quotation services that provide insurance quotes auldfellas, Dodgygeezers and GirlsAllowed. These services use JMS to asynchronously publish their quotations to a common topic, which is QUOTATIONS in this case. Each service acts as a JMS publisher, sending quotations as JMS messages to the QUOTATIONS topic. This decoupled architecture allows multiple quotation providers(auldfellas, Dodgygeezers and GirlsAllowed) to operate independently without direct dependencies on one another.

Broker Service: 
The broker service is responsible for getting quotations from different providers. It uses JMS to subscribe to the QUOTATIONS topic as a consumer. When a quotation provider publishes a quotation to the topic, the broker consumes these messages asynchronously. This design ensures that the broker can process quotations from multiple providers without blocking or slowing down the system.

Offer Generation and Sending: 
The broker processes the quotations it receives from the QUOTATIONS topic and combines them with client information to create offer messages. These offer messages are also sent asynchronously using JMS to the OFFERS queue. The broker acts as a producer in this case. Clients can later consume these offer messages from the OFFERS queue.

Client: 
In the client service, clients request insurance quotes by sending a client message to the broker. This client message includes client information. Clients use JMS to send these messages asynchronously to the APPLICATIONS topic. This architecture allows clients to request quotes without direct, synchronous communication with the broker.


Broker Service: The broker service, acting as a consumer this time, subscribes to the APPLICATIONS topic to receive client messages. When a client sends a request to the APPLICATIONS topic, the broker consumes it and initiates the quotation process by forwarding the client message to the relevant quotation providers. This design allows the broker to serve multiple clients without blocking client requests.

Client Response Handling:
After processing the client request, the broker generates and sends offer messages to the OFFERS queue using JMS. Clients as consumers can then asynchronously consume these offer messages to view their insurance offers.