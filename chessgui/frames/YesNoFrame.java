package chessgui.frames;
import chessgui.ChessGame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;


public class YesNoFrame extends JFrame implements ActionListener 
{
    private JTextArea text; 
    private JButton yesButton;
    private JButton noButton;
    private int originalGameStatus;
    
    private int frameType;
    private ChessGame game;
    
    public static final int NEW_GAME = 0; 
    public static final int THREEFOLD_REPITITION = 1;
    public static final int QUIT_PROGRAM = 2;
    
    public YesNoFrame(String areYouSureString, int areYouSureType, ChessGame game)
    {
        super("Are you sure?");
        this.setBounds(400,400,400,200);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLayout(new GridLayout(3,1));
        this.setBackground(Color.WHITE);
        
        this.originalGameStatus=game.getGameStatus();
        this.frameType=areYouSureType;
        this.game=game;
        
        this.game.setGameStatus(ChessGame.PAUSED);
        game.getPauseButton().setEnabled(false);
        game.getUndoButton().setEnabled(false);
        game.getNewGameButton().setEnabled(false);
        
        text = new JTextArea(areYouSureString);
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setBackground(new Color(240,240,240));
        this.add(text);
        
        yesButton = new JButton("YES");
        yesButton.addActionListener(this);
        noButton = new JButton("NO");
        noButton.addActionListener(this);
        this.add(yesButton);
        this.add(noButton);
        
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("YES"))
        {
            if(frameType==NEW_GAME)
            {
                game.dispose();
                this.dispose();
                new WelcomeFrame();
            }
            else if(frameType==THREEFOLD_REPITITION)
            {
                game.setCheckStatus(ChessGame.STALEMATE);
                game.getMessageTextArea().setText("The game has ended in stalemate by threefold repition.");
                this.dispose();
            }
            else if(frameType==QUIT_PROGRAM)
            {
                System.exit(0);
            }
        }
        else if (e.getActionCommand().equals("NO"))
        {
            game.getPauseButton().setEnabled(true);
            game.getUndoButton().setEnabled(true);
            game.getNewGameButton().setEnabled(true);
            this.dispose();
            game.setGameStatus(originalGameStatus);
        }
    }
}
