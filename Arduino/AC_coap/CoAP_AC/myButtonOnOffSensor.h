/**
* myButtonOnOffSensor.h - Toyotomi HVAC Remote Control Library which represents the ON-OFF Button
*
* Created by: Papadimitriou K. Georgios, Skartsilas K. Nikolaos, July 15, 2013
* Department of Computer Engineering and Informatics
* University of Patras, Greece
*
* Release into the public domain.
*/

#include <coapSensor.h>
#include <Toyotomi.h>

class myButtonOnOffSensor : public CoapSensor 
{
public:
  //Create Toyotomi object to use Toyotomi's class methods
  Toyotomi *toyo;
  myButtonOnOffSensor(String name, Toyotomi *toyotomi): CoapSensor(name)
  {
    this->toyo=toyotomi; 
  }
  //CoAP's get function which gets the current state of Toyotomi AC (1:on, 0:off) 
  void get_value( uint8_t* output_data, size_t* output_data_len)
  {
    *output_data_len = sprintf( (char*)output_data, "%d", this->toyo->isPoweredOn() ); 
  }
  //CoAP's set function which sets the state of Toyotomi AC to on/off
  void set_value(uint8_t* input_data, size_t input_data_len, uint8_t* output_data, size_t* output_data_len)
  {
     if(this->toyo->isPoweredOn()==0) 
     {
       this->toyo->powerOn();//Called 5 times for synchronization reasons
       this->toyo->powerOn();
       this->toyo->powerOn();
       this->toyo->powerOn();
       this->toyo->powerOn();
     }
     else if(this->toyo->isPoweredOn()==1) 
     {
       this->toyo->powerOff();//Called 5 times for synchronization reasons
       this->toyo->powerOff();
       this->toyo->powerOff();
       this->toyo->powerOff();
       this->toyo->powerOff();
     }
     *output_data_len = sprintf( (char*)output_data, "%d", this->toyo->isPoweredOn());
  }
  
};

