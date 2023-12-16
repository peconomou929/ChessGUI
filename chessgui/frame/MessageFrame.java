package chessgui.frame;
/*
This class is currently not used at all in the program.
I'm keeping it just in case I might want it.
It is for whenever a message needs to be displayed during the game.
The game is automatically paused when it pops up. 
When the user presses return, the game continues playing
*/
import chessgui.game.ChessGame;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;


public class MessageFrame extends JFrame implements ActionListener{
   
    JTextArea text;  
    JButton returnButton;
    ChessGame game;
    int originalGameStatus;

    public MessageFrame(String message, ChessGame game)
    {
        super("Message");
        this.setBounds(400,400,400,200);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);
        this.game=game;
        this.originalGameStatus=game.getGameStatus();
        this.game.setGameStatus(ChessGame.DIALOGUE_BOX);
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        text = new JTextArea(message);
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setBackground(new Color(240,240,240));
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridheight=1;
        gbc.gridheight=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        this.add(text, gbc);
        
        returnButton = new JButton("RETURN");
        returnButton.addActionListener(this);
        gbc.gridy=1;
        this.add(returnButton, gbc);
        
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        String action = e.getActionCommand();
        if(action.equals("RETURN"))
        {
            this.dispose();
            this.game.setGameStatus(originalGameStatus);
        }
    }
}
