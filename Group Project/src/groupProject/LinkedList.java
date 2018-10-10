package groupProject;
import java.util.*;

public class LinkedList{


	
	private Actvities head;
	
	
	public LinkedList()
	{
		head = null;
	}
	
	public void add(Actvities activity)
	{
		if(head == null)
		{
			head = activity;
			head.next = null;
		}
		else
		{
			Actvities current = new Actvities();
			current = head;
			while(current.next != null)
			{
				current = current.next;
			}
			current.next = activity;
			activity.next = null;	
		}

	}
	
	public void delete()
	{
		head = null;
	}
	
	public int durationTotal()//gets sum of path duration
	{
		int sum = 0;
		Actvities current = head;
		
		while(current != null)
		{
			sum += current.getDuration();
			current = current.next;
		}
		
		return sum;
	}
	
	public String pathtoString()
	{
		Actvities current = head;
		//sort then make String
		//intSort();
		
		String output = "";
		
		while(current != null)
		{
			output += current.getName() + ", " + current.getDuration()+ ", (" + current.dependencyString(current.getDependency()) + ")" + "\n";
			
			current = current.next;
		}
		
		return output;
	}
	
	public boolean exists(String name)
	{
		boolean exists = false;
		
		Actvities current = head;
		while(current != null)
		{
			if(current.getName().equals(name))
				return true;
			current = current.next;
		}
		return exists;
	}
	
	public boolean dependencyExists(String name)
	{
		boolean exists = false;
		
		Actvities current = head;
		while(current != null)
		{
			if(current.getDependency().equals(name))
				return true;
			current = current.next;
		}
		return exists;
	}
	
	public boolean multiDepenExist(String[] deps)
	{
		boolean exists = true;
		
		for(int i=0; i < deps.length; i++)
		{
			if(!exists(deps[i]))
			{
				exists = false;
			}
		}
	
		return exists;
	}
	
	public boolean hasLoop()
	{
		ArrayList prevNodes = new ArrayList();
		Actvities current = head;
		while (current!=null)
		{
			if (!prevNodes.contains(current.next))
			{
				prevNodes.add(current);
				current = current.next;
			}
			else
			{
				return true;
			}
		}
		return false;	
	}
	
	public void removeNode(String name)
	{
		Actvities current = head;
		
		while(current!=null)
		{
			if(current.getName().equals(name))
			{
				current = null;
				return;
			}
			current = current.next;
		}
	}
	
	public Actvities findByName(String name)
	{
		Actvities current = head;
		while(current!=null)
		{
			if(current.getName().equals(name))
				return current;
			current = current.next;
		}
		return null;
	}
	public boolean isGhost()
	{
		if(head.isGhost())
		{
			return true;
		}
		return false;
	}
	
	//public LinkedList combine(LinkedList a,LinkedList b,String parent,String child)
	public void combine(LinkedList a,LinkedList b,String parent,String child)
	{
		/*
		LinkedList result = new LinkedList();
		Actvities par = a.findByName(parent);
		Actvities chil = b.findByName(child);
		
		Actvities current=a.head;
		while(current!=par )
		{
			result.add(current);
			current=current.next;
		}
		result.add(current);
		
		current=b.head;
		while(current!=null)
		{
			result.add(current);
			current=current.next;
		}
		
		return result;
		*/
		Actvities par = a.findByName(parent);
		Actvities chil = b.findByName(child);
		par.next=chil;
		
	}
	
	public void updateHead(String name, int length, String[] depends)
	{
		Actvities newHead = new Actvities();
		Actvities newAct = new Actvities(name,length,depends);
		newHead=newAct;
		
		Actvities Hnext=head.next;
		head=newHead;
		head.next=Hnext;
		return;
	}

}
