package chessgui.game;
/*
Abstraction of a chess square
*/

import java.awt.Color;
import javax.swing.JPanel;

public class ChessSquare 
{
    //colors of the chess squares
    public static final Color GREEN_SQUARE = new Color(188,223,173);
    public static final Color WHITE_SQUARE = new Color(255,255,255);
    
    private final Color actualColor;        //actual color of square, either green or white
    private Color currentColor;             //the color it currently appears, due to being highlighted by a piece selection
    private JPanel innerPanel;              //the inner panel which holds the image of the piece
    private JPanel outerPanel;              //the outer panel, whose only purpose is to give the effect of a border
    private ChessPiece piece;               //the piece that is currently on the square, null if the square is unoccupied
    private boolean occupied;               //if there is a piece on the square
    private final int file;                 //the column of the square, in the program 0 through 7, to the user, 'a' through 'h'
    private final int rank;                 //the row of the square, in the program 0 through 7, to the user, 1 through 8
    private final ChessGame game;           //the game to which the square belongs

    public ChessSquare(JPanel outerPanel, JPanel innerPanel, int file, int rank, ChessGame game) {
        this.occupied=false;
        this.piece=null;
        this.outerPanel = outerPanel;
        this.innerPanel = innerPanel;
        this.file = file;
        this.rank = rank;
        this.actualColor=(file-rank)%2==0? (GREEN_SQUARE):(WHITE_SQUARE);
        this.innerPanel.setBackground(actualColor);
        this.outerPanel.setBackground(Color.BLACK);
        this.currentColor = actualColor;
        this.game=game;
    }

    public void setCurrentBorder(Color color)
    {
        //the color of the border is really the color of the outer panel
        this.outerPanel.setBackground(color);
    }

    public JPanel getOuterPanel() {
        return outerPanel;
    }

    public ChessGame getGame() {
        return game;
    }
    
    public void setInnerPanel(JPanel squarePanel) {
        this.innerPanel = squarePanel;
    }

    public JPanel getInnerPanel() {
        return innerPanel;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public int getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }
    
    public void setPiece(ChessPiece piece) 
    {
        this.piece = piece;
    }
    
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Color getActualColor() {
        return actualColor;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
        this.getInnerPanel().setBackground(currentColor);
    }
    
}
