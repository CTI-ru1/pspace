/**
* myModeSensor.h - Toyotomi HVAC Remote Control Library which represents the MODE OPERATION
*
* Created by: Papadimitriou K. Georgios, Skartsilas K. Nikolaos, July 15, 2013
* Department of Computer Engineering and Informatics
* University of Patras, Greece
*
* Release into the public domain.
*/

#include <coapSensor.h>
#include <Toyotomi.h>

class myModeSensor : public CoapSensor 
{
public:
//Create Toyotomi object to use Toyotomi's class methods
  Toyotomi *toyo;
  myModeSensor(String name, Toyotomi *toyotomi): CoapSensor(name)
  {
    this->toyo=toyotomi;
   
  }
  //CoAP's get function which gets the current mode of Toyotomi AC
  void get_value( uint8_t* output_data, size_t* output_data_len)
  {
    *output_data_len = sprintf( (char*)output_data, "%d", this->toyo->getMode()); 
  }
  //CoAP's set function which sets the current mode to Toyotomi AC
  void set_value(uint8_t* input_data, size_t input_data_len, uint8_t* output_data, size_t* output_data_len)
  {
    if(input_data[0]=='0')
    {
      this->toyo->setMode(AUTO);
    }
    else if(input_data[0]=='1')
    {
       this->toyo->setMode(COOL);
    }
    else if(input_data[0]=='2')
    {
       this->toyo->setMode(DRY);
    }
    else if(input_data[0]=='3')
    {
       this->toyo->setMode(HEAT);
    }
    else if(input_data[0]=='4')
    {
       this->toyo->setMode(FAN);
    }
 
    *output_data_len = sprintf( (char*)output_data, "%d", this->toyo->getMode());
  }
};

