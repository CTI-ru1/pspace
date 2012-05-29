package pspacewm;

import com.google.gson.stream.JsonReader;
import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.communication.websocket.readings.WSReadingsClient;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import net.sf.libgrowl.Application;
import net.sf.libgrowl.GrowlConnector;
import net.sf.libgrowl.Notification;
import net.sf.libgrowl.NotificationType;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.xml.sax.SAXException;


/**
 *
 * @author Konstantinos V.
 */
public class pspace_interface extends javax.swing.JFrame implements Observer {

    /**
     * Creates new form pspace_interface
     */
    String hostname="pspace.dyndns.org:8080";
    String capability_namespace="urn:node:capability:";
    String[] discard_capabilities={
        capability_namespace+"x",
        capability_namespace+"y",
        capability_namespace+"height",
        capability_namespace+"width",
        capability_namespace+"report"
    };
    String button_main="urn:pspace:door";
    String hifi_main="urn:pspace:hifi";
    String[] track;   
    String[] people=new String[15]; //last entries

    Map<String, PspaceWM> notif; 
    Map<String,Integer> button_data;
    DefaultPieDataset dpd;
    DefaultValueDataset data;
    XYDataset temperature_data;
    XYSeries s1;
    GrowlConnector gc=new GrowlConnector("localhost");
        
    Application ap=new Application("P-Space","");

    NotificationType send_notification = new NotificationType("Node Notification Type");
    NotificationType[] notificationTypes = new NotificationType[] { send_notification};
    
    private String temperature_main="urn:pspace:test";
    
    public class twitter_update extends Thread {
         public void run() {
            progress_text.setVisible(true);
            progress_text.setText("Fetching twitter data account:"+twitter_account.getText().toString());
            initialize_twitter();   
         
         }
     }
     
