package chessgui.frame;
/*
A frame holding help information for the user.
*/
import chessgui.game.ChessGame;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;



public class InfoFrame extends JFrame implements MouseListener
{
    
    //the frame consists of tabs on the left with different topics the user can select
    //each tab has information laid out in multiple sections, each occupying one textarea
    
    public final static int INFO_FRAME_WIDTH=1100;
    public final static int INFO_FRAME_HEIGHT=500;
    public final static Color LIGHT_GRAY = new Color(230,230,230);
    
    //maximum number of text areas that the information in a tab can occupy
    public int numberOfTextAreas=7;

    //an image of king
    private final ImageIcon IMAGE = new ImageIcon
        (new ImageIcon("./resources/InfoPageImage.jpg").getImage().getScaledInstance(INFO_FRAME_WIDTH/5,INFO_FRAME_HEIGHT*5/6,Image.SCALE_DEFAULT));
    
    JPanel tabPanel;            //panel that will hold all the tabs
    JPanel textPanel;           //panel that will hold all the text
    InfoTab[] tabs;             //array of tabs (each tab is a JPanel)
    String[] tabLabels;         //an array of strings representing the label on each tab
    JTextArea[] textAreas;      //an array of text areas that will hold the current tab's information
    String[][] tabContents;     //Each tab has a string array with it's information. This is an array of these arrays.
    int originalGameStatus;     //the game status when this dialogue box was opened
    JButton returnButton;       //a button which returns to the game and restores the original game status
    boolean duringGame;         //was this frame even opened during the game
    ChessGame game;             //if so, which game
    
