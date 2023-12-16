package chessgui;
/*
This class represents the game itself. It is the central class of the program, so to speak. 
Most attributes of a chess game instance have references back to the chess game object itself.
This class therefore acts like the center of an octupus. 
From any of the numerous legs, I can return to the center and visit any other leg.
*/

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import chessgui.frame.InfoFrame;
import chessgui.frame.YesNoFrame;

public class ChessGame extends JFrame implements ActionListener, MouseListener {
    
    //useful integers to hold onto
    public static final int SCREEN_WIDTH=1440;
    public static final int SCREEN_HEIGHT=832;
    public static final int SQUARE_DIMENSION = 85;
    public static final int MARGIN = SQUARE_DIMENSION/3;
    public static final int THIN_MARGIN = 6;
    public static final int BORDER_THICKNESS=2;
    
    //different values for the variable gameStatus
    public static final int NOT_STARTED=0;      //before any move has been played
    public static final int PLAYING=1;          //during gameplay, whenever game is not paused
    public static final int PAUSED=2;           //when user clicks on pause
    public static final int ENDED=3;            //either checkmate or stalemate, or time over
    public static final int DIALOGUE_BOX=4;     //when dialogue box pops up, this automatically pauses time and disables other buttons
    
    //different values for the variable checkStatus
    public static final int NO_CHECK=0;
    public static final int IN_CHECK=1;
    public static final int CHECKMATE=2;
    public static final int STALEMATE=3;
    
    //font for the warning panel, turn display, and file/rank labels on the board
    public static final Font LARGE_TEXT_FONT = new Font("arial", Font.BOLD, 20);
    
    //attributes of the chess game
    ChessPlayer playerWhite, playerBlack;  
    ChessPiece[] chessPieces;
    ArrayList<BoardPosition> boardHistory;
    ArrayList<Move> moveHistory;
    JPanel chessBoardOuterPanel;
        JPanel chessBoardInnerPanel;
            ChessSquare[][] gameBoard;
    JPanel messagePanel;
        JTextArea messageTextArea;
    JPanel warningPanel;
        JLabel warningLabel;
    JPanel bottomBoundaryPanel, topBoundaryPanel, rightBoundaryPanel, leftBoundaryPanel;
    JPanel buttonPanel;
        JButton pauseButton, newGameButton, undoButton, infoButton;
    DefaultTableModel moveRecordsModel;
        JTable moveRecordsTable;
        JScrollPane moveRecordsScroll;
    Turn turnObj;
    Taken takenObj;
    Selection selectionObj; 
    EnPassant enPassantObj;
    boolean clockEnabled, undoEnabled;
    int gameStatus;
    int secondsToPlay;
    
    //mutators and accessors
    public JButton getNewGameButton() {
        return newGameButton;
    }
    public JButton getUndoButton() {
        return undoButton;
    }
    public JButton getPauseButton() {
        return pauseButton;
    }
    public int getGameStatus() {
        return gameStatus;
    }
    public void setGameStatus(int gameStatus){
        
        this.gameStatus=gameStatus;
        
        //first enable all buttons by default
        pauseButton.setEnabled(true);
        undoButton.setEnabled(true);
        newGameButton.setEnabled(true);
        infoButton.setEnabled(true);
        
        //depending on the game status, perform different actions
        switch(gameStatus) {
            case PAUSED: 
                pauseButton.setText("CONTINUE");
                selectionObj.clearSelection();
                break;
            case PLAYING: 
                pauseButton.setText("PAUSE");
                break;
            case NOT_STARTED: 
                undoButton.setEnabled(false);
                pauseButton.setEnabled(false);
                messageTextArea.setText(playerWhite.getName() + ", when you're ready, make your move!");
                break;
            case DIALOGUE_BOX:
                pauseButton.setEnabled(false);
                undoButton.setEnabled(false);
                newGameButton.setEnabled(false);
                infoButton.setEnabled(false);
                selectionObj.clearSelection();
                break;
            case ENDED:
                pauseButton.setEnabled(false);
        }
    }
    public void setCheckStatus(int checkStatus) {
        
        //add a message to the warning panel if necessary
        switch (checkStatus) {
            case NO_CHECK:
                warningPanel.setBackground(Color.WHITE);
                break;
            case IN_CHECK:
                warningPanel.setBackground(Color.RED);
                warningLabel.setText("IN CHECK");
                break;
            case CHECKMATE:
                warningPanel.setBackground(Color.RED);
                warningLabel.setText("CHECKMATE");
                this.setGameStatus(ENDED);
                break;
            case STALEMATE:
                warningPanel.setBackground(Color.RED);
                warningLabel.setText("STALEMATE");
                this.setGameStatus(ENDED);
                break;
            default:
                break;
        }
    }
    public ArrayList<Move> getMoveHistory(){
        return moveHistory;
    }
    public ArrayList<BoardPosition> getBoardHistory() {
        return boardHistory;
    }
    public JPanel getWarningPanel() {
        return warningPanel;
    }
    public JLabel getWarningLabel() {
        return warningLabel;
    }
    public JPanel getMessagePanel() {
        return messagePanel;
    }
    public JTextArea getMessageTextArea() {
        return messageTextArea;
    }
    public void setMessageTextArea(JTextArea commandTextArea) {
        this.messageTextArea = commandTextArea;
    }
    public int getSecondsToPlay() {
        return secondsToPlay;
    }
    public boolean isClockEnabled() {
        return clockEnabled;
    }
    public ChessPlayer getPlayerWhite() {
        return playerWhite;
    }
    public ChessPlayer getPlayerBlack() {
        return playerBlack;
    }
    public ChessPiece[] getChessPieces() {
        return chessPieces;
    }
    public ChessSquare[][] getGameBoard() {
        return gameBoard;
    }
    public Turn getTurnObj() {
        return turnObj;
    }
    public EnPassant getEnPassantObj() {
        return enPassantObj;
    }
    public Taken getTakenObj() {
        return takenObj;
    }
    
