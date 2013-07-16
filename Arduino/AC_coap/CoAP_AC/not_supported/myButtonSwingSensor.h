#include <coapSensor.h>
#include <Toyotomi.h>

class myButtonSwingSensor : public CoapSensor 
{
public:
  Toyotomi *toyo;
  myButtonSwingSensor(String name, Toyotomi *toyotomi): CoapSensor(name)
  {
    this->toyo=toyotomi;
  }
  void get_value( uint8_t* output_data, size_t* output_data_len)
  {
    *output_data_len = sprintf( (char*)output_data, "%d", this->toyo->isPoweredOn());
  }
  void set_value(uint8_t* input_data, size_t input_data_len, uint8_t* output_data, size_t* output_data_len)
  {
     this->toyo->buttonSwing();
     *output_data_len = sprintf( (char*)output_data, "%d", this->toyo->isPoweredOn());
  }
  
};

