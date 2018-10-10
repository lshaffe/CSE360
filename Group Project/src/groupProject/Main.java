package groupProject;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Main {

	public static void main(String[] args){
		
		JFrame frame = new JFrame("Testing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setSize(800, 500);
		
		frame.setLayout(new BorderLayout());

		JButton add = new JButton("Add");
		JButton refresh = new JButton("Refresh");

		JButton about = new JButton("About");
		JButton help = new JButton("Help");
		JButton restart = new JButton("Restart");
		JButton quit = new JButton("Quit");

		
		JPanel top = new JPanel();
		JTextArea output = new JTextArea(20,40);
		output.setText("No Activities");
		output.setEditable(false);
		
		JTextArea error = new JTextArea(20, 25);
		error.setLineWrap(true);
		error.setWrapStyleWord(true);
		error.setEditable(false);
		error.setText("No messages");
		
		top.add(output, BorderLayout.WEST);
		top.add(error, BorderLayout.EAST);
		
		frame.add(top, BorderLayout.NORTH);
		
		JLabel nam = new JLabel("Input activity name");
		JLabel dur = new JLabel("Input Duration");
		JLabel depe = new JLabel("Input dependencies, seperated by '/'");
		
		JTextField name = new JTextField(15);
		JTextField duration = new JTextField(15);
		JTextField dependencies = new JTextField(15);
	
		
		JPanel south = new JPanel();

		JPanel textfields = new JPanel();
		textfields.setLayout(new GridLayout(4,2,10,0));
		textfields.add(nam);
		textfields.add(name);
		textfields.add(dur);
		textfields.add(duration);
		textfields.add(depe);
		textfields.add(dependencies);
		
		textfields.add(add);
		textfields.add(refresh);
		
		south.add(textfields, BorderLayout.WEST);
		
		JPanel buttons = new JPanel();
		buttons.add(about);
		buttons.add(help);
		buttons.add(restart);
		buttons.add(quit);
		
		south.add(buttons, BorderLayout.EAST);
		
		frame.add(south, BorderLayout.SOUTH);
		
		ArrayList<LinkedList> list = new ArrayList<LinkedList>();
		
		add.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					int number = Integer.parseInt(duration.getText());
					
				}
				catch(NumberFormatException e)
				{
					error.setText("Please enter an integer for duration.");
				}
				

				
				if(name.getText().equals("") || duration.getText().equals("") || dependencies.getText().equals(""))
				{
					error.setText("Please fill in all parameters");
					return;
				} 	
				else
				{	 
					if(dependencies.getText().toLowerCase().equals("none"))
					{
						//create a new path
						LinkedList network = new LinkedList();//new path
						list.add(network);
						String[] dep = {}; 
						int durat = Integer.parseInt(duration.getText());
						Actvities act = new Actvities(name.getText(), durat ,dep);
						network.add(act);
						error.setText("Activity successfully added.");
					}
					
					
					else
					{//add to existing path
						String[] dep = dependencies.getText().split("/");
						int durat = Integer.parseInt(duration.getText());
						Actvities act = new Actvities(name.getText(), durat ,dep);
						
						int j = 0;
						int x=0;
						
						boolean nodeExists=false;
						boolean dependExists=false;
						boolean multiDepExists=false;
						  
						//for each LinkedList in the ArrayList
						for(int i = 0; i < list.size(); i++)
							{
								if(list.get(i).exists(name.getText()))
								{
									x=i;
									nodeExists=true;
								}
								if(list.get(i).exists(dependencies.getText())) //only use for singular dependencies
								{
									j=i;
									dependExists=true;
								}
								if(dep.length > 1)///////////////////////////////////////////////////////////////////////IF MULTI DEPENDENCIES
								{
									if(list.get(i).multiDepenExist(dep))
									{
										multiDepExists = true;
									}
								}
							}
								//check through each LinkedList for Node existence
								if (!nodeExists && dep.length==1)
								{
									if(dependExists)
									{
										//as ghost:
										if(list.get(j).isGhost())
											//add new node as dependency.next
											list.get(j).add(act);
										
										//as full node
										{
											//add new node as dependency.next
											list.get(j).add(act);
						
											if(list.get(j).hasLoop())
											{
												error.setText("Node would create cycle.");
												list.get(j).removeNode(act.getName());
												return;
											}
											
											error.setText("Activity successfully added.");
										}
									}
											
									//else if dependency !exist:
									else 
									{
										//create ghost dependency node
										Actvities dependency = new Actvities(dep[0],0,null,true);
										
										//create new LinkedList(ghost->full node)
										LinkedList ghostList= new LinkedList();
										ghostList.add(dependency);
										ghostList.add(act);
										
										//add LinkedList to ArrayList
										list.add(ghostList);
										return;
									}
								}
								
								//if the node to be added does exist somewhere
								else if (nodeExists && dep.length==1)
								{
									//if the node exists as a ghost node
									if(list.get(x).isGhost())
									{
										//fill in details of the node from user input
										list.get(x).updateHead(name.getText(), durat ,dep);
										//find dependency of the node,
										LinkedList newList= new LinkedList();
										//newList=newList.combine(list.get(j),list.get(x),dependencies.getText(),name.getText());
										newList.combine(list.get(j),list.get(x),dependencies.getText(),name.getText());
										
										LinkedList lista=list.get(j);
										LinkedList listb=list.get(x);
										
										/*
										list.remove(lista);
										list.remove(listb);
										list.add(newList);
										*/

										list.remove(listb);
									}
									
									//if the node exists as a regular node, print error
									else if(!(list.get(x).isGhost()))
									{
										error.setText("Error, that node already exists");
										return;
									}
								}	
								
								//MULTI DEPENDENCIES
								//check through each LinkedList for Node existence
								if (!nodeExists && dep.length>1)
								{
									//for each dependency
									for(int d=0;d<dep.length;d++)
									{
										//if ArrayList is empty, create a first empty list
										if(list.size()==0)
										{
											//create ghost dependency node
											Actvities dependency = new Actvities(dep[d],0,null,true);
											
											//create new LinkedList(ghost->full node)
											LinkedList ghostList= new LinkedList();
											ghostList.add(dependency);
											ghostList.add(act);
											
											//add LinkedList to ArrayList
											list.add(ghostList);
											continue;
										}
										//if it exists in any LinkedList in the ArrayList
										int t=0;
										boolean indvExists=false;
										for(t=0;t<list.size();t++)
										{
											if(list.get(t).exists(dep[d]))
											{
												indvExists=true;
												break;
											}
										}
											if(indvExists)
											{
												//as ghost:
												if(list.get(t).isGhost())
													//add new node as dependency.next
													list.get(t).add(act);
												
												//as full node
												{
													//add new node as dependency.next
													list.get(t).add(act);
								
													if(list.get(t).hasLoop())
													{
														error.setText("Node would create cycle.");
														list.get(t).removeNode(act.getName());
														return;
													}
													
													error.setText("Activity successfully added.");
												}
											}
													
											//else if dependency !exist:
											else 
											{
												//create ghost dependency node
												Actvities dependency = new Actvities(dep[d],0,null,true);
												
												//create new LinkedList(ghost->full node)
												LinkedList ghostList= new LinkedList();
												ghostList.add(dependency);
												ghostList.add(act);
												
												//add LinkedList to ArrayList
												list.add(ghostList);
												continue;
											}
										}
									}
								//if the node to be added does exist somewhere
								else if (nodeExists && dep.length>1)
								{
									//if the node exists as a ghost node
									if(list.get(x).isGhost())
									{
										for(int d=0;d<dep.length;d++)
										{
											//fill in details of the node from user input
											list.get(x).updateHead(name.getText(), durat ,dep);
											//find dependency of the node,
											LinkedList newList= new LinkedList();
											//newList=newList.combine(list.get(j),list.get(x),dependencies.getText(),name.getText());
											newList.combine(list.get(j),list.get(x),dependencies.getText(),name.getText());
											/*
											LinkedList lista=list.get(j);
											LinkedList listb=list.get(x);
											
											list.remove(lista);
											list.remove(listb);
											list.add(newList);
											
											LinkedList lista=list.get(j);
											LinkedList listb=list.get(x);
											
											if(d==dep.length-1)
												list.remove(listb);
											list.remove(lista);
											list.add(newList);
											*/
										}
									}
									
									//if the node exists as a regular node, print error
									else if(!(list.get(x).isGhost()))
									{
										error.setText("Error, that node already exists");
										return;
									}
								}	
							}
					
					
				
						
						/*
						for(int i= 0; i < list.size(); i++)
						{
							if(list.get(i).exists(dependencies.getText()))
									j = i;
						}
						
						
						list.get(j).add(act);
						
						if(list.get(j).hasLoop())
						{
							error.setText("Node would create cycle.");
							list.get(j).removeNode(act.getName());
							return;
						}
						
						error.setText("Activity successfully added.");
						*/
					}
					
				}
			}
		);
		
		refresh.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event) 
			{
				//TO STRING 
				LinkedList temp;
				
				Collections.sort(list, new sortByLength());
			
				
				
				String out = "";
				
				
				for(int i = 0; i<list.size(); i++)
				{
					out +="Path: " + (i+1) + "\tDuration: " + list.get(i).durationTotal() + "\n";
					out += list.get(i).pathtoString() + "\n\n\n";
				}
				output.setText(out);
			}
		});
		
		about.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				error.setText("This program takes in a set of user activites, then sorts them in descending order by duration of the activity.");
			}
		});
		
		help.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				error.setText("In the respective fields input activity name, duration, and dependencies. (Input dependencies seperated by a '/'). Press the add button to add an activity to the list. Press refresh to print current list of activites (sorted by duration).");
			}
		});
		
		quit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		});
		
		restart.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				error.setText("No messages");
				output.setText("No Activities");
				
				list.clear();//delete arraylist
			}
		});
		
		frame.setVisible(true);
		
		
		
	}

}
