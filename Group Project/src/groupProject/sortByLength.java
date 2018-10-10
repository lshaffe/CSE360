package groupProject;
import java.util.Comparator;

public class sortByLength implements Comparator<LinkedList>
{
	public int compare(LinkedList a, LinkedList b)
	{
		return (b.durationTotal() - a.durationTotal());
	}
}