    public ChessGame(String whiteName, String blackName, boolean clockEnabled, int secondsToPlay, boolean undoEnabled)
    {
        //format frame
        super("Chess");
        this.setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        
        //assign attributes
        this.secondsToPlay=secondsToPlay;
        this.clockEnabled=clockEnabled;
        this.undoEnabled=undoEnabled;
        this.boardHistory = new ArrayList<>();
        this.moveHistory = new ArrayList<>();
        
        //panel that holds the chess board
        chessBoardOuterPanel = new JPanel(null);
        chessBoardOuterPanel.setBounds(this.getWidth()-3*MARGIN-12*SQUARE_DIMENSION,2*MARGIN,8*SQUARE_DIMENSION+2*BORDER_THICKNESS,8*SQUARE_DIMENSION+2*BORDER_THICKNESS);
        chessBoardOuterPanel.setBackground(Color.BLACK);
            chessBoardInnerPanel = new JPanel(new GridLayout(8,8));
            chessBoardInnerPanel.setBounds(BORDER_THICKNESS,BORDER_THICKNESS,8*SQUARE_DIMENSION,8*SQUARE_DIMENSION);
            chessBoardInnerPanel.setBackground(Color.WHITE);
            chessBoardOuterPanel.add(chessBoardInnerPanel);
        this.add(chessBoardOuterPanel);
        
        //panels that hold the rank and file labels
        bottomBoundaryPanel = new JPanel(new GridLayout(1,8));
            bottomBoundaryPanel.setBounds(chessBoardOuterPanel.getX(), chessBoardOuterPanel.getY() + chessBoardOuterPanel.getHeight(), chessBoardOuterPanel.getWidth(), MARGIN);
            this.add(bottomBoundaryPanel);
        topBoundaryPanel = new JPanel(new GridLayout(1,8));
            topBoundaryPanel.setBounds(chessBoardOuterPanel.getX(), MARGIN, chessBoardOuterPanel.getWidth(), MARGIN);
            this.add(topBoundaryPanel);
        rightBoundaryPanel = new JPanel(new GridLayout(8,1));
            rightBoundaryPanel.setBounds(chessBoardOuterPanel.getX()+chessBoardOuterPanel.getWidth(), chessBoardOuterPanel.getY(), MARGIN, chessBoardOuterPanel.getHeight());
            this.add(rightBoundaryPanel);
        leftBoundaryPanel = new JPanel(new GridLayout(8,1));
            leftBoundaryPanel.setBounds(chessBoardOuterPanel.getX()-MARGIN, chessBoardOuterPanel.getY(), MARGIN, chessBoardOuterPanel.getHeight());
            this.add(leftBoundaryPanel);
        
        //fills in all the rank and file labels
        for(int i=0; i<=7; i++)
        {
            JPanel p1 = new JPanel(new GridBagLayout());
            JLabel l1 = new JLabel("" + fileConvert(i));
            l1.setFont(LARGE_TEXT_FONT);
            p1.add(l1);
            bottomBoundaryPanel.add(p1);
            
            JPanel p2 = new JPanel(new GridBagLayout());
            JLabel l2 = new JLabel("" + fileConvert(i));
            l2.setFont(LARGE_TEXT_FONT);
            p2.add(l2);
            topBoundaryPanel.add(p2);
            
            JPanel p3 = new JPanel(new GridBagLayout());
            JLabel l3 = new JLabel((8-i) + "");
            l3.setFont(LARGE_TEXT_FONT);
            p3.add(l3);
            leftBoundaryPanel.add(p3);
            
            JPanel p4 = new JPanel(new GridBagLayout());
            JLabel l4 = new JLabel((8-i) + "");
            l4.setFont(LARGE_TEXT_FONT);
            p4.add(l4);
            rightBoundaryPanel.add(p4);            
        }
        
        //message panel that is based on the previous move, informing the next player of what happened
        messagePanel = new JPanel(null);
        messagePanel.setBounds(MARGIN, chessBoardOuterPanel.getY(), leftBoundaryPanel.getX()-2*MARGIN, SQUARE_DIMENSION);
        messagePanel.setBackground(Color.WHITE);
            messageTextArea = new JTextArea("");
            messageTextArea.setBounds(THIN_MARGIN, THIN_MARGIN, messagePanel.getWidth()-2*THIN_MARGIN,messagePanel.getHeight()-2*THIN_MARGIN);
            messageTextArea.setEditable(false);
            messageTextArea.setLineWrap(true);
            messageTextArea.setWrapStyleWord(true);
        messagePanel.add(messageTextArea);
        this.add(messagePanel);
        
        //add a warning panel which will show simple warning messages such as IN CHECK, TIMES UP, CHECKMATE, and STALEMATE
        warningPanel = new JPanel(new GridBagLayout());
        warningPanel.setBounds(messagePanel.getX(), messagePanel.getY() + messagePanel.getHeight() + MARGIN, messagePanel.getWidth(), 2*SQUARE_DIMENSION - messagePanel.getHeight() - MARGIN);
        warningPanel.setBackground(Color.WHITE);
            warningLabel = new JLabel("");
            warningLabel.setForeground(Color.WHITE);
            warningLabel.setFont(LARGE_TEXT_FONT);
        warningPanel.add(warningLabel);
        this.add(warningPanel);
        
        //panel that holds the following buttons: (1) to start a new game, (2) to pause/continue playing (only when timer is enabled), 
        //and (3) to undo a move (only when undo is enabled), (4) info button for help
        buttonPanel = new JPanel(new GridLayout(3,1));
        buttonPanel.setBounds(MARGIN, chessBoardOuterPanel.getY() + 13*SQUARE_DIMENSION/2, messagePanel.getWidth(), 3*SQUARE_DIMENSION/2);
            newGameButton = new JButton("NEW GAME");
            infoButton = new JButton("INFO");
            pauseButton = new JButton("PAUSE");
            undoButton = new JButton("UNDO");
            
            infoButton.addActionListener(this);
            newGameButton.addActionListener(this);
            pauseButton.addActionListener(this);
            undoButton.addActionListener(this);
            
            buttonPanel.add(newGameButton);
            buttonPanel.add(infoButton);
            if(clockEnabled) buttonPanel.add(pauseButton);
            if(undoEnabled) buttonPanel.add(undoButton);
        this.add(buttonPanel);
        
        //an array of chess squares that makes up the "gameboard"
        gameBoard = new ChessSquare[8][8];
        
        //starts at top left corner, moves right to begin
        for(int rank=7; rank>=0; rank--)
        {
            for(int file=0; file<=7; file++)
            {
                
                JPanel innerPanel = new JPanel(null);   //holds chess pieces
                JPanel outerPanel = new JPanel(null);   //acts as a border, and holds the inner panel
                
                innerPanel.setBounds(BORDER_THICKNESS,BORDER_THICKNESS,SQUARE_DIMENSION-2*BORDER_THICKNESS, SQUARE_DIMENSION-2*BORDER_THICKNESS);
                outerPanel.add(innerPanel);
                
                //constructs a new chess square object to fill each spot in the 2D array
                //each chess square has its inner and outer panels as attributes
                gameBoard[file][rank] = new ChessSquare(outerPanel, innerPanel, file, rank, this);
               
                //adds the outer panel (which carries the inner panel too) to the game board
                chessBoardInnerPanel.add(outerPanel);
                innerPanel.addMouseListener(this);
            }  
        }
        
        //construct both players (String name, boolean white, ChessGame game, int secondsRemaining)
        playerWhite = new ChessPlayer(whiteName, true, this, secondsToPlay);
        playerBlack = new ChessPlayer(blackName, false, this, secondsToPlay);
        playerWhite.setOpponent(playerBlack);

        //make objects of the chess game 
        turnObj = new Turn(this);                   //sets up and manages turn display/manages timer, knows whose turn it is
        takenObj = new Taken(this);                 //sets up display for taken pieces, has methods for adding and recovering taken pieces
        enPassantObj = new EnPassant(this);         //holds values relevant to setting up en passant
        selectionObj = new Selection(this);         //holds values relevant to the selection of a piece when the user clicks on it
        
        //adding new ChessPiece objects to an array of chess pieces, using ChessPiece(player, square, piece type)
        //chess pieces are preserved, and so is their color, though they may change type (during promotions), or may be captured
        chessPieces = new ChessPiece[32];
        chessPieces[0] = new ChessPiece(playerWhite, gameBoard[0][0], ChessPiece.ROOK);
        chessPieces[1] = new ChessPiece(playerWhite, gameBoard[1][0], ChessPiece.KNIGHT);
        chessPieces[2] = new ChessPiece(playerWhite, gameBoard[2][0], ChessPiece.BISHOP);
        chessPieces[3] = new ChessPiece(playerWhite, gameBoard[3][0], ChessPiece.QUEEN);
        chessPieces[4] = new ChessPiece(playerWhite, gameBoard[4][0], ChessPiece.KING);
        chessPieces[5] = new ChessPiece(playerWhite, gameBoard[5][0], ChessPiece.BISHOP);
        chessPieces[6] = new ChessPiece(playerWhite, gameBoard[6][0], ChessPiece.KNIGHT);
        chessPieces[7] = new ChessPiece(playerWhite, gameBoard[7][0], ChessPiece.ROOK);
        chessPieces[8] = new ChessPiece(playerWhite, gameBoard[0][1], ChessPiece.PAWN);
        chessPieces[9] = new ChessPiece(playerWhite, gameBoard[1][1], ChessPiece.PAWN);
        chessPieces[10] = new ChessPiece(playerWhite, gameBoard[2][1], ChessPiece.PAWN);
        chessPieces[11] = new ChessPiece(playerWhite, gameBoard[3][1], ChessPiece.PAWN);
        chessPieces[12] = new ChessPiece(playerWhite, gameBoard[4][1], ChessPiece.PAWN);
        chessPieces[13] = new ChessPiece(playerWhite, gameBoard[5][1], ChessPiece.PAWN);
        chessPieces[14] = new ChessPiece(playerWhite, gameBoard[6][1], ChessPiece.PAWN);
        chessPieces[15] = new ChessPiece(playerWhite, gameBoard[7][1], ChessPiece.PAWN);
        chessPieces[16] = new ChessPiece(playerBlack, gameBoard[0][6], ChessPiece.PAWN);
        chessPieces[17] = new ChessPiece(playerBlack, gameBoard[1][6], ChessPiece.PAWN);
        chessPieces[18] = new ChessPiece(playerBlack, gameBoard[2][6], ChessPiece.PAWN);
        chessPieces[19] = new ChessPiece(playerBlack, gameBoard[3][6], ChessPiece.PAWN);
        chessPieces[20] = new ChessPiece(playerBlack, gameBoard[4][6], ChessPiece.PAWN);
        chessPieces[21] = new ChessPiece(playerBlack, gameBoard[5][6], ChessPiece.PAWN);
        chessPieces[22] = new ChessPiece(playerBlack, gameBoard[6][6], ChessPiece.PAWN);
        chessPieces[23] = new ChessPiece(playerBlack, gameBoard[7][6], ChessPiece.PAWN);
        chessPieces[24] = new ChessPiece(playerBlack, gameBoard[0][7], ChessPiece.ROOK);
        chessPieces[25] = new ChessPiece(playerBlack, gameBoard[1][7], ChessPiece.KNIGHT);
        chessPieces[26] = new ChessPiece(playerBlack, gameBoard[2][7], ChessPiece.BISHOP);
        chessPieces[27] = new ChessPiece(playerBlack, gameBoard[3][7], ChessPiece.QUEEN);
        chessPieces[28] = new ChessPiece(playerBlack, gameBoard[4][7], ChessPiece.KING);
        chessPieces[29] = new ChessPiece(playerBlack, gameBoard[5][7], ChessPiece.BISHOP);
        chessPieces[30] = new ChessPiece(playerBlack, gameBoard[6][7], ChessPiece.KNIGHT);
        chessPieces[31] = new ChessPiece(playerBlack, gameBoard[7][7], ChessPiece.ROOK);
        
        //identifies which piece is the king of the player for quick access
        playerWhite.setPlayerKing(chessPieces[4]);
        playerBlack.setPlayerKing(chessPieces[28]); 

        //sets up a table that will display moves in algebraic notation
        moveRecordsModel = new DefaultTableModel(); 
        moveRecordsTable = new JTable(moveRecordsModel);
        moveRecordsTable.setEnabled(false);
        moveRecordsScroll = new JScrollPane(moveRecordsTable);
        moveRecordsScroll.setBounds(MARGIN, turnObj.getTurnDisplayPanel().getY()+turnObj.getTurnDisplayPanel().getHeight()+MARGIN, 
                messagePanel.getWidth(), buttonPanel.getY()-2*MARGIN-turnObj.getTurnDisplayPanel().getY()-turnObj.getTurnDisplayPanel().getHeight());
        this.add(moveRecordsScroll);
        
        //adds columns and rows to begin with
        moveRecordsModel.addColumn(""); 
        moveRecordsModel.addColumn("White"); 
        moveRecordsModel.addColumn("Black"); 
        moveRecordsModel.addRow(new String[]{"Score: ", "" + playerWhite.getScore(), "" + playerBlack.getScore()});
        moveRecordsModel.addRow(new String[]{"","",""});
        
        setGameBoard();
        this.setVisible(true);
    }
    
