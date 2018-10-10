package groupProject;

import java.util.ArrayList;

public class Actvities {
	
	private String name;
	private int duration;
	public Actvities next;
	private String[] dependency = {"",""};
	private boolean ghostDep;
	
	public Actvities()
	{
		name = "";
		duration = 0;
		//dependency gets initialized up top
		ghostDep = false;
	}
	
	public Actvities(String name1, int durat, String[] depend)
	{
		name = name1;
		duration = durat;
		dependency = depend;
		ghostDep = false;
	}
	
	public Actvities(String name1, int durat, String[] depend, boolean ghost)
	{
		name = name1;
		duration = durat;
		dependency = depend;
		ghostDep = ghost;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getDuration()
	{
		return duration;
	}
	
	public String[] getDependency()
	{
		return dependency;
	} 
	
	public void setName(String name2)
	{
		name = name2;
	}
	
	public void setDuration(int durati)
	{
		duration = durati;
	}
	
	public String dependencyString(String[] dependency)
	{
		if(dependency==null) //ghost node
		{
			return "";
		}
		String output = "";
		for(int i = 0; i < dependency.length; i++)
		{
			output += dependency[i]; 
			if(i != dependency.length-1)
			{
			 output += ",";
			}
		}
		return output;
	}
	
	public boolean isGhost()
	{
		return ghostDep;
	}
	
	public boolean hasChild()
	{
		if(this.next!=null)
		{
			return true;
		}
		return false;
	}
}
