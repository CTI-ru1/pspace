package com.pspace.foursquare_card;

import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.communication.websocket.readings.WSReadingsClient;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Veroutis
 *
 */

public class Service implements Observer
{
    static String db_url="url";
    String db_user="user";
    String db_password="pass";
    String db_database="db";
    
    public static void main( String[] args )
    {

        Service s;
        if(args.length==3)
        {
            s=new Service(args[0],args[1],args[2]); 
            //db_url=args[3]; 
        }
        else
            System.out.println("Missing Arguments");
    }
    
    private Service(String hostname,String n,String c)
    {
        String node="urn:pspace:"+n;
        String capability="urn:node:capability:"+c;
        WSReadingsClient.getInstance().setServerUrl("ws://"+hostname+"/uberdust/readings.ws");
        WSReadingsClient.getInstance().subscribe(node, capability);
        WSReadingsClient.getInstance().addObserver(this);
        
        System.out.println("["+hostname+"]Checkin Service...Subscribed to capability["+capability +"] at node["+node+"]");
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
                if(reading.hasStringReading())//exception
                {
                    check_in(reading.getStringReading());                    //responses are queued , no need for thread.
                    System.out.println(reading.getStringReading());
                }
                else if(reading.hasDoubleReading())
                {
                    check_in("berigo");            
                    System.out.println(reading.getDoubleReading());

                }
        }

    }
    private void check_in(String pspace_username)
    {
        Database db=new Database(db_url,db_database,db_user,db_password);
        db.connect();
        
        String token=db.get_token(pspace_username);
        if(token == null)
            System.out.println("Log: user:\""+pspace_username+"\" hasn't connected 4square with pspace");
        else
        {
            String venue=db.get_venue_id();
            try {
                foursquare_request(token,pspace_username,venue);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        db.disconnect();                 
    }
    
    private void foursquare_request(String token,String user,String venue_id) throws MalformedURLException, IOException
    {
        String url="https://api.foursquare.com/v2/checkins/add";
        PostMethod pm=new PostMethod(url);
        
        pm.addParameter("oauth_token",token);
        pm.addParameter("venueId",venue_id);
        pm.addParameter("broadcast","public");
        //pm.addParameter("shout","Used RF Card to Enter CEID"); //Comment along with checkin
        
        HttpClient hc=new HttpClient();

        hc.executeMethod(pm);

        
        if(pm.getStatusCode()==200)
            System.out.println("Log: successfull checkin for user: \""+user+"\"");
        else if(pm.getStatusCode()==401) //maybe set token to null so that the web page doesn't check again for token validity
            System.out.println("Log: expired or erroneous token for user \""+user+"\"");
        else
            System.out.println("Log: communication error when user \""+user+"\" used the card");
   
    }
  
}