    public void setGameBoard()
    {
        //based on current board: sets piece scopes, legal moves, and saves board position
        //based on last move: sets en passant, command text, game status, and moves to next player
        this.selectionObj.clearSelection();
        this.enPassantObj.clearEnPassant();
        this.restoreSquareBorders();
        this.setAllScopes();
        this.setAllLegalMoves();

        //if no moves have been made yet
        if(moveHistory.isEmpty())
        {
            this.setGameStatus(NOT_STARTED);
            this.getTurnObj().setTurn(playerWhite);
        }
        //if at least one move has been made
        else
        {
            //if that was the first move
            if(moveHistory.size()==1)
            {
                //initiate the game
                this.setGameStatus(PLAYING);
                this.turnObj.start();
            }
            
            Move lastMove = moveHistory.get(moveHistory.size()-1);
            
            //if the last move allows was pawn moving up two squares
            if(lastMove.pieceType==ChessPiece.PAWN && Math.abs(lastMove.origin.getRank()-lastMove.destination.getRank())==2) 
                enPassantObj.setUpEnPassant(lastMove);
        
            //add messages for the user
            this.setCheckStatus(lastMove.checkResult);
            this.getMessageTextArea().setText(lastMove.messageText);
            //move to next turn
            this.getTurnObj().setTurn(lastMove.pieceMoving.getPlayer().getOpponent());
            
            //highlight in red the origin and destination of the previous move
            lastMove.origin.setCurrentBorder(Color.RED);
            lastMove.destination.setCurrentBorder(Color.RED);
        }
        
        //add the current board to the board history
        this.boardHistory.add(new BoardPosition(this));
        //calculate scores and add to table
        setScoresOnTable();
        //check if this board has been repeated twice before (if so, potential stalemate)
        checkForThreefoldRepition();
        this.repaint();
    }
    
