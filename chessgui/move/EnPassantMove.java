package chessgui.move;
/*
A subclass of Move. Handles all moves that are specifically en passant.
*/

import chessgui.game.ChessGame;
import chessgui.game.ChessSquare;
import chessgui.game.ChessPiece;

public class EnPassantMove extends Move
{
    //one extra attribute, in addition to the inherited (since captured square is not the same as destination)
    ChessSquare capturedSquare;
    
    public EnPassantMove(ChessPiece pieceMoving, ChessSquare destination) 
    {
        super(pieceMoving, destination, EN_PASSANT_MOVE);
    }
    
    public void performMove()
    {
        //precondition is that the move is legal
        
        //grabs the en passant object for easy access
        EnPassant enPassantObj = pieceMoving.getGame().getEnPassantObj();
        
        //grabs the pawn that is being taken
        ChessPiece pawnTaken = enPassantObj.getPawnBeingTaken();
        
        //the captures square is the square of the pawn being taken
        capturedSquare = pawnTaken.getSquare();
        
        //generates the movestring in algebraic notation for this move
        moveString= ChessGame.fileConvert(pieceMoving.getFile()) 
                + "x" + ChessGame.fileConvert(destination.getFile()) 
                + ChessGame.rankConvert(destination.getRank()) + " e.p.";
        
        //generates the message text that will be displayed after the move
        messageText=pieceMoving.getPlayer().getName() 
                + " just performed en passant, moving his pawn to "
                + ChessGame.fileConvert(destination.getFile()) + ChessGame.rankConvert(destination.getRank()) 
                + ", and capturing the pawn on " + ChessGame.fileConvert(capturedSquare.getFile()) + ChessGame.rankConvert(capturedSquare.getRank())
                + ". ";
        
        //removes the taken pawn as captured
        pawnTaken.removeCaptured();
        //moves the moving piece to the destination
        pieceMoving.moveTo(destination, ChessPiece.INCLUDE_IMAGE);
        //performs any necessary actions after
        afterMove();
    }
    
    public static boolean isMoveLegal(ChessPiece pieceMoving, ChessSquare targetSquare)
    {
        //determines if the given move is a legal en passant move and if it does not leave the player himself in check
        //precondition that it is an attempted en passant
        
        //grabs the en passant obj for easy access
        EnPassant enPassantObj = pieceMoving.getGame().getEnPassantObj();
        
        //if en passant was not possible, return false
        if(!enPassantObj.canEnPassant() || !enPassantObj.getPawnsTaking().contains(pieceMoving) 
                || enPassantObj.getSkippedSquare()!=targetSquare) return false; 

        //otherwise, set values for each of the variables
        ChessPiece pawnTaken = enPassantObj.getPawnBeingTaken();
        ChessSquare takenSquare = pawnTaken.getSquare();
        ChessSquare originSquare = pieceMoving.getSquare();
        
        //temporarily perform the move
        //first taking up the taken pawn
        pawnTaken.liftUp(!ChessPiece.INCLUDE_IMAGE);
        //then making the move
        pieceMoving.moveTo(targetSquare, !ChessPiece.INCLUDE_IMAGE);
        
        //see if the move puts the player himself in check
        pieceMoving.getGame().setAllScopes();
        boolean inCheck = pieceMoving.getPlayer().isInCheck();
        
        //and then return pieces to original position
        pieceMoving.moveTo(originSquare, !ChessPiece.INCLUDE_IMAGE);
        pawnTaken.placeOn(takenSquare, !ChessPiece.INCLUDE_IMAGE);
        
        //reset all the scopes, and tell if the move is legal
        pieceMoving.getGame().setAllScopes();
        return !inCheck;
    }
    
    public void processUndo()
    {
        //processes undo for an en passant move
        pieceMoving.moveTo(origin, ChessPiece.INCLUDE_IMAGE);
        game.getTakenObj().recoverLastTakenPiece(capturedSquare);
    }
}
