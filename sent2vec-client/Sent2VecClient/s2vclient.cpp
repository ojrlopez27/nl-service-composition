//
//  S2V ZMQ client in C++
//  Connects REQ socket to tcp://localhost:5555
//  Sends "your message" to server, expects "semantically similar service descriptions" back
//
#include <zmq.hpp>
#include <string>
#include <iostream>

int main ()
{
    char utterances[5][40] = {
        "book flight ticktes to Milan",
        "book a hotel accomodation in Milan",
        "what are activities to do in Milan",
        "best restaurants for dinner in Milan",
        "weather in Milan during May 2019"
    };
    //  Prepare our context and socket
    zmq::context_t context (1);
    zmq::socket_t socket (context, ZMQ_REQ);
    
    std::cout << "Connecting to S2V server..." << std::endl;
    socket.connect ("tcp://localhost:5555");
    
    //  Send 5 utterances/task descriptions, waiting each time for a response
    for (int request_nbr = 0; request_nbr != 11; request_nbr++) {
        zmq::message_t request (40);
        //use same utterance for uniform comparison accross all # service description scenarios
        memcpy (request.data (), utterances[0], 40);
        std::cout << "Sending message " << request_nbr << "..." << std::endl;
        socket.send (request);
        
        //  Get the reply.
        zmq::message_t reply;
        socket.recv (&reply);
        std::cout << "Received sentences " << request_nbr << std::endl;
    }
    socket.disconnect("tcp://localhost:5555");
    return 0;
}
