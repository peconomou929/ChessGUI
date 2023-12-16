package chessgui.move;
/*
A move that is not a special move (castle, en passant, promotion)
*/

import chessgui.game.ChessGame;
import chessgui.game.ChessPiece;
import chessgui.game.ChessSquare;

public class NormalMove extends Move
{
    //no additional attributes
    
    public NormalMove(ChessPiece pieceMoving, ChessSquare destination) 
    {
        super(pieceMoving, destination, NORMAL_MOVE);
    }
    
    public void performMove()
    {
        //generates move string in algebraic notation
        moveString = (pieceType==ChessPiece.PAWN? "":""+pieceMoving.getSymbol())
            + (includeFile()? ""+ChessGame.fileConvert(pieceMoving.getFile()):"")
            + (includeRank()? ""+ChessGame.rankConvert(pieceMoving.getRank()):"")
            + (capture? "x":"")
            + ChessGame.fileConvert(destination.getFile()) + ChessGame.rankConvert(destination.getRank());
                
        //generates message to be displayed after move
        messageText=this.pieceMoving.getPlayer().getName() 
                + " just moved " + pieceMoving.getName() + " to " 
                + ChessGame.fileConvert(destination.getFile()) + ChessGame.rankConvert(destination.getRank()) 
                + (capture? ", capturing a " + destination.getPiece().getName(): "")
                + ". ";
        
        //move piece and remove captured piece if exists
        if(capture) destination.getPiece().removeCaptured();
        pieceMoving.moveTo(destination, ChessPiece.INCLUDE_IMAGE);
        afterMove();
    }
    
    public static boolean isMoveLegal(ChessPiece pieceMoving, ChessSquare destination)
    {
        //determines if the given move will place the moving player in check, if so it's illegal
        
        //grabs the origin square
        ChessSquare origin = pieceMoving.getSquare();
        ChessPiece takenPiece=null;
        boolean capture=destination.isOccupied();
        
        //if this is a capture move
        if(capture) 
        {
            //temporarily lift up the piece to be taken
            takenPiece=destination.getPiece();
            takenPiece.liftUp(!ChessPiece.INCLUDE_IMAGE);
        }
        
        //move the moving piece in its place and set all scopes
        pieceMoving.moveTo(destination, !ChessPiece.INCLUDE_IMAGE);
        pieceMoving.getGame().setAllScopes();
        
        //see if this puts himself in check
        boolean inCheck = pieceMoving.getPlayer().isInCheck();
        
        //then move everything back to where it was
        pieceMoving.moveTo(origin, !ChessPiece.INCLUDE_IMAGE);
        if(capture) takenPiece.placeOn(destination, !ChessPiece.INCLUDE_IMAGE);
        
        //reset all the scopes
        pieceMoving.getGame().setAllScopes();

        //and tell if it's legal
        return !inCheck;
    }
    
    public void processUndo()
    {
        //undoing a normal move
        
        //first move the piece back to where it came from
        pieceMoving.moveTo(origin, ChessPiece.INCLUDE_IMAGE);
        //if it was the first touch, set untouched to true
        pieceMoving.setUntouched(firstTouch);
        //if there was a capture, recover the last taken piece
        if(capture) game.getTakenObj().recoverLastTakenPiece(destination);
    }
}
