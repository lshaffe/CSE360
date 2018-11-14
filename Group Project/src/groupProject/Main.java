package cse360;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class Main {

	
	
	public static void main(String[] args){
		
		JFrame frame = new JFrame("CSE 360");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setSize(860, 560);
		
		frame.setLayout(new BorderLayout());

		JButton add = new JButton("Add");
		JButton process = new JButton("Process");
		JButton critical = new JButton("Critical Path");
		JButton edit = new JButton("Edit");

		JButton about = new JButton("About");
		JButton help = new JButton("Help");
		JLabel fileName = new JLabel("Input File Name");
		JTextField rip = new JTextField(15);
		JButton report = new JButton("Create a Report");
		JButton restart = new JButton("Restart");
		JButton quit = new JButton("Quit");

		
		JPanel top = new JPanel();
		JTextArea output = new JTextArea(20,45);
		JScrollPane mainScroll = new JScrollPane(output);
		output.setText("No Activities");
		output.setEditable(false);
		
		JTextArea error = new JTextArea(20, 25);
		error.setLineWrap(true);
		error.setWrapStyleWord(true);
		error.setEditable(false);
		error.setText("No messages");
		
		top.add(mainScroll, BorderLayout.WEST);
		top.add(error, BorderLayout.EAST);
		
		frame.add(top, BorderLayout.NORTH);
		
		JLabel nameLabel = new JLabel("Input activity name");
		JLabel durationLabel = new JLabel("Input Duration");
		JLabel dependenciesLabel = new JLabel("Input dependencies, seperated by '/'");
		
		JTextField nameBox = new JTextField(15);
		JTextField durationBox = new JTextField(15);
		JTextField dependenciesBox = new JTextField(15);
	
		
		JPanel south = new JPanel();

		JPanel textfields = new JPanel();
		textfields.setLayout(new GridLayout(6,2,10,5));
		textfields.add(nameLabel);
		textfields.add(nameBox);
		textfields.add(durationLabel);
		textfields.add(durationBox);
		textfields.add(dependenciesLabel);
		textfields.add(dependenciesBox);
		
		textfields.add(add);
		textfields.add(process);
		textfields.add(critical);
		textfields.add(edit);
		
		south.add(textfields, BorderLayout.WEST);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(4,2,10,5));
		buttons.add(fileName);
		buttons.add(rip);
		buttons.add(report);
		buttons.add(about);
		buttons.add(help);
		buttons.add(restart);
		buttons.add(quit);
		
		south.add(buttons, BorderLayout.EAST);
		
		frame.add(south, BorderLayout.SOUTH);
		
		//pathsArray is a list of all paths through the network
		ArrayList<LinkedList> pathsArray = new ArrayList<LinkedList>();
		
		//an unconnected mass of all completed nodes
		ArrayList<Activities> nodes = new ArrayList<Activities>();
		
		
		//takes input from text boxes and adds a new complete node to the list of all nodes
		//DOES NOT ADD NODES TO ANY PATH, JUST CREATES NEW NODES TO BE PROCESSED LATER
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){	
				
				//if any boxes are not filled in
				if(nameBox.getText().equals("") || durationBox.getText().equals("") ) {
					error.setText("Please fill in all parameters");
					return;
				} 
				//if all boxes filled in
				else{	 
					//create new placeholder activity
					Activities actToAdd = new Activities();
					int inputDur; //placeholder for duration
					String inputName;
					String depNames;//the whole list, Activities has a method to parse to individual deps
					
					//test input duration
					try{	
						inputDur = Integer.parseInt(durationBox.getText());
					}
					catch(NumberFormatException e){
						error.setText("Please enter an integer for duration.");
						return;
					}
					
					
					//set the rest of the node's attributes from the input boxes
					inputName=nameBox.getText();
					for(int i=0;i<nodes.size();i++) {
						if(nodes.get(i).getName().equals(inputName)) {
							error.setText("Error, node "+inputName+" already exists in the network\n");
							return;
						}
					}
					depNames=dependenciesBox.getText();
					//add the attributes from the text boxes
					actToAdd.setDuration(inputDur);
					actToAdd.setName(inputName);
					actToAdd.setTentDeps(depNames);
					

					
					error.setText("Node "+inputName+" successfully added\n");
					nodes.add(actToAdd);
					
				}
			}
		}

		);//add action listener done
		
		
		//PROCESSES THE LIST OF COMPLETED NODES AND LINKS THEM ALL TOGETHER IN A SERIES OF LINKED LISTS
		//this is where the paths through the network are created and analyzed, as well as where any error messages
		//about said paths will arise 
		process.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				error.setText("");
				pathsArray.clear();//hard reset the list of paths each time to prevent duplicates
				boolean headFound=false;
				ArrayList<String>errorList = new ArrayList<String>();
				
				Activities head = new Activities();
				//find the head
				for(int i=0; i<nodes.size();i++) {
					if(nodes.get(i).getDependencyNames().isEmpty()) {
						//if there are multiple heads
						if(headFound==true) {
							
							//if this is the first duplicate head, print an error for the original head
							if(errorList.isEmpty()) {
								errorList.add("Error: Duplicate heads found, original head:"+head.getName()+"\n");
							}
							errorList.add("Duplicate head: "+nodes.get(i).getName()+"\n");
							continue;
						}
						headFound=true;
						head=nodes.get(i);//just the first one
					}
				}
				//if more than one head
				if(!errorList.isEmpty()) {
					String text="";
					for(int i=0; i<errorList.size();i++) {
						text+=errorList.get(i);
					}
					error.setText(text);
					return;
				}
				
				
				
				//if there was only one head detected
				//for each node, iterate through the list to link the node with it parents, and update the parent
				//with pointers to each of its children
				
				
				//for each node in our collection
				for(int i=0; i<nodes.size();i++) {
					
					//change the dependencies from names to pointers, also sets list of children in parents
					errorList=nodes.get(i).setDeps(nodes);
					//if some dependencies were named but not completed, display those messages
					if(!errorList.isEmpty()) {
						String text="";
						for(int j=0; j<errorList.size();j++) {
							text+=errorList.get(j);
						}
						String currentText=error.getText();
						error.setText(currentText+text);
					}
				}
				if(!errorList.isEmpty()) {
					return;
				}
				

				LinkedList newPath = new LinkedList();
				ArrayList<LinkedList> generatedPathsArray = new ArrayList<LinkedList>();
				generatedPathsArray=newPath.createPathArray(pathsArray,head,newPath);

				//end while current!= null, im going to try to make the above function recursive
				
				if(generatedPathsArray.size()>1) {
					//sort the completed paths through the network and print them in order of duration
					Collections.sort(generatedPathsArray, new sortByLength());
				}
				String out = "";
				for(int i = 0; i<generatedPathsArray.size(); i++){
					out +="Path: " + (i+1) + "\tDuration: " + generatedPathsArray.get(i).getTotalDuration() + "\n";
					out += generatedPathsArray.get(i).pathtoString() + "\n\n\n";
				}
				output.setText(out);
				error.setText("Processing completed successfully!");
				
			}
		});
		
		edit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				int duration; 
				String name = nameBox.getText(); 
				
				try{	
					duration = Integer.parseInt(durationBox.getText());
				}
				catch(NumberFormatException e){
					error.setText("Please enter an integer for duration.");
					return;
				}
				
				int i;
				for(i = 0; i < nodes.size(); i++)
				{
					if(nodes.get(i).getName().equals(name))
					{
						nodes.get(i).setDuration(duration);
					}
				}
				
				error.setText("The duration of activity " + name + " has been changed to " + duration + ".");
				
				
			}
		});
		
		critical.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				error.setText("");
				pathsArray.clear();//hard reset the list of paths each time to prevent duplicates
				boolean headFound=false;
				ArrayList<String>errorList = new ArrayList<String>();
				
				Activities head = new Activities();
				//find the head
				for(int i=0; i<nodes.size();i++) {
					if(nodes.get(i).getDependencyNames().isEmpty()) {
						//if there are multiple heads
						if(headFound==true) {
							
							//if this is the first duplicate head, print an error for the original head
							if(errorList.isEmpty()) {
								errorList.add("Error: Duplicate heads found, original head:"+head.getName()+"\n");
							}
							errorList.add("Duplicate head: "+nodes.get(i).getName()+"\n");
							continue;
						}
						headFound=true;
						head=nodes.get(i);//just the first one
					}
				}
				//if more than one head
				if(!errorList.isEmpty()) {
					String text="";
					for(int i=0; i<errorList.size();i++) {
						text+=errorList.get(i);
					}
					error.setText(text);
					return;
				}
				
				
				//if there was only one head detected
				//for each node, iterate through the list to link the node with it parents, and update the parent
				//with pointers to each of its children
				
				
				//for each node in our collection
				for(int i=0; i<nodes.size();i++) {
					
					//change the dependencies from names to pointers, also sets list of children in parents
					errorList=nodes.get(i).setDeps(nodes);
					//if some dependencies were named but not completed, display those messages
					if(!errorList.isEmpty()) {
						String text="";
						for(int j=0; j<errorList.size();j++) {
							text+=errorList.get(j);
						}
						String currentText=error.getText();
						error.setText(currentText+text);
					}
				}
				if(!errorList.isEmpty()) {
					return;
				}
				

				LinkedList newPath = new LinkedList();
				ArrayList<LinkedList> generatedPathsArray = new ArrayList<LinkedList>();
				generatedPathsArray=newPath.createPathArray(pathsArray,head,newPath);

				//end while current!= null, im going to try to make the above function recursive
				
				if(generatedPathsArray.size()>1) {
					//sort the completed paths through the network and print them in order of duration
					Collections.sort(generatedPathsArray, new sortByLength());
				}
				String out = "";
				int length = generatedPathsArray.get(0).getTotalDuration();
				for(int i = 0; i<generatedPathsArray.size(); i++){
					if(generatedPathsArray.get(i).getTotalDuration() == length)
					{
						out +="Path: " + (i+1) + "\tDuration: " + generatedPathsArray.get(i).getTotalDuration() + "\n";
						out += generatedPathsArray.get(i).pathtoString() + "\n\n\n";
					}
					
				}
				output.setText(out);
				error.setText("Critical Path(s) displayed.");
				
			}
		});
		
		about.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				error.setText("This project was created by Andrew Nguyen, Allen Soocey, Luke Shaffer, and Joel Torres. The program takes in a set of nodes from the user to create an activity network, which is then broken down into different paths.");
			}
		});
		
		help.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				error.setText("In the respective fields, input activity name, duration, and dependencies (input dependencies seperated by a '/'). Press the add button to add an activity to the list. Press process to print the current list of network paths (sorted by duration). Use the edit button to change the duration of an exisiting node. By pressing the critical path button, the critical path will be displayed. About and help give the user information about the program and the report button will create a text file with network information.");
			}
		});
		
		quit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		});
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    Date date = new Date();
	    
		report.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				error.setText("");
				pathsArray.clear();//hard reset the list of paths each time to prevent duplicates
				boolean headFound=false;
				ArrayList<String>errorList = new ArrayList<String>();
				
				Activities head = new Activities();
				//find the head
				for(int i=0; i<nodes.size();i++) {
					if(nodes.get(i).getDependencyNames().isEmpty()) {
						//if there are multiple heads
						if(headFound==true) {
							
							//if this is the first duplicate head, print an error for the original head
							if(errorList.isEmpty()) {
								errorList.add("Error: Duplicate heads found, original head:"+head.getName()+"\n");
							}
							errorList.add("Duplicate head: "+nodes.get(i).getName()+"\n");
							continue;
						}
						headFound=true;
						head=nodes.get(i);//just the first one
					}
				}
				//if more than one head
				if(!errorList.isEmpty()) {
					String text="";
					for(int i=0; i<errorList.size();i++) {
						text+=errorList.get(i);
					}
					error.setText(text);
					return;
				}
				
				
				//if there was only one head detected
				//for each node, iterate through the list to link the node with it parents, and update the parent
				//with pointers to each of its children
				
				
				//for each node in our collection
				for(int i=0; i<nodes.size();i++) {
					
					//change the dependencies from names to pointers, also sets list of children in parents
					errorList=nodes.get(i).setDeps(nodes);
					//if some dependencies were named but not completed, display those messages
					if(!errorList.isEmpty()) {
						String text="";
						for(int j=0; j<errorList.size();j++) {
							text+=errorList.get(j);
						}
						String currentText=error.getText();
						error.setText(currentText+text);
					}
				}
				if(!errorList.isEmpty()) {
					return;
				}
				

				LinkedList newPath = new LinkedList();
				ArrayList<LinkedList> generatedPathsArray = new ArrayList<LinkedList>();
				generatedPathsArray=newPath.createPathArray(pathsArray,head,newPath);

				//end while current!= null, im going to try to make the above function recursive
				
					
				
				
				if(rip.getText() == "")
				{
					error.setText("Must insert file name in order to create a report.");
					
				}
				String path = "C://Users//asoocey//Desktop//team//" + rip.getText() + ".txt";
				File report = new File(path);
				try {
				FileWriter write = new FileWriter(report);
				write.write( formatter.format(date));
				write.write(System.getProperty("line.separator"));
				write.write("ACTIVITY NETWORK REPORT");
				write.write(System.getProperty("line.separator"));
				write.write(System.getProperty("line.separator"));

				write.write("List of all nodes:");
				write.write(System.getProperty("line.separator"));

				ArrayList<String> sorted = new ArrayList<String>();
				for(int i = 0; i < nodes.size(); i++)
				{
					sorted.add(nodes.get(i).getName() + "\t" + nodes.get(i).getDuration());
				}
				Collections.sort(sorted, String.CASE_INSENSITIVE_ORDER);
				for(int i = 0; i < sorted.size(); i++)
				{
					write.write(sorted.get(i));
					write.write(System.getProperty("line.separator"));

				}
				write.write(System.getProperty("line.separator"));
				write.write(System.getProperty("line.separator"));

				
				if(generatedPathsArray.size()>1) {
					//sort the completed paths through the network and print them in order of duration
					Collections.sort(generatedPathsArray, new sortByLength());
				}
				for(int i = 0; i<generatedPathsArray.size(); i++){
					write.write("Path: " + (i+1) + "\tDuration: " + generatedPathsArray.get(i).getTotalDuration());
					write.write(System.getProperty("line.separator"));
					write.write(generatedPathsArray.get(i).pathtoString());
					write.write(System.getProperty("line.separator"));
					write.write(System.getProperty("line.separator"));
					write.write(System.getProperty("line.separator"));

				}	
				write.close();				
				}
				catch(Exception e)
				{
					
				};
				
			}
		});
		
		restart.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				error.setText("No messages");
				output.setText("No Activities");
				
				nameBox.setText("");
				durationBox.setText("");
				dependenciesBox.setText("");
				
				pathsArray.clear();//delete arraylist
				nodes.clear();//delete
			}
		});
		
		frame.setVisible(true);
	}
	

	
	/*old process logic
	LinkedList newPath = new LinkedList();
	Activities current = new Activities();
	newPath.add(head);
	current=head;

	while(current!=null) {
		//iterate through the list by children
		
		//base case
		if(current.getChildren().size()==0) {
			current=null;
			if(pathsArray.contains(newPath)) {
				
			}
			else {
				paths.add(newPath);
			}
			break;//prevents adding this last empty path
		}
		//if there is only one child, add it and continue
		else if(current.getChildren().size()==1) {
			newPath.add(current.getOnlyChild());
			current=current.getOnlyChild();
		}
		
		//else if this node has multiple children
		else if (current.getChildren().size()>1) {
		//for each child listed
			System.out.println("HAHA, multiple children, exiting");
			//break;
			ArrayList<LinkedList> listOfChildrenPaths= new ArrayList<LinkedList>();
			for(int child=0;child<current.getChildren().size();child++) {
				//recursively add paths
				LinkedList childpath=newPath.copy();
				
			}
		}
		if(!pathsArray.contains(newPath)) {
			pathsArray.add(newPath);
		}
	}
	*/

}