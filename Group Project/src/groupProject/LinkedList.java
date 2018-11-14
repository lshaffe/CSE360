package cse360;
import java.util.*;


public class LinkedList{
	private class Node{
		Node prev;
		Activities data;
		Node next;
		
		Node(){
			prev=null;
			data=null;
			next=null;
		}
		boolean hasNext() {
			if(this.next==null)
				return false;
			else
				return true;
		}
		
		
	};

	private Node head;	
	private Node tail;
	
	public LinkedList(){
		head = null;
		tail=null;
	}
	
	public void add(Activities activity){
		if(head == null){
			head = new Node();
			head.data = activity;
			head.next = null;
		}
		else{
			Node current = head;
			while(current.hasNext()) {
				current=current.next;
			}
			
			//current is now the tail of the linked list
			Node newNode = new Node();
			newNode.data=activity;
			newNode.prev=current;
			newNode.next=null;
			tail=newNode;
			
			current.next=newNode;
		}

	}
	
	public void add(Node node){
		if(head == null){
			head = node;
			head.prev=null;
		}
		else{
			Node current = head;
			while(current.hasNext()) {
				current=current.next;
			}
			
			//current is now the tail of the linked list
			Node newNode = new Node();
			newNode=node;
			newNode.prev=current;
			newNode.next=null;
			
			current.next=newNode;
		}

	}
	
	//gets sum of path duration
	public int getTotalDuration(){
		int sum = 0;
		Node current = head;
		
		while(current != null){
			sum += current.data.getDuration();
			current = current.next;
		}
		return sum;
	}
	
	public String pathtoString(){
		Node current = head;
		String output = "";
		
		while(current != null){
			output += current.data.getName() + ", " + current.data.getDuration()+ ", (" + current.data.getDependencyString() + ")" + "\n";
			current = current.next;
		}
		
		return output;
	}

	
	//this function loops through each LinkedList (unique path) and determines if duplicate entries exist in a single path
	public boolean hasLoop(){
		Node current=head;
		ArrayList<String> foundNodeNames=new ArrayList<String>();
		while(current!=null) {
			//check if the current node is already present in our path
			if(foundNodeNames.contains(current.data.getName())) {
				return true;
			}
			else {
				foundNodeNames.add(current.data.getName());
			}
		}
		
		//default case
		return false;
	}
	
	public LinkedList copy() {
		LinkedList copy = new LinkedList();
		copy.head = new Node();
		
		Node current=this.head;
		copy.head.data=current.data;
		current=current.next;
		
		while(current!=null) {
			copy.add(current.data);
			current=current.next;
		}
		return copy;
	}
	
	public ArrayList<LinkedList> createPathArray(ArrayList<LinkedList> pathArray, Activities head,LinkedList newPath) {
		//add the given head pointer to the end of the given LinkedList pointer regardless of # of children
		Activities current=head;
		if(current.getChildren().size()==0) {
			newPath.add(head);
			if(!pathArray.contains(newPath)) {
				pathArray.add(newPath);
			}
			return pathArray;
		}
		//for each child of the current node
		newPath.add(head);
		for(int i=0;i<current.getChildren().size();i++) {
			
			//set reference variables for each loop iteration
			Activities currentChild = current.getChildren().get(i);
			int childCount = current.getChildren().size();
			
			//base case
			if(childCount<1){
				newPath.add(head);
				if(!pathArray.contains(newPath)) {
					pathArray.add(newPath);
				}
				return pathArray;
			}
			//non splitting recursion
			else if(childCount==1) {
				pathArray=createPathArray(pathArray,currentChild,newPath);
				return pathArray;
			}
			else if(childCount>1) {
				ArrayList<LinkedList> branchingPaths = new ArrayList<LinkedList>();
				LinkedList childPath = newPath.copy();
					branchingPaths=createPathArray(branchingPaths,currentChild,childPath);
					pathArray.addAll(branchingPaths);
				//return pathArray;
			}
		}
		
		return pathArray;
	}
}


