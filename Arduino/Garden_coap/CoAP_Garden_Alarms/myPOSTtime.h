#include "coapSensor.h"

class myPOSTtime : 
public CoapSensor 
{
public:
  myPOSTtime(String name): 
  CoapSensor(name)
  {
    setTime(12,0,0,1,6,13);
  }
  void get_value( uint8_t* output_data, size_t* output_data_len)
  {
    *output_data_len = sprintf( (char*)output_data, "%lu", now() ); 
  }
  void set_value(uint8_t* input_data, size_t input_data_len, uint8_t* output_data, size_t* output_data_len)
  {
    long pctime = atol((const char*)input_data);
    // check the integer is a valid time (greater than Jan 1 2013)
    if( pctime >= 1357041600) // Jan 1 2013 
    { 
      // Sync Arduino clock to the time received on the serial port
      setTime(pctime);
      *output_data_len = sprintf( (char*)output_data, "New time is: %ld", pctime );
    }
  }
};


