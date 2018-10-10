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
	
	public int durationTotal()//gets sum of path duration
	{
		int sum = 0;
		Node current = head;
		
		while(current != null)
		{
			sum += current.act.getDuration();
			current = current.next;
		}
		
		return sum;
	}
	
	public String pathtoString()
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
	
	public boolean dependencyExists(String name)
	{
		boolean exists = false;
		
		Node current = head;
		while(current != null)
		{
			if(current.act.getDependency().equals(name))
				return true;
			current = current.next;
		}
		return exists;
	}
	
	public boolean hasLoop()
	{
		ArrayList prevNodes = new ArrayList();
		Node current = head;
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
		Node current = head;
		
		while(current!=null)
		{
			if(current.act.getName().equals(name))
			{
				current = null;
				return;
			}
			current = current.next;
		}
	}
	
	public Node findByName(String name)
	{
		Node current = head;
		while(current!=null)
		{
			if(current.act.getName().equals(name))
				return current;
			current = current.next;
		}
		return null;
	}
	public boolean isGhost()
	{
		if(head.act.isGhost())
		{
			return true;
		}
		return false;
	}
	
	public LinkedList combine(LinkedList a,LinkedList b,String parent,String child)
	{
		LinkedList result = new LinkedList();
		Node par = a.findByName(parent);
		Node chil = b.findByName(child);
		
		Node current=a.head;
		while(current!=par  && current.next!=null)
		{
			result.add(current.act);
			current=current.next;
		}
		result.add(current.act);
		
		current=b.head;
		while(current!=null)
		{
			result.add(current.act);
			current=current.next;
		}
		
		return result;
	}
	
	public void updateHead(String name, int length, String[] depends)
	{
		Node newHead = new Node();
		Actvities newAct = new Actvities(name,length,depends);
		newHead.act=newAct;
		
		Node Hnext=head.next;
		head=newHead;
		head.next=Hnext;
		return;
	}

}
