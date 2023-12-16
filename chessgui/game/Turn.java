package chessgui.game;
/*
Handles all information relating to the turn and turn display.
*/

import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Timer;
import java.util.TimerTask;

public class Turn 
{
    public static final int ONE_SECOND=1000; //milliseconds
    public static final Color LIGHT_GRAY = new Color(200,200,200);
    private Timer timerObj = new Timer();
    
    private ChessGame game;                         //game to which this object belongs
    private ChessPlayer playerOnTurn;               //who's turn it is
    private final JPanel turnDisplayPanel;          //holds the turn display
        private final JPanel blackTimeBarPanel;     //indicator of whose turn, as well as how much time left
        private final JPanel blackTurnPanel;        //contains name of player and time left
            private final JLabel blackTurnLabel;    //says "black"
            private JLabel blackTimeLabel;          //if clock is enabled, shows how much time white has
        private final JPanel whiteTimeBarPanel;     //indicator of whose turn, as well as how much time left
        private final JPanel whiteTurnPanel;        //contains name of player and time left
            private final JLabel whiteTurnLabel;    //says "white"
            private JLabel whiteTimeLabel;          //if clock is enabled, shows how much time black has
    
    private TimerTask taskObj = new TimerTask()
    {
        //task that runs every second
        public void run()
        {
            //only when the game is playing
            if(game.getGameStatus()==ChessGame.PLAYING)
            {
                //take a second of the time remaining of the player with the turn and update time display
                playerOnTurn.setSecondsRemaining(playerOnTurn.getSecondsRemaining()-1);
                updateTimeDisplay();
                
                //if the time is up for a particular player, end the game
                if(playerOnTurn.getSecondsRemaining()==0)
                {
                    game.setGameStatus(ChessGame.ENDED);
                    game.displayWarning("TIME'S UP");
                    game.getMessageTextArea().setText(playerOnTurn.getName() + 
                            " has run out of time! " + playerOnTurn.getOpponent().getName() + " wins!");
                }
            }
        }
    };
    
    public ChessPlayer getPlayerOnTurn() {
        return playerOnTurn;
    }

    public Turn(ChessGame game)
    {
        //set instance variables
        this.game=game;
        this.playerOnTurn=game.playerWhite;
        
        //make GridGagConstraints object to handle layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(ChessGame.THIN_MARGIN,ChessGame.THIN_MARGIN,ChessGame.THIN_MARGIN,ChessGame.THIN_MARGIN);

        //make turn display and position on the chess game frame
        turnDisplayPanel = new JPanel(null);
        turnDisplayPanel.setBounds(game.getWarningPanel().getX(), game.getWarningPanel().getY()+game.getWarningPanel().getHeight()+ChessGame.MARGIN, game.getWarningPanel().getWidth(), ChessGame.SQUARE_DIMENSION*3/2);
        turnDisplayPanel.setBackground(Color.WHITE);
        
            blackTimeBarPanel = new JPanel(null);
            blackTimeBarPanel.setBounds(0,0,turnDisplayPanel.getWidth(), turnDisplayPanel.getHeight()/6);
            blackTimeBarPanel.setBackground(game.clockEnabled? LIGHT_GRAY: Color.WHITE);
            turnDisplayPanel.add(blackTimeBarPanel);
        
            blackTurnPanel = new JPanel(new GridBagLayout());
            blackTurnPanel.setBounds(0, blackTimeBarPanel.getHeight(), turnDisplayPanel.getWidth(), turnDisplayPanel.getHeight()/3);
            blackTurnPanel.setBackground(Color.WHITE);
                blackTurnLabel = new JLabel("Black");
                blackTurnLabel.setForeground(LIGHT_GRAY);
                blackTurnLabel.setFont(ChessGame.LARGE_TEXT_FONT);
            blackTurnPanel.add(blackTurnLabel, gbc);
                blackTimeLabel = new JLabel(this.timeToString(game.getPlayerBlack().getSecondsRemaining()));
                blackTimeLabel.setForeground(LIGHT_GRAY);
                blackTimeLabel.setFont(ChessGame.LARGE_TEXT_FONT);
            if(game.isClockEnabled()) blackTurnPanel.add(blackTimeLabel, gbc);
            turnDisplayPanel.add(blackTurnPanel);
            
            whiteTurnPanel = new JPanel(new GridBagLayout());
            whiteTurnPanel.setBounds(0, blackTurnPanel.getHeight() + blackTurnPanel.getY(), turnDisplayPanel.getWidth(), turnDisplayPanel.getHeight()/3);
            whiteTurnPanel.setBackground(Color.WHITE);
                whiteTurnLabel = new JLabel("White");
                whiteTurnLabel.setForeground(LIGHT_GRAY);
                whiteTurnLabel.setFont(ChessGame.LARGE_TEXT_FONT);
            whiteTurnPanel.add(whiteTurnLabel, gbc);
                whiteTimeLabel = new JLabel(this.timeToString(game.getPlayerWhite().getSecondsRemaining()));
                whiteTimeLabel.setForeground(LIGHT_GRAY);
                whiteTimeLabel.setFont(ChessGame.LARGE_TEXT_FONT);
            if(game.isClockEnabled()) whiteTurnPanel.add(whiteTimeLabel, gbc);
            turnDisplayPanel.add(whiteTurnPanel);
            
            whiteTimeBarPanel = new JPanel(new GridBagLayout());
            whiteTimeBarPanel.setBounds(0,whiteTurnPanel.getHeight() + whiteTurnPanel.getY(),turnDisplayPanel.getWidth(), turnDisplayPanel.getHeight()/6);
            whiteTimeBarPanel.setBackground(game.clockEnabled? LIGHT_GRAY: Color.WHITE);
            turnDisplayPanel.add(whiteTimeBarPanel);
            
        //add this turn diplay to the game
        this.game.add(turnDisplayPanel);
    }

