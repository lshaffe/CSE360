package cse360;

import java.util.ArrayList;

public class Activities {

	private String name;
	private int duration;

	//names necessary because the nodes may be added out of order and we can't be bothered to go around 
	//cleaning up null pointers with every pass
	private ArrayList<String> dependencyNames;
	private ArrayList<Activities> dependencies;

	private ArrayList<Activities> children;

	//Constructor
	public Activities(){;
	dependencyNames = new ArrayList<String>();
	dependencies = new ArrayList<Activities>();

	children = new ArrayList<Activities>();
	}


	//Getters 
	public String getName(){
		return name;
	}

	public int getDuration(){
		return duration;
	}

	public ArrayList<String> getDependencyNames(){
		return dependencyNames;
	} 

	public ArrayList<Activities> getDependecies(){
		return dependencies;
	}

	public Activities getOnlyChild(){
		if(this.children.isEmpty()) {
			return null;
		}
		return children.get(0);
	}

	public ArrayList<Activities> getChildren(){
		return children;
	}

	public void setName(String name2){
		name = name2;
	}

	public void setDuration(int durati){
		duration = durati;
	}

	public void setTentDeps(String depNames) {
		if(depNames.isEmpty()) {
			return;
		}
		if(depNames.contains("/")) {

			String[] tokens=depNames.split("/");
			for(int i=0;i<tokens.length;i++) {
				//System.out.println("token#"+i+" is "+tokens[i]);
				System.out.println("adding dependency named:"+tokens[i]);
				this.dependencyNames.add(tokens[i]);
			}
		}
		else {
			System.out.println("adding dependency named:"+depNames);
			this.dependencyNames.add(depNames);
		}
	}


	//find each dependency from its name in dependencyNames and add the actual Dependency
	public ArrayList<String> setDeps(ArrayList<Activities> completedNodes) {
		ArrayList<String> errorList = new ArrayList<String>();
		boolean nodeExists;
		for(int i=0; i<this.dependencyNames.size();i++) {
			nodeExists=false;
			for(int j=0; j<completedNodes.size();j++) {
				//if the current name is found as the name of a completed node, set current node's parent to found
				//node and set found node's child to current node
				if(dependencyNames.get(i).equals( (completedNodes).get(j).getName() ) ){

					//if the process button has already run and the nodes have been added
					if(this.dependencies.contains(completedNodes.get(j))) {
						nodeExists=true;
						continue;
					}
					this.dependencies.add(completedNodes.get(j));
					completedNodes.get(j).children.add(this);
					nodeExists=true;
				}
			}
			if(!nodeExists) {
				errorList.add("Error: Node \""+this.getName()+"\" lists \""+this.dependencyNames.get(i)+"\" as a dependency but \""+this.dependencyNames.get(i)+"\" has not been added to the network.\n");
			}
		}
		return errorList;
	}


	public String getDependencyString(){
		if(this.dependencies.isEmpty()){
			return "none";
		}
		String output = "";
		if(this.dependencies.size()==1) {
			output+=this.dependencies.get(0).getName();
		}
		else if(this.dependencies.size()>1) {
			for(int i = 0; i < this.dependencies.size()-1; i++){
				output += dependencies.get(i).getName()+", ";
			}
			output+=this.dependencies.get(dependencies.size()-1).getName();
		}
		return output;
	}

	public boolean hasChild(){
		if(this.children!=null)
			return true;
		else
			return false;
	}


}


