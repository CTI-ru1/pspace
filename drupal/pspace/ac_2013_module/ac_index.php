<?php
/*
* ac_index.php - Toyotomi HVAC Remote Control GUI
*
* Created by: Papadimitriou K. Georgios, Skartsilas K. Nikolaos, July 15, 2013
* Department of Computer Engineering and Informatics
* University of Patras, Greece
*
* Release into the public domain.
*/

//Add necessary JavaScript files in drupal
drupal_add_js('segment-display.js');
drupal_add_js('ac_displayTemp.js');
drupal_add_js('ac_mode.js');
drupal_add_js('ac_fanspeed.js');

//Get data from uberdust's status page(raw file)
$status_raw=file_get_contents("http://uberdust.cti.gr/rest/testbed/5/status/raw");
$line = explode("\n", $status_raw);

//Initialize php variables as:	
$on_off=0.0; //off
$fan_speed=0.0; //none_sp
$mode=0.0; //auto
$stemp=20.0; 

//For each element of array $line
for($i=0; $i<sizeof($line); $i++)
{
    //Remove tabs in array $nodetype so: 
	//$nodetype[0]=p-space nodes, $nodetype[1]=last's measurement time, $nodetype[3]=last's measurement value 
    $nodetype= explode("\t", $line[$i]);	
	
	//If node 0x466 exists:  
    if(isset($nodetype[0])&& $nodetype[0]=="urn:pspace:0x466" && $nodetype[1]!='report')
	{
           //Set capabilities in respective variables
		   
		   if($nodetype[1]=='urn:node:capability:on') $on_off=$nodetype[3];
		  
		   if($nodetype[1]=='urn:node:capability:fan') $fan_speed=$nodetype[3];
		 
		   if($nodetype[1]=='urn:node:capability:mode') $mode=$nodetype[3];
		   
		   if($nodetype[1]=='urn:node:capability:stemp') $stemp=$nodetype[3];
		  
    }	
     }	
	      echo "<br><br><br><br>";
?>
<!-- Add necessary references to use jquery CDN -->
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.1.0.js"></script>

<!-- Initialize global variables based on status's values -->
<script type="text/javascript">
window.bfan=<?php echo $fan_speed; ?>;
window.bmode=<?php echo $mode; ?>;
window.on=<?php echo $on_off; ?>;
window.temp=<?php echo $stemp; ?>;
</script>

<!-- Mapped image of AC Toyotomi remote controller to map controller's features -  http://www.electrolux-ui.com:8080/2007/220/5509767EN.pdf -->
<div style="text-align:center; width:209px; margin-left:auto; margin-right:auto;">

<!-- The controller's image which onload JS's event loads the graphical segment display for displaying DIGITAL DISPLAY area  -->
<img id="Image-Maps_2201305240514055" src="http://www.image-maps.com/uploaded_files/2201305240514055_ir_controller2.png" usemap="#Image-Maps_2201305240514055" border="0" width="209" height="476" alt=""  onload="showSSD('<?php echo $stemp;?>');" />

<!-- Image Buttons -->
<map id="_Image-Maps_2201305240514055" name="Image-Maps_2201305240514055">
<area shape="rect" coords="44,177,94,195" alt="buttontempdown" id="down" title="buttontempdown" style="cursor:pointer" onclick='temp=decValue(temp);'    />
<area shape="rect" coords="112,176,162,194" alt="buttontempup" id="up" title="buttontempup" style="cursor:pointer" onclick='temp=incValue(temp);' />
<area shape="rect" coords="43,224,69,238" alt="buttonmode" title="buttonmode" style="cursor:pointer" onclick='bmode=buttonMode(bmode);'  />
<area shape="rect" coords="140,225,166,239" alt="buttonfanspeed" title="buttonfanspeed" style="cursor:pointer" onclick='bfan=buttonFan(bmode,bfan);'    />
<area shape="rect" coords="42,260,68,274"  alt="buttonswing" title="buttonswing"  onclick="notSupported();"  />
<area shape="rect" coords="137,259,163,273"  alt="buttontimeron" title="buttontimeron"   onclick="notSupported();" />
<area shape="rect" coords="139,291,165,305"  alt="buttontimeroff" title="buttontimeroff"   onclick="notSupported();" />
<area shape="rect" coords="42,328,62,347"  alt="buttonairdirection" title="buttonairdirection"   onclick="notSupported();" />
<area shape="rect" coords="75,328,95,347"  alt="buttoncleanair" title="buttoncleanair"   onclick="notSupported();" />
<area shape="rect" coords="146,328,166,347"  alt="buttonturbo" title="buttonturbo"   onclick="notSupported();" />
<area shape="rect" coords="109,328,129,347"  alt="buttonleddisplay" title="buttonleddisplay"   onclick="notSupported();" />
<area shape="rect" coords="78,221,128,242" alt="buttononoff" title="buttononoff" style="cursor:pointer" onclick="on=AConoff(on);"  />
<area shape="rect" coords="86,258,123,273"  alt="buttoneconomy" title="buttoneconomy" onclick="notSupported();"   />

