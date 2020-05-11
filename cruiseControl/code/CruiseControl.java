/**
 *	Authors: Eric Altenburg, Michael McCreesh, Hamzah Nizami, Constance Xu
 *	Description: Cruise Control Simulation for CS347
 *	Pledge: I pledge my honor that I have abided by the Stevens Honor System. 
*/

import java.io.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.time.LocalTime;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.*;


public class CruiseControl {

	/**
     * 
     */
    public class Engine {
    	// Data fields of engine
        private int current_speed;

        // Constructor
        public Engine(){
            current_speed = 0;
        }

        /**
    	 * 	Returns current_speed
    	 */
        public int get_current_speed(){
            return current_speed;
        }

        /**
    	 * 	Sets current speed
    	 */
        public boolean set_current_speed(int i) {
        	if (i >= 0 && i <= 160) {
        		current_speed = i;
        		return true;
        	} else {
        		return false;
        	}
        }
    }

	/**
     *	Brake object holding value of brake being pressed
     */
    public class Brake {
        // Keeps track if the brake is activated
        private boolean is_brake;
        
        // Constructor
        public Brake(){
            is_brake = false;
        }

        /**
    	 * 	Returns value of brake
    	 */
        public boolean is_brake_pressed(){
            return is_brake;
        }

        /**
     	 * Sets value of break then writes to log
     	 */
        public void set_brake(boolean b, BufferedWriter cc_log, String inst) throws IOException{
            is_brake = b;
            write_to_log(cc_log, inst);
        }

    }
	
	/**
	 * Log object holding time stamp and the instruction
	 **/
	public static class Log {
		// Holds current instruction to be written
		private String instruction;

		// Constructor
		public Log(String instruction) {
			this.instruction = instruction;
		}

		/**
    	 * 	Returns a string of the and the instruction
     	 */
		public String toString() {
			LocalTime time = LocalTime.now();

			return time + "\t\t" + instruction + "\n";
		}
	}

    // Data fields for cruise control
    private int cruise_speed; 
    private boolean is_activated;
    private boolean is_set;
    private Engine cc_engine;
	private Brake cc_brake;

	// Constructor
    public CruiseControl(){
    	cc_engine = new Engine();
    	cc_brake = new Brake();
        cruise_speed = 0;
        is_activated = false;
        is_set = false;
    }

    /**
     *  Writes to log through buffered writer and log object, throws IOException
     * 	if the files doesn't exist.
     */
    public void write_to_log(BufferedWriter cc_log, String inst) throws IOException {
    	// Creates log object with time stamp and action and append to cc_log
		Log temp = new Log(inst);
		cc_log.append(temp.toString());
    }

    public int get_cruise_speed() {
    	return cruise_speed;
    }

    /**
     * This function returns whether or not cruise control is activated
     */
    public boolean is_cruise_control_activated(){
        return is_activated;
    }

    /**
     * This function returns whether or not cruise control is set 
     */
    public boolean is_cruise_control_set () {
    	return is_set;
    }

    /**
     * This will decrement the cruise control speed by 1 every time it is called. 
     * It will also ensure that is_activated = true and that the cruise control 
     * speed is greater than 25. If it is 25 or lower, it will not decrement. 
     */
    public boolean decrement_speed(BufferedWriter cc_log) throws IOException {
    	// Check to see if the cc is activated and the speed is not at the limit of 25
        if (is_cruise_control_activated() && cruise_speed > 25) {
        	// If the car speed is equal to the pre cc decrement, then gas is not pressed and engine speed is updated
        	if (cc_engine.get_current_speed() == cruise_speed) {
        		cc_engine.set_current_speed(cc_engine.get_current_speed() - 1);
        	}

        	// Decrement cc speed and write to log then return true for successful decrement
            cruise_speed -= 1;
        	String decrement = "decremented CC speed by 1: " + cruise_speed;
        	write_to_log(cc_log, decrement);
        	return true;
        } else {
        	// Write to log and return false for unsuccessful decrement
        	String failed = "Cruise control failed to decrement speed.";
        	write_to_log(cc_log, failed);
        	return false;
        }
    }
    
