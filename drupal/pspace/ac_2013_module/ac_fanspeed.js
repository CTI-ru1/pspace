/*
* ac_fanspeed.js - Toyotomi HVAC Remote Control GUI
*
* Created by: Papadimitriou K. Georgios, Skartsilas K. Nikolaos, July 15, 2013
* Department of Computer Engineering and Informatics
* University of Patras, Greece
*
* Release into the public domain.
*/

//function which takes 2 arguments (id1: mode id, id2: fan id) 
//and calls function callFan() only in modes (COOL, HEAT, FAN)
function ACfanSpeed(id1,id2)
{
  if(id1==1.0)//cool
  {
   callFan(id2);
  }
  else if(id1==3.0)//heat
  {
  callFan(id2);
  }
  else if(id1==4.0)//fan
  {
  callFan(id2);
  }
}

//function which posts respective value in uberdusts's 'fan' resource and shows the respective arrow indicator
function callFan(id2)
{
   if(id2==4.0)//high
   {
   $.post( "http://uberdust.cti.gr/rest/testbed/5/node/urn:pspace:0x466/capability/urn:node:capability:fan/4/",0);
    document.getElementById("high").style.visibility='';
    document.getElementById("med").style.visibility='hidden';
    document.getElementById("low").style.visibility='hidden';
   }
   else if(id2==3.0)//med
   {
   $.post( "http://uberdust.cti.gr/rest/testbed/5/node/urn:pspace:0x466/capability/urn:node:capability:fan/3/",0);
    document.getElementById("high").style.visibility='hidden';
    document.getElementById("med").style.visibility='';
    document.getElementById("low").style.visibility='hidden';
   }
   else if(id2==2.0)//low
   {
   $.post( "http://uberdust.cti.gr/rest/testbed/5/node/urn:pspace:0x466/capability/urn:node:capability:fan/2/",0);
    document.getElementById("high").style.visibility='hidden';
    document.getElementById("med").style.visibility='hidden';
    document.getElementById("low").style.visibility='';
   }
   else//none
   { 
   $.post( "http://uberdust.cti.gr/rest/testbed/5/node/urn:pspace:0x466/capability/urn:node:capability:fan/0/",0);
    document.getElementById("high").style.visibility='hidden';
    document.getElementById("med").style.visibility='hidden';
    document.getElementById("low").style.visibility='hidden';
   
   }
   
   return id2;
}

//function which implements FAN Button operation (NONE-LOW-MED-HIGH)
//Each time the buttonFan is called, the fan speed mode is shifted
function buttonFan(id1,id2)
{
  
 if(id1==1.0 || id1==3.0 || id1==4.0)
 {
   switch (id2)
   {
   case 4.0://high
      id2=callFan(0.0);
	  break;
   case 3.0://med
      id2=callFan(4.0);
	  break;
   case 2.0://low
      id2=callFan(3.0);
	  break;
   case 0.0://none
      id2=callFan(2.0);
	  break;
   }
 }
   return id2;
}