<!-- Image Mode-Fan Button (outside DIGITAL DISPLAY area) -->
<area shape="rect" coords="28,76,61,91" alt="modeauto" title="modeauto" style="cursor:pointer" onclick="bmode=ACMode(0.0);"  />
<area shape="rect" coords="28,93,61,109" alt="modecool" title="modecool" style="cursor:pointer" onclick="bmode=ACMode(1.0);"  />
<area shape="rect" coords="27,111,60,126" alt="modedry" title="modedry"  style="cursor:pointer" onclick="bmode=ACMode(2.0);"  />
<area shape="rect" coords="27,128,59,144" alt="modeheat" title="modeheat"  style="cursor:pointer" onclick="bmode=ACMode(3.0);"  />
<area shape="rect" coords="146,76,180,92" alt="fanspeedfan" title="fanspeedfan"  style="cursor:pointer" onclick="bmode=ACMode(4.0);" />
<area shape="rect" coords="146,93,179,110" alt="fanspeedhigh" title="fanspeedhigh"  style="cursor:pointer"  onclick="ACfanSpeed(bmode,4.0);"/>
<area shape="rect" coords="147,109,180,126" alt="fanspeedmedium" title="fanspeedmedium" style="cursor:pointer"  onclick="ACfanSpeed(bmode,3.0);"  />
<area shape="rect" coords="147,127,180,144" alt="fanspeedlow" title="fanspeedlow" style="cursor:pointer"  onclick="ACfanSpeed(bmode,2.0);" />

<area shape="rect" coords="207,474,209,476" href="http://www.image-maps.com/index.php?aff=mapped_users_2201305240514055" alt="Image Map" title="Image Map" />
</map>

<!-- Image on-off Indicator -->
<img id="onoff" src="misc/myicons/on_off1.png" border="0" width="12" height="8" alt="No Image" style="position:absolute;top:300px;left:377px;visibility:hidden"/>

<!-- Image Mode Indicators -->
<img id="auto" src="misc/myicons/north_left_arrow1.png" border="0" width="12" height="8" alt="No Image" style="position:absolute;top:298px;left:317px;visibility:hidden"/>
<img id="cool" src="misc/myicons/left_arrow1.png" border="0" width="12" height="8" alt="No Image" style="position:absolute;top:305px;left:317px;visibility:hidden"/>
<img id="dry" src="misc/myicons/left_arrow1.png" border="0" width="12" height="8" alt="No Image" style="position:absolute;top:320px;left:317px;visibility:hidden"/>
<img id="heat" src="misc/myicons/south_left_arrow1.png" border="0" width="12" height="8" alt="No Image" style="position:absolute;top:330px;left:317px;visibility:hidden"/>
<img id="fan" src="misc/myicons/north_right_arrow1.png" border="0" width="12" height="8" alt="No Image" style="position:absolute;top:298px;left:392px;visibility:hidden"/>

<!-- Image Fan Indicators -->
<img id="high" src="misc/myicons/right_arrow1.png" border="0" width="12" height="8" alt="No Image" style="position:absolute;top:305px;left:392px;visibility:hidden"/>
<img id="med" src="misc/myicons/right_arrow1.png" border="0" width="12" height="8" alt="No Image" style="position:absolute;top:320px;left:392px;visibility:hidden"/>
<img id="low" src="misc/myicons/south_right_arrow1.png" border="0" width="12" height="8" alt="No Image" style="position:absolute;top:330px;left:392px;visibility:hidden"/>

<!-- Image DIGITAL DISPLAY area indicator -->
<canvas id='myDisplay' height="34" width="120" style="position:absolute;top:307px;left:297px;">
  Your browser is unfortunately not supported.
</canvas>
</div>
 
<!-- Initializate Toyotomi HVAC Remote Control GUI -->
<script> 
ACinit(<?php echo $on_off." , ".$mode." , ".$fan_speed;?>);
</script>        