    public class hifi_update extends Thread {
         public void run() {
            progress_text.setVisible(true);
            progress_text.setText("Getting latest tracks:");
            initialize_hifi();   
         
         }
     }
     public class save_xml extends Thread {
         public void run() {

        XMLSave xml=new XMLSave();
            try {
                xml.xml_write(notif);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(pspace_interface.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerConfigurationException ex) {
                Logger.getLogger(pspace_interface.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerException ex) {
                Logger.getLogger(pspace_interface.class.getName()).log(Level.SEVERE, null, ex);
            }
         
         }
     }
    public class test extends Thread {
    private String refresh;
    private test(String refresh) {
            this.refresh=refresh;
        }
        @Override
    public void run() {

         if(refresh.equals("refresh"))
         {
             
             door_statistics.removeAll();
             temp_panel.removeAll();
             temp_plot.removeAll();
         }
         System.out.println("Populating Interface started");
         progress_text.setVisible(true);
         progress.setVisible(true);
         progress.setValue(0);
        if(!refresh.equals("refresh"))
        {
                    progress_text.setText("Initializing Socket");
                    initialize();
                    progress.setValue(1);
                    progress_text.setText("Fetching Uberdust Nodes");
                    initialize_nodes();
                    progress.setValue(2);
        }

        progress_text.setText("Calculating Door Statistics");
        initialize_button();
        progress.setValue(3);


        progress_text.setText("Getting temperature Data"); 
        initialize_temperature();
        progress.setValue(4);
        
        progress_text.setText("Fetching latest tweets");
        initialize_twitter();   
        progress.setValue(5);
        
        progress_text.setText("Getting latest tracks");
        initialize_hifi();   
        progress.setValue(6);        
       
        progress_text.setVisible(false);
        progress.setVisible(false);
        
        button_list.repaint();
        validate();

     }
    }
    
    public pspace_interface() {
        initComponents();

        System.out.println("Interface Created");
        Thread thread = new test("");
        thread.start();

    }
    
    public void initialize()
    {
   
        /////TRAY/////
        /*SystemTray tray=SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("icon.png");
       PopupMenu pp = new PopupMenu();
        MenuItem defaultItem = new MenuItem("Exit");

        pp.add(defaultItem);
        TrayIcon ti=new TrayIcon(image,"Tray test",pp);
        try {
            tray.add(ti);
        } catch (AWTException ex) {
            Logger.getLogger(pspace_interface.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        //////MOVE TO DIFFERENT INITIALZE FUNCTION//////////////
        WSReadingsClient.getInstance().setServerUrl("ws://"+hostname+"/uberdust/readings.ws");



        WSReadingsClient.getInstance().addObserver(this); 


        
        
    }
    
    public void initialize_hifi()
    {
        int excep=0;
        track=new String[Integer.parseInt(track_num.getText())];
        try {
            URL status = new URL("http://" + hostname + "/uberdust/rest/testbed/2/node/"+hifi_main+"/capability/urn:node:capability:track/tabdelimited/limit/"+track_num.getText());
            BufferedReader br = new BufferedReader(new InputStreamReader(status.openStream()));
            String line;
            int counter=0;
            while((line=br.readLine())!=null)
            {
              String[] tracks=line.split("\t");
              if(tracks.length>1)
              {
               track[counter]=tracks[1];
               counter++;
              }
            }
        } catch (IOException | NumberFormatException e) {
                        excep=1;
                        progress_text.setText("Error getting latest Tracks...");
        }     
       if(excep==0)
           progress_text.setVisible(false);
        hifi_list.setListData(track);
    }   
    public void initialize_button()
    {
        button_data=new HashMap<>();
        try {
            URL status = new URL("http://" + hostname + "/uberdust/rest/testbed/2/node/"+button_main+"/capability/urn:node:capability:card/tabdelimited/limit/");
            BufferedReader br = new BufferedReader(new InputStreamReader(status.openStream()));
            String line;

            int last_readings=people.length;
            long last_date;
            while((line=br.readLine())!=null)
            {
              String name=line.split("\t")[1];
              
              if(button_data.containsKey(name))
              {
                  int temp=button_data.get(name);
                  button_data.put(name,++temp);
              }
              else
                  button_data.put(name,1);
           
              if(last_readings!=0)
              {
                  Date time=new Date((long)Long.parseLong(line.split("\t")[0]));
                  people[people.length-last_readings]=time.toLocaleString()+"   :   "+name;
                  last_readings--;
              }

              //  System.out.println(line.split("\t")[1]);
            }
        } catch (IOException | NumberFormatException e) {
        }
        
        door_dataset(button_data);
        PieDataset pd=dpd;
        JFreeChart button_chart=ChartFactory.createPieChart("Door Statistics Since The Beginning of Time",pd , false, false, false);
        door_statistics.add(new ChartPanel(button_chart));
        button_list.setListData(people);
    }
    
    public  void door_dataset(Map<String, Integer> set)
    {
         dpd=new DefaultPieDataset();
         for(Map.Entry<String, Integer> element : set.entrySet())
         {
             if(!element.getKey().equals("button"))
             dpd.setValue(element.getKey(), element.getValue());
         }

         
    }
    public void push_array(String value,String[] topush)
    {
        for(int i=topush.length-1;i>0;i--)
          topush[i]=topush[i-1];  

        topush[0]=value;
    }
     public void initialize_temperature()
    {
       ////Thermometer Statistics Plot/// 

        
        
         s1=new XYSeries("Temperature Values");
         float latest_reading = 0;
        try {
            URL status = new URL("http://" + hostname + "/uberdust/rest/testbed/2/node/urn:pspace:test/capability/urn:node:capability:temperature/tabdelimited/limit/40");
            BufferedReader br = new BufferedReader(new InputStreamReader(status.openStream()));
            String line;
            Day d1=new Day();
            int c=1;
            while((line=br.readLine())!=null)
            {
                s1.add(c,Float.parseFloat(line.split("\t")[1]));
                c++;
            }
            if(s1.getItemCount()>0)
            latest_reading=s1.getY(s1.getItemCount()-1).floatValue();
            System.out.println(latest_reading);
        } catch (IOException | NumberFormatException e) {
        }
        
        
        ////Thermometer Plot///
        data=new DefaultValueDataset(latest_reading);
        ThermometerPlot tp=new ThermometerPlot(data);
        tp.setRange(10, 40);
        tp.zoom(100);
        JFreeChart chart_t=new JFreeChart("Θερμοκρασία",tp);
       
        temperature_data=new XYSeriesCollection(s1);
        ValueAxis tade=new NumberAxis("value");
       // XYPlot xy=new XYPlot(temperature_data,,tade,);

        JFreeChart chart_spline=ChartFactory.createXYLineChart("Temperature Chart", "Index", "Temperature", temperature_data, PlotOrientation.VERTICAL, true, true, true);       

        
        temp_panel.add(new ChartPanel(chart_t));
        temp_plot.add(new ChartPanel(chart_spline));
    }
     
    private void initialize_twitter() {
        int failed=0;
        int tweets=Integer.parseInt(tweets_num.getText());
        String[] twitter_flow=new String[tweets];
        String twitter_url="http://api.twitter.com/1/statuses/user_timeline.json?screen_name="+twitter_account.getText().toString()+"&count="+tweets;
        try {
                boolean skip=false;
                BufferedReader br = new BufferedReader(new InputStreamReader(new URL(twitter_url).openStream()));
                JsonReader json=new JsonReader(br);
                json.beginArray();
                int counter=0;
                while(json.hasNext())
                {
                    skip=false;
                    json.beginObject();
                    while(json.hasNext())
                    {
                        if(skip==true)
                        {
                            json.skipValue();
                        }
                        else
                        {
                            if(json.nextName().equals("text"))
                            {
                                
                                twitter_flow[counter]=Integer.toString(counter+1)+" : "+json.nextString();
                                counter++;
                                skip=true;
                            }
                            else
                                json.skipValue();
                        }
                    }
                    json.endObject();
                    
                }
                json.close();

        } catch (Exception ex) {
            progress_text.setVisible(true);
            failed=1;
            progress_text.setText("Error fetching Twitter Feed for account:"+twitter_account.getText().toString());
            Logger.getLogger(pspace_interface.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(failed==0)
            progress_text.setVisible(false);
        tweet_list.setListData(twitter_flow);
    }
    
    private void initialize_nodes() {
        notif = new HashMap<>();      
        //notif.put("urn:pspace:0x123", new PspaceWM("0x123","temperature",true));

        URL nodes;
        try {
            String node;
            nodes = new URL("http://" + hostname + "/uberdust/rest/testbed/2/status/json");
            BufferedReader br = new BufferedReader(new InputStreamReader(nodes.openStream()));
            JsonReader json=new JsonReader(br);
            json.beginObject();
            while(json.hasNext())
            {
                node=json.nextName();
                json.beginArray();
                

                    while(json.hasNext())
                    {
                        json.beginObject();
                        while(json.hasNext())
                        {
                            if(json.nextName().equals("capability"))
                            {
                                String next_capability=json.nextString();
                                if(!Arrays.asList(discard_capabilities).contains(next_capability))
                                {
                                    
                                    if(notif.get(node)!=null)
                                        notif.get(node).set_capability(next_capability,false);
                                    else
                                        notif.put(node,new PspaceWM(node,next_capability,false));  
                                }
                            }
                            else
                                json.skipValue();
                        }
                        json.endObject();
                    }
                json.endArray();
            }
            json.endObject();
            json.close();

        } catch (Exception ex) {
            Logger.getLogger(pspace_interface.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       XMLSave xmlread=new XMLSave();
            try {
                xmlread.xml_read(notif);
            } catch (    ParserConfigurationException | SAXException | IOException ex) {
                Logger.getLogger(pspace_interface.class.getName()).log(Level.SEVERE, null, ex);
        }
        //notifications.getContentPane().setLayout(new java.awt.GridLayout(notif.size()*2, 1));

        node_drop = new JComboBox();

        
                node_drop.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                node_selected(evt);
            }
        });
        notification_dropdown.removeAll();
        notification_dropdown.add(node_drop);
        for(final Map.Entry<String,PspaceWM> element:notif.entrySet())
        {
            node_drop.addItem(element.getValue().get_node());
            for(Map.Entry<String, Boolean> element2 : element.getValue().get_capability().entrySet())
            {
                if(element2.getValue())
                              WSReadingsClient.getInstance().subscribe(element.getValue().get_node(), element2.getKey());   
            }
        }
}
    
    
    private void notification_changed(ItemEvent evt,String id) {
        boolean value = false;
        if(evt.getStateChange()==1)
        {
             WSReadingsClient.getInstance().subscribe(node_drop.getSelectedItem().toString(), id);
             value=true;
        }
        else if(evt.getStateChange()==2)
            value=false;
        
        Thread thread = new save_xml();
        thread.start();  
        
        notif.get(node_drop.getSelectedItem().toString()).set_capability(id, value);

        
     }
     private void node_selected(ActionEvent event) {
         checkboxes.removeAll();
        checkboxes.validate();
        for(Map.Entry<String,Boolean> element:notif.get(node_drop.getSelectedItem().toString()).get_capability().entrySet())
        {
                                  final String temp=element.getKey();
                                  boolean checked=element.getValue();

                                  JCheckBox jc=new JCheckBox(temp);
                                  jc.setSelected(checked);
                                    jc.addItemListener(new java.awt.event.ItemListener() {
                @Override
                                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                                    notification_changed(evt,temp);
                                    }
                                    });
                                  checkboxes.add(jc);
                                  
                           }
        checkboxes.validate();
        checkboxes.repaint();


        
     }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        notifications = new javax.swing.JDialog();
        notification_dropdown = new javax.swing.JPanel();
        checkboxes = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        about = new javax.swing.JDialog();
        jLabel6 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        temp_panel = new javax.swing.JPanel();
        temp_plot = new javax.swing.JPanel();
        tab33 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        button_list = new javax.swing.JList();
        door_statistics = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tweet_list = new javax.swing.JList();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        twitter_account = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        tweets_num = new javax.swing.JTextField();
        progress = new javax.swing.JProgressBar();
        progress_text = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        hifi_list = new javax.swing.JList();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        track_num = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        notifications.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        notifications.setTitle("Configure Notifications");
        notifications.setMinimumSize(new java.awt.Dimension(400, 350));
        notifications.setResizable(false);
        notifications.getContentPane().setLayout(new javax.swing.BoxLayout(notifications.getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        notification_dropdown.setMinimumSize(new java.awt.Dimension(0, 0));
        notifications.getContentPane().add(notification_dropdown);

        checkboxes.setBorder(javax.swing.BorderFactory.createTitledBorder("Capabilities"));
        checkboxes.setLayout(new java.awt.GridLayout(10, 1));

        jLabel1.setText("Layout testing");
        checkboxes.add(jLabel1);

        notifications.getContentPane().add(checkboxes);

        about.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        about.setTitle("About");
        about.setMinimumSize(new java.awt.Dimension(320, 240));
        about.setResizable(false);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pspacewm/pspace.png"))); // NOI18N

        javax.swing.GroupLayout aboutLayout = new javax.swing.GroupLayout(about.getContentPane());
        about.getContentPane().setLayout(aboutLayout);
        aboutLayout.setHorizontalGroup(
            aboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
        );
        aboutLayout.setVerticalGroup(
            aboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("P-Space Interface");
        setIconImage(new ImageIcon(this.getClass().getResource("icon.png")).getImage());
        setPreferredSize(new java.awt.Dimension(1170, 700));

        jTabbedPane1.setMaximumSize(new java.awt.Dimension(100, 32767));

        temp_panel.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("RoomTemperature", temp_panel);

        temp_plot.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("Temperature Plot", temp_plot);

        button_list.setBackground(new java.awt.Color(240, 240, 240));
        jScrollPane2.setViewportView(button_list);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tab33.addTab("Door", jPanel4);

        door_statistics.setLayout(new java.awt.BorderLayout());
        tab33.addTab("Door Statistics", door_statistics);

        jPanel2.setBackground(new java.awt.Color(235, 235, 235));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Settings"));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 565, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 246, Short.MAX_VALUE))
        );

        tab33.addTab("Settings", jPanel1);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pspacewm/twitter.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        tweet_list.setToolTipText("");
        jScrollPane1.setViewportView(tweet_list);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane2.addTab("Twitter Feed", jPanel5);

        jLabel3.setText("Twitter Account Screen Name:");

        twitter_account.setText("patrasspace");

        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setText("Number of feeds");

        tweets_num.setText("10");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(twitter_account, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                    .addComponent(tweets_num))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(twitter_account, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tweets_num, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addContainerGap(67, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Settings", jPanel6);

        progress.setForeground(new java.awt.Color(0, 204, 51));
        progress.setMaximum(6);

        jButton1.setText("Refresh");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        hifi_list.setBackground(new java.awt.Color(240, 240, 240));
        jScrollPane3.setViewportView(hifi_list);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("HiFi", jPanel3);

        jLabel5.setText("Number of tracks:");

        track_num.setText("10");

        jButton3.setText("Save");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(track_num, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 279, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(track_num, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addGap(0, 147, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Settings", jPanel7);

        jMenu1.setText("View");

        jMenuItem2.setText("P-Space Website");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("P-Space Control");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Options");

        jMenuItem1.setText("Subscriptions");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Help");

        jMenuItem4.setText("About");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(progress_text))
                    .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tab33, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tab33, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(progress_text, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1))
                .addContainerGap(103, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        notifications.setLocation(this.getX()+this.getWidth()/2-160, this.getY()+this.getHeight()/2-120);
        notifications.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Thread thread = new test("refresh");
        thread.start();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        Thread thread = new twitter_update();
        thread.start();    

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Thread thread = new hifi_update();
        thread.start();   
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        String url = "http://www.p-space.gr";
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (IOException ex) {
            Logger.getLogger(pspace_interface.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
                String url = "http://pspace.dyndns.org/drupal";
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (IOException ex) {
            Logger.getLogger(pspace_interface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        about.setLocation(this.getX()+this.getWidth()/2-160, this.getY()+this.getHeight()/2-120);
        about.setVisible(true);       

    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
                String url = "http://twitter.com/#!/patrasspace";
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (IOException ex) {
            Logger.getLogger(pspace_interface.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }//GEN-LAST:event_jLabel2MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                                    System.out.println(info.getName());

                if ("Metal".equals(info.getName())) {


                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(pspace_interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new pspace_interface().setVisible(true);
            }
        });
    }

    

    @Override
    public void update(Observable o, Object arg) {
        

        
        if (!(o instanceof WSReadingsClient)) {
            return;
        }
        if (!(arg instanceof Message.NodeReadings)) {
            return;
        }
        Message.NodeReadings readings = (Message.NodeReadings) arg;
        for (Message.NodeReadings.Reading reading : readings.getReadingList()) {
                            PspaceWM v1=notif.get(reading.getNode());
                            System.out.println("String reading:"+reading.getStringReading()+" Double Reading:"+reading.hasDoubleReading());

                            /////MAIN PAGE UPDATES REGARDLESS SUBSCRIPTIONS////
                            if(reading.getNode().equals(button_main)&&reading.getCapability().equals(capability_namespace+"card"))
                            {             
                                String person=reading.getStringReading();
                                Date time=new Date(reading.getTimestamp());
                                push_array(time.toLocaleString()+"   :   "+person,people);
                                if(!person.equals("button"))
                                {
                                    if(button_data.containsKey(person))
                                    {
                                        int temp=button_data.get(person);
                                        button_data.put(person,++temp);
                                        dpd.setValue(person, temp);
                                    }
                                    else
                                    {
                                        button_data.put(person,1);                               
                                        dpd.setValue(person, 1);

                                    }
                                }
                            }
                            else if(reading.getNode().equals(temperature_main)&&reading.getCapability().equals(capability_namespace+"temperature"))
                            {
                                data.setValue(reading.getDoubleReading());
                                 s1.add(s1.getItemCount()+1, reading.getDoubleReading());           
                            }
                            else if(reading.getNode().equals(hifi_main)&&reading.getCapability().equals(capability_namespace+"track"))
                            {
                                push_array(reading.getStringReading(),track);
                            }
                            
                            /////SEND NOTIFICATION IF USER HAS REGISTERED////
                            if(v1!=null&&v1.get_capability().get(reading.getCapability()))
                            {
                                String text;
                                if(reading.hasStringReading())
                                   text=reading.getStringReading();
                                else
                                    text=String.valueOf(reading.getDoubleReading());

                                System.out.println("Reading:"+reading.getStringReading());
                                System.out.println("Send notification");
                                gc.register(ap, notificationTypes);
                                String node_id=v1.get_node().split(":")[2];
                                String capability_id=reading.getCapability().split(":")[3];
                                Notification n1 = new Notification(ap, send_notification, "Node:"+node_id+" "+capability_id, text);
                                gc.notify(n1);
                            }
       // data.setValue(reading.getDoubleReading());
        }
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog about;
    private javax.swing.JList button_list;
    private javax.swing.JPanel checkboxes;
    private javax.swing.JPanel door_statistics;
    private javax.swing.JList hifi_list;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JPanel notification_dropdown;
    private javax.swing.JDialog notifications;
    private javax.swing.JProgressBar progress;
    private javax.swing.JLabel progress_text;
    private javax.swing.JTabbedPane tab33;
    private javax.swing.JPanel temp_panel;
    private javax.swing.JPanel temp_plot;
    private javax.swing.JTextField track_num;
    private javax.swing.JList tweet_list;
    private javax.swing.JTextField tweets_num;
    private javax.swing.JTextField twitter_account;
    // End of variables declaration//GEN-END:variables
    private JComboBox node_drop;
}