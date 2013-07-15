/**
 * Arduino Coap Garden Sensors
 * Monopatis Dimitris
 */


//Include XBEE Libraries
#include <XBee.h>
#include <XbeeRadio.h>
//Include CoAP Libraries
#include <coap.h>
#include "myGETSensor.h"
#include "myGETtemp.h"
#include "myGETmoist.h"

//Create the XbeeRadio object we'll be using
XBeeRadio xbee = XBeeRadio();
//Create a reusable response object for responses we expect to handle
XBeeRadioResponse response = XBeeRadioResponse();
//Create a reusable rx16 response object to get the address
Rx16Response rx = Rx16Response();

//CoAP object
Coap coap;

myGETSensor aSensor = myGETSensor("brightness" , A2);
myGETtemp dSensor = myGETtemp("temperature" , A1);
myGETmoist eSensor = myGETmoist("moisture" , A3);


//Runs only once
void setup()
{
  pinMode(7, OUTPUT);  

  // comment out for debuging
  xbee.initialize_xbee_module();
  //start our XbeeRadio object and set our baudrate to 38400.
  xbee.begin( 38400 );
  //Initialize our XBee module with the correct values (using the default channel, channel 12)h
  xbee.init(12);

  // init coap service 
  coap.init( &xbee, &response, &rx );

  //add the resourse resGET
  coap.add_resource(&aSensor);
  coap.add_resource(&dSensor);
  coap.add_resource(&eSensor);

}

void loop()
{
  //run the handler on each loop to respond to incoming requests
  coap.handler();
}