    public void setAllLegalMoves()
    {
        //go through all the chess pieces
        for(ChessPiece p : chessPieces)
        {
            if(p.isInPlay())
            {
                //for each piece, check each square
                for(int i=0; i<=7; i++) 
                {
                    for(int j=0; j<=7; j++) 
                    {
                        p.getLegalMove()[i][j]=false;
                        //check what type of move, and if that type of move is legal
                        if(
                                (Move.isCastleMove(p, gameBoard[i][j]) && CastleMove.isMoveLegal(p, gameBoard[i][j])) ||
                                (Move.isEnPassantMove(p, gameBoard[i][j]) && EnPassantMove.isMoveLegal(p, gameBoard[i][j])) ||
                                (p.getScope()[i][j] && NormalMove.isMoveLegal(p, gameBoard[i][j]))
                                )
                        {
                            //add this square to the legal moves of the chess piece
                            p.getLegalMove()[i][j]=true; 
                        }
                    }
                }
            }
        } 
    }
    
    //sets the scope for all chess pieces
    public void setAllScopes()
    {
        for(ChessPiece p : chessPieces)
        {
            p.setScope();
        }
    }
    
    //adds a message to the warning panel
    public void displayWarning(String message)
    {
        warningPanel.setBackground(Color.RED);
        warningLabel.setText(message);
    }
    