    public void setTurn(ChessPlayer player) 
    {
        this.playerOnTurn=player;
        updateTimeDisplay();
    }

    public JPanel getTurnDisplayPanel() {
        return turnDisplayPanel;
    }

    public JPanel getWhiteTurnPanel() {
        return whiteTurnPanel;
    }

    public JLabel getWhiteTurnLabel() {
        return whiteTurnLabel;
    }

    public JPanel getBlackTurnPanel() {
        return blackTurnPanel;
    }

    public JLabel getBlackTurnLabel() {
        return blackTurnLabel;
    }
    
    public String timeToString(int time)
    {
        //converts a time in seconds to a string 
        int minutes = time/60;
        int seconds = time%60;
        return "" + minutes + ":" + (seconds<10? "0":"") + seconds;
    }
    
    public void updateTimeDisplay()
    {
        //this method gets called in three occasions: when the timer begins to run (when the user presses START), 
        //after the timer task runs every second, and when the turn changes
        
        //sets the color of the time bar panel, based on how many seconds left (and whether clock is enabled)
        whiteTimeBarPanel.setBackground(this.game.playerWhite.getTimeBarColor());
        blackTimeBarPanel.setBackground(this.game.playerBlack.getTimeBarColor());
        
        //sets length of time bar panel, based on number of seconds left (full length if clock disabled)
        whiteTimeBarPanel.setBounds(whiteTimeBarPanel.getX(),whiteTimeBarPanel.getY(),this.game.playerWhite.getTimeBarLength(),whiteTimeBarPanel.getHeight());
        blackTimeBarPanel.setBounds(blackTimeBarPanel.getX(),blackTimeBarPanel.getY(),this.game.playerBlack.getTimeBarLength(),blackTimeBarPanel.getHeight());
        
        //sets the time display based on seconds remaining (doesn't show up if clock disabled)
        whiteTimeLabel.setText(timeToString(game.playerWhite.getSecondsRemaining()));
        blackTimeLabel.setText(timeToString(game.playerBlack.getSecondsRemaining()));

        //sets color of text based on whose turn it is
        whiteTimeLabel.setForeground(playerOnTurn==game.playerWhite? Color.BLACK:LIGHT_GRAY);
        whiteTurnLabel.setForeground(playerOnTurn==game.playerWhite? Color.BLACK:LIGHT_GRAY);
        blackTimeLabel.setForeground(playerOnTurn==game.playerBlack? Color.BLACK:LIGHT_GRAY);
        blackTurnLabel.setForeground(playerOnTurn==game.playerBlack? Color.BLACK:LIGHT_GRAY);
    }
    
    public void start()
    {
        //called when the user makes his first move. If clock enabled, begins to run task every second 
        if(game.clockEnabled) this.timerObj.scheduleAtFixedRate(this.taskObj, ONE_SECOND, ONE_SECOND);
        updateTimeDisplay();
    }
}
