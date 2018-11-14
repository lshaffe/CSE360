package cse360;
import java.util.Comparator;

public class sortByLength implements Comparator<LinkedList>
{
	public int compare(LinkedList a, LinkedList b)
	{
		return (b.getTotalDuration() - a.getTotalDuration());
	}
}
