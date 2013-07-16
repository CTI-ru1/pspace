/**
* CoAP_AC.ino - Toyotomi HVAC Remote Control based on CoAP protocol
*
* Created by: Papadimitriou K. Georgios, Skartsilas K. Nikolaos, July 15, 2013
* Department of Computer Engineering and Informatics
* University of Patras, Greece
*
* Release into the public domain.
*/


//Include XBee,XBeeRadio and Toyotomi Libraries
#include <XBee.h>
#include <XbeeRadio.h>
#include <Toyotomi.h>

//Include CoAP Libraries
#include <coap.h>

//Include necessary header files
#include "myModeSensor.h"
#include "myButtonOnOffSensor.h"
#include "myButtonTempUpDownSensor.h"
#include "mySetFanSpeedSensor.h"
//#include "myTempSensor.h"
//#include "myPowerOnSensor.h"
//#include "myButtonAirDirectionSensor.h"
//#include "myButtonCleanAirSensor.h"
//#include "myButtonFanSpeedSensor.h"
//#include "myButtonModeSensor.h"
//#include "myButtonSwingSensor.h"
//#include "myButtonTurboSensor.h"



//Create the XbeeRadio object we'll be using
XBeeRadio xbee = XBeeRadio();
//Create a reusable response object for responses we expect to handle
XBeeRadioResponse response = XBeeRadioResponse();
//Create a reusable rx16 response object to get the address
Rx16Response rx = Rx16Response();

//Create Toyotomi object
Toyotomi toyo = Toyotomi(DEFAULT_TEMP, AUTO, DEFAULT_SP, DEFAULT_TIMER, DEFAULT_TIMER, DEFAULT_POWER);

//CoAP object
Coap coap;

//Create objects from operation classes

myModeSensor mSensor = myModeSensor("mode" , &toyo);
myButtonOnOffSensor bofSensor = myButtonOnOffSensor("on" , &toyo);
myButtonTempUpDownSensor btudSensor = myButtonTempUpDownSensor("stemp" , &toyo);
mySetFanSpeedSensor sfsSensor = mySetFanSpeedSensor("fan" , &toyo);
//myTempSensor aSensor = myTempSensor("temp" , &toyo);
//myButtonAirDirectionSensor badSensor = myButtonAirDirectionSensor("airdirection" , &toyo);
//myButtonCleanAirSensor bcaSensor = myButtonCleanAirSensor("cleanair" , &toyo);
//myButtonFanSpeedSensor bfsSensor = myButtonFanSpeedSensor("fanspeed" , &toyo);
//myButtonModeSensor bmSensor = myButtonModeSensor("resGET7" , &toyo);
//myButtonSwingSensor bsSensor = myButtonSwingSensor("swing" , &toyo);
//myButtonTurboSensor btSensor = myButtonTurboSensor("turbo" , &toyo);

//Runs only once
void setup()
{
     
  // comment out for debuging
  xbee.initialize_xbee_module();
  //start our XbeeRadio object and set our baudrate to 38400.
  xbee.begin( 38400 );
  //Initialize our XBee module with the correct values (using the default channel, channel 12)h
  xbee.init(12);

  // init coap service 
  coap.init( &xbee, &response, &rx );

  //add the resources
  coap.add_resource(&mSensor);
  coap.add_resource(&bofSensor);
  coap.add_resource(&btudSensor);
  coap.add_resource(&sfsSensor);
   //coap.add_resource(&aSensor);
  //coap.add_resource(&bsSensor);
  // coap.add_resource(&btSensor);
 //coap.add_resource(&ponSensor);
  //coap.add_resource(&badSensor);
  //coap.add_resource(&bcaSensor);
  //coap.add_resource(&bfsSensor);
  //coap.add_resource(&bmSensor);


}

void loop()
{
  //run the handler on each loop to respond to incoming requests
  coap.handler();
}
