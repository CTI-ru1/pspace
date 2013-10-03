/**
* myButtonTempUpDownSensor.h - Toyotomi HVAC Remote Control Library which represents the TEMP-UP & TEMP-DOWN Buttons
*
* Created by: Papadimitriou K. Georgios, Skartsilas K. Nikolaos, July 15, 2013
* Department of Computer Engineering and Informatics
* University of Patras, Greece
*
* Release into the public domain.
*/
#include <coapSensor.h>
#include <Toyotomi.h>

class myButtonTempUpDownSensor : public CoapSensor 
{
public:
  //Create Toyotomi object to use Toyotomi's class methods
  Toyotomi *toyo;
  myButtonTempUpDownSensor(String name, Toyotomi *toyotomi): CoapSensor(name)
  {
    this->toyo=toyotomi;
  }
  //CoAP's get function which gets the current temperature of Toyotomi AC
  void get_value( uint8_t* output_data, size_t* output_data_len)
  {
    *output_data_len = sprintf( (char*)output_data, "%d",  this->toyo->getTemperature()); 
  }
  //CoAP's set function which gets the current temperature of Toyotomi AC and increases/decreases it by 1
  void set_value(uint8_t* input_data, size_t input_data_len, uint8_t* output_data, size_t* output_data_len)
  {
     int temper; 
     if(*input_data=='+')
     {
       temper=this->toyo->getTemperature()+1;
       this->toyo->setTemperature(temper);
     }
     else if(*input_data=='-')
     {
       temper=this->toyo->getTemperature()-1;
       this->toyo->setTemperature(temper);
     }
     *output_data_len = sprintf( (char*)output_data, "%d", this->toyo->getTemperature());
    
  }
  
};

