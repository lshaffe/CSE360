package groupProject;
public class Actvities {
	
	private String name;
	private int duration;
	private String[] dependency;
	
	public Actvities(String name1, int durat, String[] depend)
	{
		name = name1;
		duration = durat;
		dependency = depend;
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
	
	
}