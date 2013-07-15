#include "coapSensor.h"

class myGETtemp : 
public CoapSensor 
{
public:
  int pin;
  float status;
  myGETtemp(String name, int pin): 
  CoapSensor(name)
  {
    this->pin = pin;
    this->status = 0;
  }
  void get_value( uint8_t* output_data, size_t* output_data_len)
  {
    char temp[10];
    dtostrf(status,1,2,temp);
    *output_data_len = sprintf( (char*)output_data, "%s", temp ); 
  }
  void check(void)
  {
    static unsigned long timestamp = 0;
    if(millis() - timestamp > 500)
    {
      float newStatus = analogRead(this->pin);  // read the value from the sensor
      newStatus = newStatus * 0.48828125;
      if(newStatus != this->status)
      {
        this->changed = true;
        this->status = newStatus;
      }
      timestamp = millis();
    }
  }
};
