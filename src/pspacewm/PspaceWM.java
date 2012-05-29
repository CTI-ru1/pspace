/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pspacewm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Konstantinos
 */
public class PspaceWM {

    /**
     * @param args the command line arguments
     */
    private String node;
    Map<String, Boolean> capability; 

    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    public PspaceWM(String node,String capability,Boolean activated)
    {
        this.capability=new HashMap<String, Boolean>();   

        set_node(node);
        set_capability(capability,activated);
    }
    
    public void set_node(String node)
    {
        this.node=node;
    }
    public void set_capability(String capability,Boolean enabled)
    {
        this.capability.put(capability,enabled);
    }

    public String get_node()
    {
        return node;
    }
    public Map<String, Boolean> get_capability()
    {
        return capability;
    }

}
