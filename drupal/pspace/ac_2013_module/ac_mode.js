/*
* ac_mode.js - Toyotomi HVAC Remote Control GUI
*
* Created by: Papadimitriou K. Georgios, Skartsilas K. Nikolaos, July 15, 2013
* Department of Computer Engineering and Informatics
* University of Patras, Greece
*
* Release into the public domain.
*/


//function which takes 1 argument(id1:mode id), posts respective value 
//in uberdusts's 'mode' resource and shows the respective arrow indicator
function ACMode(id1)
{
 
 if(id1==0.0) //auto
 {
   $.post( "http://uberdust.cti.gr/rest/testbed/5/node/urn:pspace:0x466/capability/urn:node:capability:mode/0/",0);
   document.getElementById("auto").style.visibility='';
   document.getElementById("cool").style.visibility='hidden';
   document.getElementById("dry").style.visibility='hidden';
   document.getElementById("heat").style.visibility='hidden';
   
   document.getElementById("fan").style.visibility='hidden';
   document.getElementById("high").style.visibility='hidden';
   document.getElementById("med").style.visibility='hidden';
   document.getElementById("low").style.visibility='hidden';
 }
 else if(id1==1.0) //cool
 {
    $.post( "http://uberdust.cti.gr/rest/testbed/5/node/urn:pspace:0x466/capability/urn:node:capability:mode/1/",0);
   document.getElementById("auto").style.visibility='hidden';
   document.getElementById("cool").style.visibility='';
   document.getElementById("dry").style.visibility='hidden';
   document.getElementById("heat").style.visibility='hidden';
   
   document.getElementById("fan").style.visibility='hidden';
   
 }
 else if(id1==2.0) //dry
 {
   $.post( "http://uberdust.cti.gr/rest/testbed/5/node/urn:pspace:0x466/capability/urn:node:capability:mode/2/",0);
   document.getElementById("auto").style.visibility='hidden';
   document.getElementById("cool").style.visibility='hidden';
   document.getElementById("dry").style.visibility='';
   document.getElementById("heat").style.visibility='hidden';
   
   document.getElementById("fan").style.visibility='hidden';
   document.getElementById("high").style.visibility='hidden';
   document.getElementById("med").style.visibility='hidden';
   document.getElementById("low").style.visibility='hidden';
  }
  else if(id1==3.0) //heat
  {
   $.post( "http://uberdust.cti.gr/rest/testbed/5/node/urn:pspace:0x466/capability/urn:node:capability:mode/3/",0);
   document.getElementById("auto").style.visibility='hidden';
   document.getElementById("cool").style.visibility='hidden';
   document.getElementById("dry").style.visibility='hidden';
   document.getElementById("heat").style.visibility='';
   
   document.getElementById("fan").style.visibility='hidden';
   
  }
  else if(id1==4.0) //fan
  {
    $.post( "http://uberdust.cti.gr/rest/testbed/5/node/urn:pspace:0x466/capability/urn:node:capability:mode/4/",0);
   document.getElementById("auto").style.visibility='hidden';
   document.getElementById("cool").style.visibility='hidden';
   document.getElementById("dry").style.visibility='hidden';
   document.getElementById("heat").style.visibility='hidden';
   
   document.getElementById("fan").style.visibility='';
  }
  
  return id1;

}
//function which takes 1 argument(cond:current state of AC), posts respective value 
//in uberdusts's 'on' resource and shows the respective on-off indicator
function AConoff(cond)
{
 if(cond==1.0)
 {
   document.getElementById("onoff").style.visibility='';
   cond=0.0;
   $.post( "http://uberdust.cti.gr/rest/testbed/5/node/urn:pspace:0x466/capability/urn:node:capability:on/0/",0);
 }
 else if(cond==0.0)
 {
  document.getElementById("onoff").style.visibility='hidden';
   cond=1.0;
   $.post( "http://uberdust.cti.gr/rest/testbed/5/node/urn:pspace:0x466/capability/urn:node:capability:on/1/",0);
   
 }

return cond;
}

//function which takes 3 arguments(on:current state of AC, mode:current mode of AC, fan: current fan speed of AC), 
//and shows on-off,mode and fan indicators
function ACinit(on,mode,fan)
{
  var id1;
  if(on==1.0)//if ac is open
  {
     document.getElementById("onoff").style.visibility='';
     
	id1=ACMode(mode);
	 
	 ACfanSpeed(id1,fan);
      
  }

}
//function which implements MODE Button operation (AUTO-COOL-DRY-HEAT-FAN)
//Each time the buttonMode is called, the mode is shifted
function buttonMode(id)
{
   var x;
   switch (id)
   {
   case 0.0://auto
      x=ACMode(1.0);
	  break;
   case 1.0://cool
      x=ACMode(2.0);
	  break;
   case 2.0://dry
      x=ACMode(3.0);
	  break;
   case 3.0://heat
      x=ACMode(4.0);
	  break;
   case 4.0://fan
      x=ACMode(0.0);
	  break;
   }
   
   return x;
}

//function which alerts when a non-supported button is pressed
function notSupported()
{
alert('Not supported yet.');
}