    public InfoFrame(boolean duringGame, ChessGame game) 
    {
        //format frame
        super("Info");
        this.setBounds(ChessGame.SCREEN_WIDTH/2-INFO_FRAME_WIDTH/2,ChessGame.SCREEN_HEIGHT/2-INFO_FRAME_HEIGHT/2,INFO_FRAME_WIDTH,INFO_FRAME_HEIGHT);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLayout(new GridLayout(1,4));
        
        //assign attributes
        this.duringGame=duringGame;
        this.game=game;
        if(duringGame)
        {
            this.originalGameStatus=game.getGameStatus();
            game.setGameStatus(ChessGame.DIALOGUE_BOX);
        }
        
        //the labels for each of the tabs
        tabLabels = new String[]{"About", "Undo feature", "Timer feature", 
            "Message panel", "Warning panel", "Turn display", "Move table", "Scoring", "Buttons", "Captured pieces", "Moving pieces", "Algebraic Notation"};
        
        //an array of string arrays. Each string array corresponds to the information in a tab.
        //each array of strings is in the index that corresponds to the index of the tab label
        tabContents = new String[][] {
            {"This program simulates the game of chess for two players. It includes:",
                "(1) Recognition of legal moves, check, checkmate, stalemate, promotions, en passant and castling.",
                "(2) The option to include a timer.",
                "(3) The ability to undo moves if the timer is not included,",
                "(4) Easy-to-use interface for moving pieces and displaying the game."
            },
            {"If the undo feature is enabled, a button labeled \'UNDO\' will appear.",
                "Clicking this button will undo the most recent move, no matter what it was.",
                "In a formal games of chess, this is not allowed; however, in a friendly game, you may wish to use it."
            },
            {"If the timer is enabled, each player will be given the desired amount of time.",
                "This is not the time required to complete each single move, but rather all moves in total.",
                "The turn display will indicate how much time is left.",
                "If a player runs out of time, the opposite player automatically wins."
            },
            {"The message panel is in the top left corner of the window.",
                "It will explain to the next player the move that just occurred.",
                "Red highlights on the board also indicate what move was just played."
            },
            {"The warning panel is located just below the message panel.",
                "The warning panel displays messages such as IN CHECK, STALEMATE, CHECKMATE, and TIMES UP.",
            },
            {"The turn display is located below the warning panel.",
                "The turn display indicates whose turn it is, and, if the timer is enabled, how much time each player has."
            },
            {"The move table is located below the turn display.",
                "Its primary purpose is to hold a record of all the moves that have been played.",
                "The moves are recorded in algebraic notation.",
                "The move table will also indicate the score of each player."
            },
            {"The score of each player is determined by the value of the pieces remaining on the board.",
                "These values are based on the chess piece relative value system.",
                "Pawn is worth 1. Knight and Bishop are worth 3. Rook is worth 5. Queen is worth 8.",
                "In the case that a game cannot be finished, the score is a good indicator of who is winning."
            },
            {"Below the move table are the buttons.",
                "There are four different buttons that could appear.",
                "The button labeld INFO will open up this window.",
                "The button labeled NEW GAME can be clicked to delete the current game and begin a new one.",
                "If the timer is enabled, there will be a button labeled PAUSE/CONTINUE, which will control the time.",
                "If the undo feature is enabled, there will be a button labeled UNDO." 
            },
            {"On the far right of the window, there is a panel of captured pieces.",
                "They are separated by color, but appear in the order that they were captured.",
                "Captured pieces are recovered when a capture move is undone."
            },
            {"First, click the piece which you wish to move.",
                "Then click any one of the highlighted squares to move there.",
                "To cancel your piece selection, click anywhere else on the board." 
            },
            {
                "Algebraic notation is a way of recording moves in a chess game, and is explained here briefly.",
                "The first letter represents the piece moving (unless it is a pawn).",
                "The file or rank of origin can be included after if there is ambiguity.",
                "In the case of a pawn capturing, the file of origin is included.",
                "The symbol \'x\' indicates a capture.",
                "At the end is the file and rank of the destination square.",
                "There is special notation for castles, promotions, and en passants."
            }
        };
        
        //the panel that holds all the tabs
        JPanel tabPanel = new JPanel(new GridLayout(tabLabels.length,1));
        this.add(tabPanel);
        //a blank panel that will be added next
        JPanel blank1 = new JPanel();
        blank1.setBackground(Color.WHITE);
        this.add(blank1);
        //a panel that will hold the information for each tab
        JPanel textPanel = new JPanel(new GridLayout(numberOfTextAreas+2, 1));
        this.add(textPanel);
        //a panel that will hold the image
        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.add(new JLabel(IMAGE));
        imagePanel.setBackground(Color.WHITE);
        this.add(imagePanel);
        
        //an array that will hold reference to each tab
        tabs = new InfoTab[tabLabels.length];
        //successively add each tab to the panel
        for(int i=0; i<tabLabels.length; i++)
        {
            //each tab receives a label and the an array of strings which holds its information
            tabs[i] = new InfoTab(tabLabels[i], tabContents[i]);
            tabPanel.add(tabs[i]);
            tabs[i].addMouseListener(this);
        }
        
        //a blank panel is placed at the top of the text panel as a margin
        JPanel blank = new JPanel();
        blank.setBackground(Color.WHITE);
        textPanel.add(blank);
        
        //an array of text areas that will hold information for each tab
        textAreas = new JTextArea[numberOfTextAreas];
        //successively add each text area to frame
        for(int i=0; i<numberOfTextAreas; i++)
        {
           
            //text area initially blank
            JTextArea t = new JTextArea("");
            t.setForeground(Color.BLACK);
            t.setEditable(false);
            t.setLineWrap(true);
            t.setWrapStyleWord(true);
            
            //add text area to frame
            textPanel.add(t);
            
            //store reference to text area in array
            textAreas[i]=t;
        }
            
        //at the bottom of the text panel, add a button panel with a return button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        returnButton = new JButton(this.duringGame? "CONTINUE PLAYING":"RETURN");
        returnButton.addMouseListener(this);
        buttonPanel.add(returnButton);
        textPanel.add(buttonPanel);
    
        this.setVisible(true);
        
        //selects a tab to initially display
        selectTab(tabs[0]);
    }
    
    public void selectTab(InfoTab tab)
    {
        //when the user clicks on a tab
        
        //goes through all tabs in the array, makes them gray
        for(InfoTab t: tabs)
        {
            t.setBackground(LIGHT_GRAY);
        }
        //makes the selected tab white
        tab.setBackground(Color.WHITE);
        
        //goes through all the text areas of the window
        for(int i=0; i<textAreas.length; i++)
        {
            //if this tab has more text to add, add it
            if(i<tab.textAreaContent.length)
            {
               textAreas[i].setText(tab.textAreaContent[i]);
            }
            //otherwise set the text as blank
            else 
            {
               textAreas[i].setText("");
            }
        }
    }
    
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
        //goes trough all tabs to determine if any was clicked
        for(InfoTab t: tabs)
        {
            if(e.getComponent()==t)
            {
                //if it was, selects that tab
                selectTab(t);
            }
        }
        
        //if the return button was clicked
        if(e.getComponent() == returnButton)
        {
            //if it was during a game
            if(duringGame)
            {
                //restore the original game status
                game.setGameStatus(this.originalGameStatus);
            }
            //always dispose this frame
            this.dispose();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}