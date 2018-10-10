package groupProject;
import java.util.*;

public class LinkedList{

	private class Node
	{
		public Node next; 
		public Actvities act;
	}
	
	private Node head;
	
	
	public LinkedList()
	{
		head = null;
	}
	
	public void add(Actvities activity)
	{
		if(head == null)
		{
			Node first = new Node();
			first.act = activity;
			first.next = null;
			head = first;
		}
		else
		{
			Node current = new Node();
			current = head;
			while(current.next != null)
			{
				current = current.next;
			}
			Node activ = new Node();
			activ.act = activity;
			current.next = activ;
			activ.next = null;	
		}

	}
	
	public void delete()
	{
		head = null;
	}
	

	public void intSort()
	{
		//sort by duration GREATEST TO LEAST
		Node i,j,temp;
		
		if(head.next !=null)
		{
			for(i = head; i.next != null; i = i.next)
			{
				for(j = i.next; j != null; j = j.next)
				{
					if(i.act.getDuration() < j.act.getDuration())
					{
						temp = i;
						i = j;
						j = temp;//same as i.next = temp
					}
				}
			}
		}
	}

	
	public String toString()
	{
		Node current = head;
		//sort then make String
		//intSort();
		
		String output = "";
		
		while(current != null)
		{
			output += current.act.getName() + ", " + current.act.getDuration()+ ", (" + current.act.dependencyString(current.act.getDependency()) + ")" + "\n";
			
			current = current.next;
		}
		
		return output;
	}
	
	public boolean exists(String name)
	{
		boolean exists = false;
		
		Node current = head;
		while(current != null)
		{
			if(current.act.getName().equals(name))
				return true;
			current = current.next;
		}
		return exists;
	}
	
	
}
