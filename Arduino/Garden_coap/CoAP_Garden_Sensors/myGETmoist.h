#include "coapSensor.h"

class myGETmoist : 
public CoapSensor 
{
public:
  int pin, status;
  myGETmoist(String name, int pin): 
  CoapSensor(name)
  {
    this->pin = pin;
    this->status = 0;
  }
  void get_value( uint8_t* output_data, size_t* output_data_len)
  {
    digitalWrite(7, HIGH);
    int newStatus = analogRead(this->pin);  // read the value from the sensor
    digitalWrite(7, LOW);
    *output_data_len = sprintf( (char*)output_data, "%d", newStatus ); 
  }
  void check(void)  
  {
    static unsigned long timestamp = 0;
    if(millis() - timestamp > 5000)
    {
      digitalWrite(7, HIGH);
      int newStatus = analogRead(this->pin);  // read the value from the sensor
      digitalWrite(7, LOW);
      if(newStatus != this->status)
      {
        this->changed = true;
        this->status = newStatus;
      }
      timestamp = millis();
    }
  }
};

