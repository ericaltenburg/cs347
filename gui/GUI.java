//Usually you will require both swing and awt packages
// even if you are working with just swings.
import javax.swing.*;
import java.awt.*;
class gui {
    public static void main(String args[]) {

        //Creating the Frame
        JFrame frame = new JFrame("Cruise Control v.0.0.1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);

        //Creating the MenuBar and adding components
        // JMenuBar mb = new JMenuBar();
        // JMenu m1 = new JMenu("FILE");
        // JMenu m2 = new JMenu("Help");
        // mb.add(m1);
        // mb.add(m2);
        // JMenuItem m11 = new JMenuItem("Open");
        // JMenuItem m22 = new JMenuItem("Save as");
        // m1.add(m11);
        // m1.add(m22);

        JPanel speedNotification = new JPanel();
        JPanel carNotifications = new JPanel();
        JPanel carOperations = new JPanel();
        JPanel cruiseControlOperations = new JPanel();

        //let's create our speed notifications; 
        //JPanel speedPanel = new JPanel();
        JLabel speed = new JLabel("Speed: ");
        JLabel speedDisplay = new JLabel("NULL");

        speedNotification.add(speed);
        speedNotification.add(speedDisplay);

        //lets create our car notifications 
        JLabel identity = new JLabel("Notifications: ");
        JLabel notifications = new JLabel("Hello world!");

        carNotifications.add(identity);
        carNotifications.add(notifications);

        //lets create our car operations 
        JButton accessAdmin = new JButton("Access Admin");
        JButton runSimulation = new JButton("Run Simulation");
        JButton activateCC = new JButton("Activate Cruise Control");
        JButton deactivateCC = new JButton("Deactivate Cruise Control");
        JButton brake = new JButton("Brake");

        activateCruiseControl.addActionListener(new ActionLister){ 
            public void actionPerformed(ActionEvent e){ 
                JFrame ccFrame = new JFrame("Cruise Control Activated");
                ccFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ccFrame.setSize(400, 400);

                JPanel
            }
        }

        carOperations.add(accessAdmin);
        carOperations.add(runSimulation);
        carOperations.add(activateCC);
        carOperations.add(deactivateCC);
        carOperations.add(brake);

        //lets create our cruise control operations 
        JButton setSpeed = new JButton("Set Speed");
        JButton decrementSpeed = new JButton("Decrement Speed");

        cruiseControlOperations.add(setSpeed);
        cruiseControlOperations.add(decrementSpeed);


        //populate the frame
        frame.getContentPane().add(BorderLayout.NORTH, speedNotification);
        frame.getContentPane().add(BorderLayout.EAST, carNotifications);
        frame.getContentPane().add(BorderLayout.SOUTH, cruiseControlOperations);
        frame.getContentPane().add(BorderLayout.CENTER, carOperations);
        frame.setVisible(true);

    }
}