    /**
     * This will increment the cruise control speed by 1 every time it is called.
     * It will also ensure that is_activated = true and that the cruise control 
     * speed is actually set to something above or equal to 25 mph. 
     */
    public boolean increment_speed(BufferedWriter cc_log) throws IOException{
    	// Check to see if the cc is activated and the speed is not at the limits of 25 or 100
        if (is_cruise_control_activated() && cruise_speed >= 25 && cruise_speed < 100) {
        	// If the car speed is equal to pre cc increment, then gas is not pressed and engine speed is updated
        	if (cc_engine.get_current_speed() == cruise_speed) {
        		cc_engine.set_current_speed(cc_engine.get_current_speed() + 1);
        	}

        	// Increment cc speed and write to log then return true for successful increment
            cruise_speed += 1;
            String increment = "incremented CC speed by 1: " + cruise_speed;
    	    write_to_log(cc_log, increment);
    	    return true;
        } else {
        	// Write to log and return false for unsuccessful increment
        	String failed = "Cruise control failed to increment speed.";
        	write_to_log(cc_log, failed);
        	return false;
        }
    }

    /**
     * This function sets the cruise control speed as long as cruise control is activated
     */
    public boolean set_speed(BufferedWriter cc_log) throws IOException {
    	// Cruise control cannot set speed unless car is going at least 25 mph or under 100 mph
        if (is_cruise_control_activated() && cc_engine.current_speed >= 25 && cc_engine.current_speed < 100) {
        	// Set cc speed to car speed write to log and return true for successful set 
            cruise_speed = cc_engine.get_current_speed();
            String set_s = "Cruise control speed has been set to " + cruise_speed;
            write_to_log(cc_log, set_s);
            is_set = true;
            return true;
        } else {
        	// Write to log that it failed and return false
        	String failed = "Cruise control speed failed to be set.";
        	write_to_log(cc_log, failed);
        	return false;
        }
    }

    /**
     * 	This function unsets the cruise control speed reverting it to 0
     */
    public void unset_speed(BufferedWriter cc_log) throws IOException {
    	// Reset cruise speed and is_set to false
    	cruise_speed = 0;
    	is_set = false;

    	// Write to log
    	String unset_s = "Cruise control speed has been unset";
    	write_to_log(cc_log, unset_s);
    }

    /**
     * This function will set is_actiaved to true to simulate turning on cruise control.
     */
    public void activate_cruise_control(BufferedWriter cc_log) throws IOException {
    	// Write to log and change value
    	String activation = "activated cruise control";
    	write_to_log(cc_log, activation);
        is_activated = true;
    }

    /**
     * This function will set is_activated to false to simulate turning off cruise control. 
     */
    public void deactivate_cruise_control(BufferedWriter cc_log) throws IOException {
    	// Write to log and change value
    	String deactivation = "deactivated cruise control";
    	write_to_log(cc_log, deactivation);
        is_activated = false;
    }

