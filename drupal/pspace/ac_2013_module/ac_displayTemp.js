/*
* ac_displayTemp.js - Toyotomi HVAC Remote Control GUI
*
* Created by: Papadimitriou K. Georgios, Skartsilas K. Nikolaos, July 15, 2013
* Department of Computer Engineering and Informatics
* University of Patras, Greece
*
* Release into the public domain.
*/

//function which takes 1 argument(value:# of temperature) and displays 
//it as segment display on GUI (file segment-display.js is required)
function showSSD(value)
{
  //document.write(value);
  var display = new SegmentDisplay('myDisplay');
  display.pattern         = "##";
  display.displayAngle    = 0.31;
  display.digitHeight     = 0.63;
  display.digitWidth      = 0.38;
  display.digitDistance   = 0.08;
  display.segmentWidth    = 0.08;
  display.segmentDistance = 0.02;
  display.segmentCount    = 7;
  display.cornerType      = 3;
  display.colorOn         = "#090909";
  display.colorOff        = "#afcbaf";
  display.draw();
  
  var val = '' + value;
  display.setValue(val);
}

//function which takes 1 argument(temp: # of temperature) posts '+' value 
//in uberdusts's 'stemp' resource and calls showSSD() function to show
function incValue(temp)
{
  var c=++temp;
  if(c>30) c=30;//if temp increased over 30, set it 30 (TEMP ranges between 17 and 30)
  $.post( "http://uberdust.cti.gr/rest/testbed/5/node/urn:pspace:0x466/capability/urn:node:capability:stemp/+/",0);
  //alert(c);
  showSSD(c);
  
  return c;
}
//function which takes 1 argument(temp: # of temperature) posts '-' value 
//in uberdusts's 'stemp' resource and calls showSSD() function to show
function decValue(temp)
{
  var c=--temp;
  if(c<17) c=17;//if temp decreased under 17, set it 17 (TEMP ranges between 17 and 30)
  $.post( "http://uberdust.cti.gr/rest/testbed/5/node/urn:pspace:0x466/capability/urn:node:capability:stemp/-/",0);
  //alert(c);
  showSSD(c);
  
  return c;
}