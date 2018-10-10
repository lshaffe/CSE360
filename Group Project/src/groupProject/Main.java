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
		
		ArrayList list = new ArrayList();
		LinkedList network = new LinkedList();
		
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
				
				if(network.exists(name.getText()))
				{
					error.setText("Activity name already exists.");
				}
				
				else if(name.getText().equals("") || duration.getText().equals("") || dependencies.getText().equals(""))
				{
					error.setText("Please fill in all parameters");
				} 	
				else
				{
					if(dependencies.getText().toLowerCase().equals("none"))
					{
						//create a new path
						String[] dep = {}; 
						int durat = Integer.parseInt(duration.getText());
						Actvities act = new Actvities(name.getText(), durat ,dep);
						network.add(act);
						error.setText("Activity successfully added.");
					}
					else
					{
						String[] dep = dependencies.getText().split("/");
						int durat = Integer.parseInt(duration.getText());
						Actvities act = new Actvities(name.getText(), durat ,dep);
						network.add(act);
						error.setText("Activity successfully added.");
					}
					
				}
			}
		});
		
		refresh.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event) 
			{
				//TO STRING 
				output.setText(network.toString());
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
				network.delete();
			}
		});
		
		frame.setVisible(true);
		
		
		
	}

}
