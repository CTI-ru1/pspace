<?php

//hook
function pspace_nodes_init() {

  drupal_add_js(drupal_get_path('module', 'pspace_nodes') . '/js/highcharts.js');
  drupal_add_js(drupal_get_path('module', 'pspace_nodes') . '/js/highcharts.src.js');
  drupal_add_js(drupal_get_path('module', 'pspace_nodes') . '/js/themes/gray.js');
}
?>

<?php

//Is not used, it's almost working but abandoned..
//saved for future implementation (maybe)

//---charts
//-------javascript functions------//
$maxRows=100;
$js_header="\n<script type=\"text/javascript\">\n";
$js_footer="\n</script>\n";
$charts="";

$charts.="
function displayChart(){
	var chart;
	chart = new Highcharts.Chart({
		chart: {
                    renderTo: 'container',
                    defaultSeriesType: 'spline',
                    zoomType: 'x',
                    spacingRight: 20
                },
                title: {
                    text: 'whateva'  
                },
                subtitle: {
                    text: document.ontouchstart === undefined ?
                            'Click and drag in the plot area to zoom in' :
                            'Drag your cursor over the plot to zoom in'
                },
                xAxis: {
                    type: 'datetime',
                    tickPixelInterval: 150,
                    maxZoom: 1000
                },
                yAxis: {
                    title: {
                        text: 'Reading'
                    },
                    min: 0,
                    startOnTick: false,
                    showFirstLabel: false
                },
                tooltip: {
                    shared: true
                },
                legend: {
                    enabled: false
                },
	      plotOptions: {
		 series: {
		    lineWidth: 1,
		    marker: {
		       enabled: false,
		       states: {
		          hover: {
		             enabled: true,
		             radius: 5
		          }
		       }
		    }
		 }
	      },
               series: [
                  {
                        name: 'zavarakatranemia',
                        data: [";

$data="[\n";

$tabdelimited = file_get_contents("$testbed/node/$node_pref".$node_name."/capability/$cap_pref"."pir"."/tabdelimited/limit/".$maxRows);

$lines = explode("\n", $tabdelimited, $maxRows);
unset($lines[count($lines)-1]);

$firstRow = explode("\t", $lines[0]);
$charts.="                                 [".$firstRow[0]." , ".$firstRow[1]."]";
unset($lines[0]);

foreach ($lines as $thisLine) {
  $row = explode("\t", $thisLine);
  $charts.=" ,\n                                 [".$row[0]." , ".$row[1]."]";
}

$charts.="
			      ]
                  }
               ]
	});
}\n";

$charts.="jQuery(document).ready(function() {displayChart();});";

$rarray['script_charts']= array(
  '#markup' => $js_header.$charts.$js_footer, 
  '#suffix' => "<div id=\"container\"></div>",
 );
///---------------charts

?>