    //adds the given move (which has a "moveString" as an attribute) to the table
    public void addMoveToTable(Move move)
    {
        //depending on the player, add to different column
        if(move.pieceMoving.getPlayer().isWhite())
            this.moveRecordsModel.addRow(new String[]{(this.moveRecordsModel.getRowCount()-1)+".", move.moveString, ""});
        else
            this.moveRecordsModel.setValueAt(move.moveString, this.moveRecordsModel.getRowCount()-1, 2);
    }
    
    //erases last move from the table
    public void removeLastMoveFromTable()
    {
        Move lastMove = moveHistory.get(moveHistory.size()-1);
        
        if(lastMove.pieceMoving.getPlayer().isWhite())
            this.moveRecordsModel.removeRow(moveRecordsModel.getRowCount()-1);
        else
            this.moveRecordsModel.setValueAt("", this.moveRecordsModel.getRowCount()-1, 2);
    }
    
    //updates scores on the table based on value of remaining pieces
    public void setScoresOnTable()
    {
        moveRecordsModel.setValueAt(playerWhite.getScore(), 0, 1);
        moveRecordsModel.setValueAt(playerBlack.getScore(), 0, 2);
    }
    
    //checks to see if the current board position has been repeated at least twice before
    public void checkForThreefoldRepition()
    {
        BoardPosition lastPosition = this.boardHistory.get(this.boardHistory.size()-1);
        int timesRepeated=0;
        
        //goes through all the previous board positions
        for(int i=0; i<boardHistory.size()-1; i++)
            //if this one is identical to that one, count it
            if(lastPosition.isIdenticalTo(boardHistory.get(i))) timesRepeated++;
        
        //if repeated at least twice
        if(timesRepeated>1)
            //offer to accept a stalemate
            new YesNoFrame("There has been threefold repitition. Would you like to accept a stalemate?", YesNoFrame.THREEFOLD_REPITITION, this);
    }
    
