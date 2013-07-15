/**
 * Arduino Coap Garden Alarms Programmer
 * Monopatis Dimitris
 */


//Include XBEE Libraries
#include <XBee.h>
#include <XbeeRadio.h>
#include <Time.h>
#include <TimeAlarms.h>
//Include CoAP Libraries
#include <coap.h>
#include "myPOSTSensor.h"
#include "myPOSTprogram.h"
#include "myPOSTtime.h"

//Create the XbeeRadio object we'll be using
XBeeRadio xbee = XBeeRadio();
//Create a reusable response object for responses we expect to handle
XBeeRadioResponse response = XBeeRadioResponse();
//Create a reusable rx16 response object to get the address
Rx16Response rx = Rx16Response();

//CoAP object
Coap coap;

myPOSTSensor bSensor = myPOSTSensor("valve3" , 3);
myPOSTtime cSensor = myPOSTtime("time");
myPOSTprogram fSensor = myPOSTprogram("program", 3);

//Runs only once
void setup()
{
  pinMode(3, OUTPUT);   
  // comment out for debuging
  xbee.initialize_xbee_module();
  //start our XbeeRadio object and set our baudrate to 38400.
  xbee.begin( 38400 );
  //Initialize our XBee module with the correct values (using the default channel, channel 12)h
  xbee.init(12);

  // init coap service 
  coap.init( &xbee, &response, &rx );

  //add the resourse resGET
  coap.add_resource(&bSensor);
  coap.add_resource(&cSensor);
  coap.add_resource(&fSensor);

}

void loop()
{
  //run the handler on each loop to respond to incoming requests
  coap.handler();
}