    public static void main (String[] args) throws IOException {
        // Create log file to be passed around
		BufferedWriter cc_log = new BufferedWriter(new FileWriter("cc.log", true));
		LocalTime time_of_car = LocalTime.now();
		cc_log.write(time_of_car + "\tBeginning of log:\n");
		
        // Create new cruise control object
        CruiseControl j = new CruiseControl();

        // Make new frame for buttons
        JFrame module = new JFrame("Cruise Control");

        // Various objects placed on frame
        JLabel speedHere = new JLabel("Current Car Speed: ");
        speedHere.setBounds(40, 30, 150, 20);

        JLabel speedNotif = new JLabel("0 mph");
        speedNotif.setBounds(200, 30, 350, 20);

        JLabel ccSpeedHere = new JLabel("Current CC Speed: ");
        ccSpeedHere.setBounds(40, 90, 150, 20);

        JLabel ccSpeedNotif = new JLabel("0 mph");
        ccSpeedNotif.setBounds(200, 90, 350, 20);

        JLabel setCarSpeedHere = new JLabel("Set Car Speed: ");
        setCarSpeedHere.setBounds(40, 150, 150, 20);

        JTextField setCarSpeed = new JTextField();
        setCarSpeed.setBounds(200, 150, 150, 20);

        JButton returnToCC = new JButton("Return to CC Speed");
        returnToCC.setBounds(40, 210, 150, 20);

        JButton brake = new JButton("Brake");
        brake.setBounds(200, 210, 150, 20);

        JButton activatecc = new JButton("Activate CC");
        activatecc.setBounds(40, 270, 150, 20);

        JButton deactivatecc = new JButton("Deactivate CC");
        deactivatecc.setBounds(200, 270,150,20);

        JButton setCCSpeed = new JButton("Set CC Speed");
        setCCSpeed.setBounds(40, 330, 150, 20);

        JButton unsetCCSpeed = new JButton("Unset CC Speed");
        unsetCCSpeed.setBounds(200, 330, 150, 20);

        JButton increasecc = new JButton("Increase CC Speed");
        increasecc.setBounds(40, 390, 150, 20);

        JButton decreasecc = new JButton("Decrease CC Speed");
        decreasecc.setBounds(200, 390, 150, 20);

        JLabel notifHere = new JLabel("Notifications: ");
        notifHere.setBounds(40, 450, 150, 20);

        JLabel notifications = new JLabel("Set a speed to turn on.");
        notifications.setBounds(200, 450, 350, 20);

        JLabel adminHere = new JLabel("Admin Login: ");
        adminHere.setBounds(40, 510, 150, 20);

        JPasswordField adminAccess = new JPasswordField(); 
        adminAccess.setBounds(200, 510, 150, 20);


        //add all of the buttons to the frame. 
        module.add(returnToCC);
        module.add(brake);
        module.add(deactivatecc);
        module.add(activatecc);
        module.add(increasecc);
        module.add(setCarSpeedHere);
        module.add(setCarSpeed);
        module.add(ccSpeedHere);
        module.add(ccSpeedNotif);
        module.add(setCCSpeed);
        module.add(unsetCCSpeed);
        module.add(decreasecc);
        module.add(notifHere);
        module.add(notifications);
        module.add(speedHere);
        module.add(speedNotif);
        module.add(adminHere);
        module.add(adminAccess);
        

        // Event to set the cc speed to whatever value the engine speed is.
        setCCSpeed.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			// Check to see if the cc is activated and subsequently sets correctly
                    if(j.is_cruise_control_activated()){ 
                    	boolean temp = j.set_speed(cc_log);
                    	if (temp) {
                    		// Change the colors of the buttons to reflect that cc is set
                    		setCCSpeed.setForeground(Color.GREEN);
	                        unsetCCSpeed.setForeground(Color.RED);
	                        module.add(setCCSpeed);
	                        module.add(unsetCCSpeed);

	                        // Update the cc speed notif and push notification
	                        notifications.setText("Cruise control speed set.");
	                        ccSpeedNotif.setText(j.get_cruise_speed() + " mph");
                    	} else {
                    		notifications.setText("CC Speed failed to set.");
                    	}
                    } else { 
                        notifications.setText("Activate cruise control to set speed.");
                    }
        		} catch (IOException f) {
        			System.out.println(f.getMessage());
        		}
        	}
        });

        // Event that unsets the cc speed reseting the value to 0. Does not impact the engine speed.
        unsetCCSpeed.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			// Check to see if cc is activated
                    if(j.is_cruise_control_activated()){
                    	// Check to see if cc is set as nothing might have to be done
                    	if (!j.is_cruise_control_set()) { 
                    		notifications.setText("Set CC before unsetting it.");
                    	} else {	
                    		// Unset the cc speed
                    		j.unset_speed(cc_log);

                    		// Change colors of buttons to signify cc is unset
	                        setCCSpeed.setForeground(Color.RED);
	                        unsetCCSpeed.setForeground(Color.GREEN);
	                        module.add(setCCSpeed);
	                        module.add(unsetCCSpeed);

	                        // Push the notification and update cc speed notif
	                        notifications.setText("Cruise control speed unset.");
	                        ccSpeedNotif.setText("0 mph");
                    	}
                    } else { 
                        notifications.setText("Activate cruise control to unset speed.");
                    }
        		} catch (IOException f) {
        			System.out.println(f.getMessage());
        		}
        	}
        });

        // Event to set the car speed to whatever value is given. This only updates the engine speed and not
        // the cc speed.
        setCarSpeed.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{ 
                	// Grab current string in box and set engine speed so long as >= 0 and <= 160
                    String text_ = setCarSpeed.getText();
                    if (j.cc_engine.set_current_speed(Integer.parseInt(text_))) {
                    	// Set the brake to false and push to log
                    	String log_text = "Brake released. Throttle engaged. Set speed to: " + j.cc_engine.get_current_speed() + " mph";
                   		j.cc_brake.set_brake(false, cc_log, log_text);

                   		// Push the car speed notif, regular notification, and then reset the field
                    	notifications.setText("Car speed set to " + text_ + " mph");
                    	speedNotif.setText(text_ + " mph");
                    	setCarSpeed.setText("");
                    } else {
                    	notifications.setText("Failed to set car speed.");
                    }
                } catch (IOException f){ 
                    System.out.println(f.getMessage());
                }
            }
        });

        // Admin login event. If the right password is given (Reza347), then a separate frame will
        // open up showing the logs.
        adminAccess.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e){ 
                try{ 
                	// Grab the current password as char[]
                    char[] text_ = adminAccess.getPassword();
                    char[] pass = {'R', 'e', 'z', 'a', '3', '4', '7'};

                    // Compare to see if passwords match
                    if(Arrays.equals(text_, pass)){ 
                    	// Create new frame, panel, and scrollpane for log
                        JFrame admin = new JFrame("Admin: Logs"); 
                        JPanel panel = new JPanel();
                        JScrollPane scrollInfo=new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 

                        // Update the log for the admin accessing
                        j.write_to_log(cc_log, "ADMIN ACCESSED.");

                        // Delete admin frame upon closing window
                        admin.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                                admin.dispose();
                            }
                        });

                        // Grab log file for JTextArea and add it to panel
                        JTextArea logs = new JTextArea(); 
                        FileReader reader = new FileReader("cc.log");
                        logs.read(reader, "cc.log");
                        panel.add(logs);

                        // Add scrollInfo to admin frame then set size and visibility
                        admin.add(scrollInfo);
                        admin.setSize(650, 650);
                        admin.setVisible(true);
                    } else { 
                        notifications.setText("Incorrect password provided.");
                    }
                    
                    // Reset the password for each attempt either good or bad entry
                    adminAccess.setText("");
                } catch (IOException f){ 
                    System.out.println(f.getMessage());
                }
            }
        });

        // Event to increment the cc speed. If pressed, then the cc speed will increment by 1 and the car speed
        // will only increment if it has the same speed.
        increasecc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                	// Check to see if cc is activated and set for error notifs
                    if(j.is_cruise_control_activated()){ 
                        if(j.is_cruise_control_set()){ 
                        	// Check to see if the increment was valid (i.e. speed was at 25 or 100)
                        	if (j.increment_speed(cc_log)) {
                        		// Push notification, updating the car speed notif and cc speed notif
                            	notifications.setText("Incremented CC Speed to " + j.get_cruise_speed());
                            	speedNotif.setText(j.cc_engine.get_current_speed() + " mph");
                            	ccSpeedNotif.setText(j.get_cruise_speed() + " mph");
                        	} else {
                        		notifications.setText("Failed to increment CC Speed.");
                        	}
                        } else { 
                            notifications.setText("Set cruise control before increasing.");
                        }
                    } else { 
                        notifications.setText("Activate cruise control before increasing.");
                    }
                } catch (IOException f){
                    System.out.println(f.getMessage());
                }
            }
        });

        // Event to decrement the cc speed. If pressed, then the cc speed will decrease by 1 and the car speed
        // will only decrement if it has the same speed.
        decreasecc.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			// Check to see if cc is activated and set for error notifs
                    if(j.is_cruise_control_activated()){ 
                        if(j.is_cruise_control_activated()){ 
                        	// Check to see if the decrement was valid (i.e. speed was at 25)
                        	if (j.decrement_speed(cc_log)) {
                        		// Push notification, update the car speed, and the cc speed
                        		notifications.setText("Decremented CC Speed to " + j.get_cruise_speed());
                            	speedNotif.setText(j.cc_engine.get_current_speed() + " mph");
                            	ccSpeedNotif.setText(j.get_cruise_speed() + " mph");
                        	} else {
                        		notifications.setText("Failed to decrement CC Speed.");
                        	}
                        } else { 
                            notifications.setText("Set cruise control before decreasing.");
                        }
                    } else { 
                        notifications.setText("Activate cruise control before decreasing.");
                    }
        		} catch (IOException f) {
        			System.out.println(f.getMessage());
        		}
        	}
        });

        // Event to make sure the cc is activated. Nothing else changes.
        activatecc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                	// If the cc is already activated, no need to change things
                    if(j.is_cruise_control_activated()){ 
                        notifications.setText("Cruise control already activated.");
                    } else {
                    	// Pass log to activate cc and change colors for feedback
                        j.activate_cruise_control(cc_log);
                        activatecc.setForeground(Color.GREEN);
                        deactivatecc.setForeground(Color.RED);
                        module.add(activatecc);
                        module.add(deactivatecc);

                        // Push notification
                        notifications.setText("Cruise Control activated");
                    }
                }
                catch(IOException f) {
                    System.out.println(f.getMessage());
                }
            }
        });

        // Event to deactivate the cc. Upon being pressed, it will unset current cc setting to 0.
        deactivatecc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                	// Check to see if cc is deactivated
                	if (!j.is_cruise_control_activated()) { 
                		// Already deactivated so no need to change colors just say it's already deactivated
                		notifications.setText("Cruise control already deactivated");
                	} else {
                		// Pass log and deactivate cc and change colors
                		j.deactivate_cruise_control(cc_log);
                    	activatecc.setForeground(Color.RED);
                   		deactivatecc.setForeground(Color.GREEN);
                   		module.add(activatecc);
                    	module.add(deactivatecc);

                    	// If the cruise control is set we want to change the colors and add them to the frame
                    	if (j.is_cruise_control_set()) {
                    		j.unset_speed(cc_log);
                    		setCCSpeed.setForeground(Color.RED);
                    		unsetCCSpeed.setForeground(Color.GREEN);
							module.add(setCCSpeed);
                    		module.add(unsetCCSpeed);

                    		// Push the cc notif as the speed is now 0
                    		ccSpeedNotif.setText(j.get_cruise_speed() + " mph");
                    	}
                    	
                    	// Push the notification saying it was deactivated
                    	notifications.setText("Cruise control deactivated");
                	}
                } catch (IOException f) {
                    System.out.println(f.getMessage());
                }
            }
        });

        // Event to have the car speed return to the cruise control speed. Ex: Car speed gas pressed and 
        // speed is now 90 but cc is set to 65. Upon being pressed, the car speed will revert to cc speed
        returnToCC.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// Check to see if set
    			if (j.is_cruise_control_set()) {
    				// Have engine speed become current set cc speed
    				j.cc_engine.set_current_speed(j.get_cruise_speed());

    				// Update the car speed notif and push notif
    				speedNotif.setText(j.cc_engine.get_current_speed() + " mph");
    				notifications.setText("Car speed returned to CC speed.");
    			} else {
    				notifications.setText("CC must be set before returning.");
    			}
        	}
        });

        // Event when the brake is pressed. Upon being pressed, it should set speed vals to 0
        // and unset cc speed but not deactivate.
        brake.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e){ 
                try {  
                	// Bounds to check if brake is activated during cruise control set
                    if(j.is_cruise_control_set()){ 
                    	// Update colors and add to frame
                        setCCSpeed.setForeground(Color.RED);
                        unsetCCSpeed.setForeground(Color.GREEN); 
                        module.add(setCCSpeed);
                        module.add(unsetCCSpeed);

                        // Set engine speed to 0, push to log, set brake to true, and unset cc
                        j.cc_engine.set_current_speed(0);
                        String log_text = "Brake activated. CC Speed set to 0. Car speed set to " + j.cc_engine.get_current_speed() + " mph";
                        j.cc_brake.set_brake(true, cc_log, log_text);
                        j.unset_speed(cc_log);

                        // Push the notification, and update car and cc speed notif
                        notifications.setText("Brake pressed. Car speed: " + j.cc_engine.get_current_speed() + " mph");
                        speedNotif.setText(j.cc_engine.get_current_speed() + " mph");
                        ccSpeedNotif.setText(j.get_cruise_speed() + " mph");
                    } else { 
                    	// If not set, just change engine speed to 0, add log text, and set brake to true.
                    	j.cc_engine.set_current_speed(0);
                        String log_text = "Brake activated. CC Speed set to 0. Car speed set to " + j.cc_engine.get_current_speed() + " mph";
                        j.cc_brake.set_brake(true, cc_log, log_text);

                        // Push notification and update only car speed as cc not set
                        notifications.setText("Brake pressed. Car speed: " + j.cc_engine.get_current_speed() + " mph");
                        speedNotif.setText(j.cc_engine.get_current_speed() + " mph");
                    }
                } catch (IOException f){ 
                    System.out.println(f.getMessage());
                }
            }
        });

        // Event to close window and then flush the stream
        module.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            	// check to see if the user wants to close out the window
                if (JOptionPane.showConfirmDialog(module, "Are you sure you want to close this window?", "Close Window?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        try{
                        	// Append new line to distinguish different drives and close stream
                        	cc_log.append("\n");
                            cc_log.close();
                            System.exit(0);
                        } catch (IOException f) {
                            System.out.println(f.getMessage());
                        }
                }
            }
        });
        

        // Setting module frame to be visible
        module.setSize(500,600);
        module.setLayout(null);
        module.setVisible(true);
        
	}
}