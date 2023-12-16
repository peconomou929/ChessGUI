package chessgui;
/*
Handles all information relating to capturing pieces. It is a JPanel and represents the taken board.
*/

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Taken extends JPanel {
    
    private ChessGame game;                     //what game are we in
    private JPanel whiteTakenPiecesPanel;       //a panel that holds all of white's taken pieces
    private JPanel blackTakenPiecesPanel;       //a panel that holds all of black's taken pieces
    private ArrayList<ChessPiece> takenPieces;  //an array that holds all taken pieces
    
    public Taken(ChessGame game)
    {
        //format JPanel and set attributes
        super();
        this.game=game;
        this.setLayout(null);
        this.setBounds(game.chessBoardOuterPanel.getX()+game.chessBoardOuterPanel.getWidth()+2*ChessGame.MARGIN, game.chessBoardOuterPanel.getY() + ChessGame.BORDER_THICKNESS, 4*ChessGame.SQUARE_DIMENSION, 8*ChessGame.SQUARE_DIMENSION);
        takenPieces = new ArrayList<>();
        
        //formats the panels that hold taken pieces, and adds to the overarching panel
        whiteTakenPiecesPanel = new JPanel(new GridLayout(8,2));
        whiteTakenPiecesPanel.setBounds(0, 0, 2*ChessGame.SQUARE_DIMENSION, 8*ChessGame.SQUARE_DIMENSION);
        this.add(whiteTakenPiecesPanel);
        blackTakenPiecesPanel = new JPanel(new GridLayout(8,2));
        blackTakenPiecesPanel.setBounds(whiteTakenPiecesPanel.getWidth(), 0, 2*ChessGame.SQUARE_DIMENSION, 8*ChessGame.SQUARE_DIMENSION);
        this.add(blackTakenPiecesPanel);
        
        //taken pieces look like this:
        //(0,0)(0,1)
        //(1,0)(1,1)
        //(2,0)(2,1)
        //(3,0)(3,1)
        //    ...
        
        
        //adds a grid of panels to each sub panel. Each element of the grid will hold pieces that are taken. 
        for(int i=0; i<=7; i++)
        {
            for(int j=0; j<=1; j++)
            {
                JPanel p1=new JPanel(null);
                this.game.getPlayerWhite().takenBoard[i][j]=p1;
                p1.setBackground(Color.WHITE);
                whiteTakenPiecesPanel.add(p1);
                
                JPanel p2=new JPanel(null);
                this.game.getPlayerBlack().takenBoard[i][j]=p2;
                p2.setBackground(Color.WHITE);
                blackTakenPiecesPanel.add(p2);
            }
        }
        //adds this panel (the taken board) to the game frame
        this.game.add(this);
    }
    
    
    public void addTakenPiece(ChessPiece p)
    {
        //adds a piece to the taken board
        
        //first adds to array of taken pieces
        takenPieces.add(p);
        //then adds the image of the piece to the taken board of the corresponding player in the next available location
        p.getPlayer().takenBoard[p.getPlayer().piecesTaken/2][p.getPlayer().piecesTaken%2].add(p.getImageLabel());
        //incerement the number of pieces that have been taken from this player
        p.getPlayer().piecesTaken++;
    }
    
    public void recoverLastTakenPiece(ChessSquare placeBackSquare)
    {
        //whenver a move is undone and the most recently taken piece needs to be recovered
        
        //grab the last piece that was taken
        ChessPiece lastPiece = takenPieces.get(takenPieces.size()-1);
        //remove it from the array of taken pieces
        takenPieces.remove(lastPiece);
        //decrement the number of pieces taken from the player
        lastPiece.getPlayer().piecesTaken--;
        //place the piece back on the board on the appropriate square
        lastPiece.placeOn(placeBackSquare, ChessPiece.INCLUDE_IMAGE);
    }
}
