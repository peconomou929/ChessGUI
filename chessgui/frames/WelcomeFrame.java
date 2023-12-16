package chessgui.frames;
import chessgui.ChessGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class WelcomeFrame extends JFrame implements ActionListener
{
    public final static Color BACKGROUND = new Color(240,240,240);
    private int minutes;
    private int seconds;
    private final ImageIcon CHESS_IMAGE = new ImageIcon(
        new ImageIcon("./resources/WelcomePageImage.jpg")
        .getImage().getScaledInstance(300,200,Image.SCALE_DEFAULT)
    );
    
    
    private JTextArea text;
    private JLabel whiteNameLabel;
    private JTextField whiteNameField;
    private JLabel blackNameLabel;
    private JTextField blackNameField;
    private JCheckBox timerEnabledButton, undoEnabledButton;
    private JLabel minutesLabel;
    private JTextField minutesField;
    private JLabel colonLabel;
    private JLabel secondsLabel;
    private JTextField secondsField;
    private JButton goButton, infoButton;
    private JLabel imageLabel;
    
    public WelcomeFrame()
    {

        //format frame
        super("Welcome");
        this.setBounds(300,200,800,400);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());

        //manages the grid bag layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        
        text = new JTextArea("Welcome to the chess application! Enter the names "
                + "of each player, whether the timer should be enabled, and, if so, how much time each player will have. Then press GO.");
            text.setEditable(false);
            text.setLineWrap(true);
            text.setWrapStyleWord(true);
            text.setBackground(BACKGROUND);
            gbc.gridx=0;
            gbc.gridy=0;
            gbc.gridwidth=3;
            gbc.gridheight=1;
            gbc.fill=GridBagConstraints.HORIZONTAL;
            this.add(text,gbc);
        
        whiteNameLabel = new JLabel("Name of WHITE");
            gbc.gridx=0;
            gbc.gridy=1;
            gbc.gridwidth=1;
            gbc.gridheight=1;
            gbc.fill=GridBagConstraints.NONE;
            this.add(whiteNameLabel,gbc);
            
        blackNameLabel = new JLabel("Name of BLACK");
            gbc.gridx=2;
            gbc.gridy=1;
            this.add(blackNameLabel,gbc);
            
        whiteNameField = new JTextField(10);
            gbc.gridx=0;
            gbc.gridy=2;
            this.add(whiteNameField,gbc);
            
        blackNameField = new JTextField(10);
            gbc.gridx=2;
            gbc.gridy=2;
            this.add(blackNameField,gbc);
            
        minutesLabel = new JLabel("Minutes");
            gbc.gridx=0;
            gbc.gridy=3;
            this.add(minutesLabel,gbc);
            
        secondsLabel = new JLabel("Seconds");
            gbc.gridx=2;
            gbc.gridy=3;
            this.add(secondsLabel,gbc);  
            
        minutesField = new JTextField(5);
            gbc.gridx=0;
            gbc.gridy=4;
            minutesField.setEnabled(false);
            this.add(minutesField,gbc);
            
        colonLabel = new JLabel(":");
            gbc.gridx=1;
            gbc.gridy=4;
            this.add(colonLabel,gbc);    
            
        secondsField = new JTextField(5);
            gbc.gridx=2;
            gbc.gridy=4;
            secondsField.setEnabled(false);
            this.add(secondsField,gbc);
         
        imageLabel = new JLabel(CHESS_IMAGE);
            gbc.gridx=3;
            gbc.gridy=0;
            gbc.gridwidth=4;
            gbc.gridheight=4;
            this.add(imageLabel,gbc);
            
        timerEnabledButton = new JCheckBox("Timer Enabled");
            gbc.gridx=3;
            gbc.gridy=4;
            gbc.gridwidth=1;
            gbc.gridheight=1;
            timerEnabledButton.addActionListener(this);
            this.add(timerEnabledButton,gbc);
            
        undoEnabledButton = new JCheckBox("Undo Enabled");
            gbc.gridx=4;
            gbc.gridy=4;
            gbc.gridwidth=1;
            gbc.gridheight=1;
            undoEnabledButton.addActionListener(this);
            this.add(undoEnabledButton,gbc);
            
        infoButton = new JButton("INFO");
            gbc.gridx=5;
            gbc.gridy=4;
            gbc.gridwidth=1;
            gbc.gridheight=1;
            infoButton.addActionListener(this);
            this.add(infoButton,gbc);
        
        goButton = new JButton("GO");
            gbc.gridx=6;
            gbc.gridy=4;
            gbc.gridwidth=1;
            gbc.gridheight=1;
            goButton.addActionListener(this);
            this.add(goButton,gbc);
        
        this.setVisible(true);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        String action = e.getActionCommand();
        if(action.equals("Timer Enabled"))
        {
            undoEnabledButton.setSelected(false);
            if(timerEnabledButton.isSelected())
            {
                minutesField.setEnabled(true);
                minutesField.setText("0");
                secondsField.setEnabled(true);
                secondsField.setText("0");
            }
            else 
            {
                minutesField.setEnabled(false);
                minutesField.setText("");
                secondsField.setEnabled(false);
                secondsField.setText("");
            }
        }
        else if(action.equals("Undo Enabled") )
        {
            minutesField.setEnabled(false);
            minutesField.setText("");
            secondsField.setEnabled(false);
            secondsField.setText("");
            timerEnabledButton.setSelected(false);
        }
        
        else if(action.equals("INFO"))
        {
            new InfoFrame(false, null);
        }
        
        else if(action.equals("GO"))
        {
            if(whiteNameField.getText().isEmpty() || blackNameField.getText().isEmpty())
            {
                new WarningFrame("ERROR: Please make sure you have entered both names.");
            }
            else if(whiteNameField.getText().contains(" ") || blackNameField.getText().contains(" "))
            {
                new WarningFrame("ERROR: Please make sure there are no spaces in the names.");
            }
            else if(whiteNameField.getText().equals(blackNameField.getText()))
            {
                new WarningFrame("ERROR: Please make sure the two names are different.");
            }
            else if(timerEnabledButton.isSelected() && (minutesField.getText().isEmpty() || secondsField.getText().isEmpty()))
            {
                new WarningFrame("ERROR: Please make sure that the time "
                        + "fields have been filled if the timer is enabled.");
            }
            else if(timerEnabledButton.isSelected())
            {
                try
                {
                    minutes = Integer.parseInt(minutesField.getText());
                    seconds = Integer.parseInt(secondsField.getText());
                    if(minutes<0 || minutes>59 || seconds<0 || seconds>59 || (60*minutes+seconds)<10) throw new Exception();
                    this.dispose();
                    new ChessGame(whiteNameField.getText(), blackNameField.getText(), true, (60*minutes+seconds), false);
                }
                catch(Exception exc)
                {
                    new WarningFrame("ERROR: Please make sure that the time "
                            + "fields have integers between 0 and 59, and that the time is at least 10 seconds.");
                }
            }
            else if(undoEnabledButton.isSelected())
            {
                this.dispose();
                 new ChessGame(whiteNameField.getText(), blackNameField.getText(), false, 1, true);
            }
            else
            {
                this.dispose();
                new ChessGame(whiteNameField.getText(), blackNameField.getText(), false, 1,false);
            }
        }
    }
}