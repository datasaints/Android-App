/*
* Not used
* This is the code used for Scan Fragment (which uses our previous mobile reader: Arete Pop
 */

package org.stjude.www.dsassetmanagement.dsassetmanagement;

public class customCell
{
    String name;
    String value;
    
    public customCell()
    {
	
    }
    
    public customCell(String name,String value)
    {
	this.name = name;
	this.value = value;
    }
    
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getValue()
    {
        return value;
    }
    public void setValue(String value)
    {
        this.value = value;
    }
    
    

}
