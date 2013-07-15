#include "coapSensor.h"
#include <Time.h>
#include <TimeAlarms.h>

class myPOSTprogram : 
public CoapSensor 
{
public:
  int pin;
  myPOSTprogram(String name, int pin): 
  CoapSensor(name)
  {
    this->pin = pin;
    pinMode(pin, OUTPUT);
    digitalWrite(pin, LOW);  
  }
  void get_value( uint8_t* output_data, size_t* output_data_len)
  {
    *output_data_len = sprintf( (char*)output_data, "%d", Alarm.count() );
  }
  void set_value(uint8_t* input_data, size_t input_data_len, uint8_t* output_data, size_t* output_data_len)
  {
    String s((char*) input_data);
    int start = s.indexOf("H") + 1;
    int end = s.lastIndexOf("H");
    String sub = s.substring(start, end);
    int hours = sub.toInt();
    start = s.indexOf("M") + 1;
    end = s.lastIndexOf("M");
    sub = s.substring(start, end);
    int minutes = sub.toInt();
    start = s.indexOf("S") + 1;
    end = s.lastIndexOf("S");
    sub = s.substring(start, end);
    int seconds = sub.toInt();
    //Duration
    start = s.indexOf("D") + 1;
    end = s.lastIndexOf("D");
    sub = s.substring(start, end);
    int duration = sub.toInt();
    //Weekday
    start = s.indexOf("W") + 1;
    end = s.lastIndexOf("W");
    sub = s.substring(start, end);
    timeDayOfWeek_t weekDay;
    weekDay = (timeDayOfWeek_t) sub.toInt();
    int ID1,ID2;
    if (weekDay > 0 && weekDay<8) {
      ID1 = Alarm.alarmRepeat(weekDay, hours, minutes, seconds, onOnceOnly); 
      seconds = seconds + duration;
      minutes = minutes + seconds/60;
      hours = hours + minutes/60;
      hours = hours%24;
      minutes = minutes%60;
      seconds = seconds%60;
      ID2 = Alarm.alarmRepeat(weekDay, hours, minutes, seconds, offOnceOnly);
    }
    else if (weekDay == 0)
    {
      ID1 = Alarm.alarmRepeat(hours, minutes, seconds, onOnceOnly); 
      seconds = seconds + duration;
      minutes = minutes + seconds/60;
      hours = hours + minutes/60;
      hours = hours%24;
      minutes = minutes%60;
      seconds = seconds%60;
      ID2 = Alarm.alarmRepeat(hours, minutes, seconds, offOnceOnly);
    }
    else if (weekDay == 10)
    {
      ID1 = Alarm.alarmOnce(hours, minutes, seconds, onOnceOnly); 
      seconds = seconds + duration;
      minutes = minutes + seconds/60;
      hours = hours + minutes/60;
      hours = hours%24;
      minutes = minutes%60;
      seconds = seconds%60;
      ID2 = Alarm.alarmOnce(hours, minutes, seconds, offOnceOnly);
    }
    *output_data_len = sprintf( (char*)output_data, "Day %d %d:%d:%d for %d ID1 %d id2 %d", weekDay, hours, minutes, seconds-duration, duration, ID1, ID2 );
    this->changed = true;
  }
  void check(void)
  {
    Alarm.delay(100); // wait one second between clock display
  }
};


