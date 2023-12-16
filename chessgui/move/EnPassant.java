package chessgui.move;
/*
Holds information relating to the possibility of en passant move.
*/

import chessgui.game.ChessGame;
import chessgui.game.ChessPiece;
import chessgui.game.ChessSquare;

import java.util.ArrayList;

public class EnPassant
{
    private ChessGame game;                     //reference back to the game
    private boolean canEnPassant;               //if en passant is possible
    private ChessPiece pawnBeingTaken;          //if so, the pawn that will be taken
    private ArrayList<ChessPiece> pawnsTaking;  //if so, the pawns that can take it
    private ChessSquare skippedSquare;          //if so, the square that was skipped

    public EnPassant(ChessGame game)
    {
        this.game=game;
        canEnPassant=false;
        pawnBeingTaken=null;
        pawnsTaking=new ArrayList<>();
        skippedSquare=null;
    }
    
    public ChessGame getGame() {
        return game;
    }
    public ChessSquare getSkippedSquare() {
        return skippedSquare;
    }
    public ArrayList<ChessPiece> getPawnsTaking() {
        return pawnsTaking;
    }
    public boolean canEnPassant() {
        return canEnPassant;
    }
    public ChessPiece getPawnBeingTaken() {
        return pawnBeingTaken;
    }
    public void setCanEnPassant(boolean canEnPassant) {
        this.canEnPassant = canEnPassant;
    }

    public void clearEnPassant()
    {
        //cancel the availability of en passant
        this.canEnPassant=false;
        this.pawnBeingTaken = null;
        this.getPawnsTaking().clear();
        this.skippedSquare=null;
    }
    
    public void setUpEnPassant(Move lastMove)
    {
        //precondition: last move was pawn moving up two squares
        
        //the skipped square is based on the file and color of the piece
        this.skippedSquare=lastMove.pieceMoving.isWhite()? 
            game.getGameBoard()[lastMove.destination.getFile()][lastMove.destination.getRank()-1]:
            game.getGameBoard()[lastMove.destination.getFile()][lastMove.destination.getRank()+1];
        
        //if there is a square to the right
        if(lastMove.destination.getFile()+1<=7)
        {
            ChessSquare squareToTheRight = game.getGameBoard()[lastMove.destination.getFile()+1][lastMove.destination.getRank()];
            
            //inspect to see if there is an opposing pawn which can en passant
            if(squareToTheRight.isOccupied() 
                    && squareToTheRight.getPiece().getType()==ChessPiece.PAWN 
                    && squareToTheRight.getPiece().isWhite()!=lastMove.pieceMoving.isWhite())
            {
                this.setCanEnPassant(true);
                this.pawnBeingTaken=lastMove.pieceMoving;
                this.pawnsTaking.add(squareToTheRight.getPiece());
                squareToTheRight.getPiece().getLegalMove()[this.skippedSquare.getFile()][this.skippedSquare.getRank()]=true;
            }
        }
            
        //if there is a square to the left
        if(lastMove.destination.getFile()-1>=0)
        {
            ChessSquare squareToTheLeft = game.getGameBoard()[lastMove.destination.getFile()-1][lastMove.destination.getRank()];

            //inspect to see if there is an opposing pawn which can en passant
            if(squareToTheLeft.isOccupied() 
                    && squareToTheLeft.getPiece().getType()==ChessPiece.PAWN 
                    && squareToTheLeft.getPiece().isWhite()!=lastMove.pieceMoving.isWhite())
            {
                this.setCanEnPassant(true);
                this.pawnBeingTaken=lastMove.pieceMoving;
                this.pawnsTaking.add(squareToTheLeft.getPiece());
                squareToTheLeft.getPiece().getLegalMove()[this.skippedSquare.getFile()][this.skippedSquare.getRank()]=true;
            }   
        }
    }
}