    //if any of the buttons are clicked
    public void actionPerformed(ActionEvent e) 
    {
        //if the pause button is clicked, set the game to paused
        if(e.getActionCommand().equals("PAUSE"))
            this.setGameStatus(PAUSED);

        //if the continue button is clicked, set the game to playing
        else if(e.getActionCommand().equals("CONTINUE"))
            this.setGameStatus(PLAYING);

        //if the new game button is clicked, prompt the user with a dialogue box saying "Are you sure?"
        else if(e.getActionCommand().equals("NEW GAME"))
            new YesNoFrame("Are you sure you want to start a new "
                    + "game? The current game will be deleted.", YesNoFrame.NEW_GAME, this);
        
        //if the undo button is clicked, grab the last move that was played and undo it
        else if(e.getActionCommand().equals("UNDO"))
            moveHistory.get(moveHistory.size()-1).undo();
        
        //if the info button is clicked, open the info frame
        else if(e.getActionCommand().equals("INFO"))
            new InfoFrame(true, this);
    }
    
    //if any of the chess squares are clicked
    public void mouseClicked(MouseEvent e) {
        
        //don't respond to the click if the game is paused, ended, or there is a dialogue box showing
        if(this.gameStatus==PAUSED || this.gameStatus==ENDED || this.gameStatus==DIALOGUE_BOX) return;
        
        //the square that was just clicked
        ChessSquare clickedSquare = null;
        
        //goes through all the chess square to see which one was clicked
        for(int i=0; i<gameBoard.length; i++)
            for(int j=0; j<gameBoard.length; j++)
                if(e.getComponent()==gameBoard[i][j].getInnerPanel())
                    clickedSquare=gameBoard[i][j];
        
        //if there is a piece on the square that belongs to the player who currently has the turn
        if(clickedSquare.isOccupied() 
                && clickedSquare.getPiece().isWhite()==turnObj.getPlayerOnTurn().isWhite() 
                && (!selectionObj.isSomethingSelected() || selectionObj.getPiece()!=clickedSquare.getPiece()))
        {
            //select that piece
            ChessPiece selectedPiece = clickedSquare.getPiece();
            selectionObj.selectPiece(selectedPiece);
                
            //go through all the chess squares on the board and highlight the ones to which the piece can move
            for(int i=0; i<gameBoard.length; i++)
                for(int j=0; j<gameBoard.length; j++)
                    if(selectedPiece.getLegalMove()[i][j] || gameBoard[i][j]==selectedPiece.getSquare())
                    {
                        ChessSquare s = gameBoard[i][j];
                        s.setCurrentColor(mixColors(Color.WHITE, Color.YELLOW, 0.5));
                    }
        }
        //if a piece has already been selected and the player is trying to move
        else if(selectionObj.isSomethingSelected())
        {
            ChessPiece selectedPiece = selectionObj.getPiece();
            
            //if the chess square which he has selected is within the legal moves
            if(selectedPiece.getLegalMove()[clickedSquare.getFile()][clickedSquare.getRank()])
            {
                //then process that move
                Move.processMove(selectedPiece, clickedSquare);
            }
            //always clear the piece selection
            selectionObj.clearSelection();
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) 
    {
        //when the mouse enters a chess square, shade the square slightly
        e.getComponent().setBackground(mixColors(e.getComponent().getBackground(), Color.BLACK, 0.1));
    }

    public void mouseExited(MouseEvent e) 
    {
        //when the mouse exits a chess square, restore to the original color
        ChessSquare enteredSquare = null;
        
        //go through all squares to figure out which one it was
        for(int i=0; i<gameBoard.length; i++)
            for(int j=0; j<gameBoard.length; j++)
                if(e.getComponent()==gameBoard[i][j].getInnerPanel())
                {
                    //and restore it to the original color
                    enteredSquare=gameBoard[i][j];
                    enteredSquare.getInnerPanel().setBackground(enteredSquare.getCurrentColor());
                    break;
                }
    }
    
    
    public static char fileConvert(int file)
    {
        //takes a file, as represented by integers 0 to 7, and returns the character representation 'a' to 'h'
        return (char) (file+97);
    }
    public static int rankConvert(int rank)
    {
        //corresponding conversion between how the rank is stored in the program and how it is displayed to the user
        return rank+1;
    }
    public void restoreSquareColor()
    {
        //goes through all chess square and restores them to their actual color
        for(int i=0; i<this.getGameBoard().length; i++)
        {
            for(int j=0; j<this.getGameBoard().length; j++)
            {
                ChessSquare s = this.getGameBoard()[i][j];
                s.setCurrentColor(s.getActualColor());
            }  
        }
    }
    
    public void restoreSquareBorders()
    {
        //goes through all chess square and restores their borders to black
        for(int i=0; i<this.getGameBoard().length; i++)
        {
            for(int j=0; j<this.getGameBoard().length; j++)
            {
                this.getGameBoard()[i][j].setCurrentBorder(Color.BLACK);
            }  
        }
    }
    
    public static Color mixColors(Color first, Color second, double weightOfSecond)
    {
        //takes two colors, and mixes them, weighting the second color by a desired amount
        
        int red=0, green=0, blue=0;
        //amount should be integer between 0 and 1
        if(weightOfSecond<=1 && weightOfSecond>=0)
        {
            red = (int) ((1-weightOfSecond)*first.getRed() + weightOfSecond*second.getRed());
            green = (int) ((1-weightOfSecond)*first.getGreen() + weightOfSecond*second.getGreen());
            blue = (int) ((1-weightOfSecond)*first.getBlue() + weightOfSecond*second.getBlue());
        }
        return new Color(red, green, blue);
    }
    
    public static void clear2DArray(boolean[][] array)
    {
        //goes through any boolean 2D array and sets all values to false
        for(int i=0; i<array.length; i++)
            for(int j=0; j<array[i].length; j++)
                array[i][j]=false;
    }
}