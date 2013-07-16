/**
* mySetFanSpeedSensor.h - Toyotomi HVAC Remote Control Library which represents the FAN-SPEED OPERATION
*
* Created by: Papadimitriou K. Georgios, Skartsilas K. Nikolaos, July 15, 2013
* Department of Computer Engineering and Informatics
* University of Patras, Greece
*
* Release into the public domain.
*/

#include <coapSensor.h>
#include <Toyotomi.h>

class mySetFanSpeedSensor : public CoapSensor 
{
public:
//Create Toyotomi object to use Toyotomi's class methods
  Toyotomi *toyo;
  mySetFanSpeedSensor(String name, Toyotomi *toyotomi): CoapSensor(name)
  {
    this->toyo=toyotomi;
  }
  //CoAP's get function which gets the current fan_speed of Toyotomi AC
  void get_value( uint8_t* output_data, size_t* output_data_len)
  {
    *output_data_len = sprintf( (char*)output_data, "%d", this->toyo->getFanSpeed());
  }
  //CoAP's set function which sets the current fan-speed to Toyotomi AC
  void set_value(uint8_t* input_data, size_t input_data_len, uint8_t* output_data, size_t* output_data_len)
  {
    if(input_data[0]=='0')
    {
      this->toyo->setFanSpeed(NONE_SP);
    }
    else if(input_data[0]=='1')
    {
       this->toyo->setFanSpeed(DEFAULT_SP);
    }
    else if(input_data[0]=='2')
    {
       this->toyo->setFanSpeed(LOW_SP);
    }
    else if(input_data[0]=='3')
    {
       this->toyo->setFanSpeed(MED_SP);
    }
    else if(input_data[0]=='4')
    {
       this->toyo->setFanSpeed(HIGH_SP);
    }
    
     *output_data_len = sprintf( (char*)output_data, "%d", this->toyo->getFanSpeed());
   
  }
  
};

