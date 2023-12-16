package chessgui;
/*
A subclass of Move. Handles moves that are specifically promotions.
*/

import chessgui.frames.PromotionFrame;

public class PromotionMove extends Move
{
    //no extra attributes
    
    public PromotionMove(ChessPiece pieceMoving, ChessSquare targetSquare) 
    {
        super(pieceMoving, targetSquare, PROMOTION_MOVE);
    }
    
    @Override
    public void performMove()
    {
        //when the move is performed, a frame pops up prompting the user to select a piece to which to promote his pawn
        new PromotionFrame(this);
    }
    public void finishMove(int pieceSelection)
    {
        //once the user selects which piece, finish the move
        
        //first lift up the pawn
        pieceMoving.liftUp(ChessPiece.INCLUDE_IMAGE);
        //change the piece type of the pawn to whatever the user selected 
        pieceMoving.changePieceType(pieceSelection);
        
        //generate move string in algebraic notation
        moveString = ChessGame.fileConvert(origin.getFile())
            + (capture? "x" + ChessGame.fileConvert(destination.getFile()):"")
            + ChessGame.rankConvert(destination.getRank())
            + "=" + pieceMoving.getSymbol();
        
        //generate message to display to user after move is made
        messageText=pieceMoving.getPlayer().getName() 
                + " just moved his pawn to " 
                + ChessGame.fileConvert(destination.getFile()) + ChessGame.rankConvert(destination.getRank()) 
                + " and promoted to a " + pieceMoving.getName() + ". ";
        
        //if the move is a capture, remove the captured piece
        if(capture) destination.getPiece().removeCaptured();
        //then finally place the piece moving on the destination square
        pieceMoving.placeOn(destination, ChessPiece.INCLUDE_IMAGE);
        //complete any further actions needed
        afterMove();
    }
    public void processUndo()
    {
        //undoes any promotion move
        
        //first lifts up the piece that just moved
        pieceMoving.liftUp(ChessPiece.INCLUDE_IMAGE);
        //changes it back into a pawn
        pieceMoving.changePieceType(pieceType);
        //places it back on original square
        pieceMoving.placeOn(origin, ChessPiece.INCLUDE_IMAGE);
        //recovers any taken pieces
        if(capture) game.getTakenObj().recoverLastTakenPiece(destination);
    }
}
