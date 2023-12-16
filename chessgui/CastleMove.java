package chessgui;
/*
This class is a subclass of Move. It handles moves that are specifically castles.
*/


public class CastleMove extends Move
{
    //attributes that are specific to a castle
    private ChessPiece rook, king;
    private ChessSquare rookOrigin, kingOrigin, rookDestination, kingDestination;
    private boolean kingSide;
    
    //constructor
    public CastleMove(ChessPiece pieceMoving, ChessSquare targetSquare)
    {
        super(pieceMoving, targetSquare, CASTLE_MOVE);
        this.king=pieceMoving;
        this.kingOrigin=origin;
        this.kingDestination=targetSquare;
        this.kingSide = (kingDestination.getFile()==6);
        capture=false;
    }

    //executes castle move with precondition that it is legal
    public void performMove()
    {
        //if the castle is kingside
        if(kingSide)
        {
            rookOrigin=king.getGame().getGameBoard()[7][king.getRank()];
            rook = rookOrigin.getPiece();
            rookDestination=king.getGame().getGameBoard()[5][king.getRank()];
            this.moveString = "0-0";
            this.messageText = king.getPlayer().getName() + " just king-side castled. ";
        }
        //if queen side
        else
        {
            rookOrigin=king.getGame().getGameBoard()[0][king.getRank()];
            rook = rookOrigin.getPiece();
            rookDestination=king.getGame().getGameBoard()[3][king.getRank()];
            this.moveString = "0-0-0";
            this.messageText = king.getPlayer().getName() + " just queen-side castled. ";
        }
        
        //actually move pieces to their destinations, and include the image
        rook.moveTo(rookDestination, ChessPiece.INCLUDE_IMAGE);
        king.moveTo(kingDestination, ChessPiece.INCLUDE_IMAGE);
        //perform after move actions
        afterMove();
    }
    
    public static boolean isMoveLegal(ChessPiece pieceMoving, ChessSquare targetSquare)
    {
        //determines whether a castle is legal, with the precondition that this moves is truly an attempt to castle
        return  (targetSquare.getFile()-pieceMoving.getFile()==2 && canKingSideCastle(pieceMoving.getPlayer())) 
                || (targetSquare.getFile()-pieceMoving.getFile()==-2 && canQueenSideCastle(pieceMoving.getPlayer()));
    }
    
    public static boolean canKingSideCastle(ChessPlayer player)
    {
        //determines if the given player can king side castle
        int rank = player.isWhite()? 0:7;
        
        //none of the squares involved should be under attack or occupied 
        //neither the king nor the rook should have moved yet
        return player.getGame().getGameBoard()[4][rank].isOccupied()
                && player.getGame().getGameBoard()[7][rank].isOccupied()
                && player.getGame().getGameBoard()[4][rank].getPiece().isUntouched() 
                && player.getGame().getGameBoard()[7][rank].getPiece().isUntouched() 
                && !player.getGame().getGameBoard()[6][rank].isOccupied()
                && !player.getGame().getGameBoard()[5][rank].isOccupied()
                && !player.getOpponent().isAttacking(player.getGame().getGameBoard()[6][rank])
                && !player.getOpponent().isAttacking(player.getGame().getGameBoard()[5][rank])
                && !player.getOpponent().isAttacking(player.getGame().getGameBoard()[4][rank]);
    }
    
    public static boolean canQueenSideCastle(ChessPlayer player)
    {
        //determines if the given player can queen side castle
        int rank = player.isWhite()? 0:7;
        
        //none of the squares involved should be under attack or occupied 
        //neither the king nor the rook should have moved yet
        return player.getGame().getGameBoard()[4][rank].isOccupied()
                && player.getGame().getGameBoard()[0][rank].isOccupied()
                && player.getGame().getGameBoard()[4][rank].getPiece().isUntouched() 
                && player.getGame().getGameBoard()[0][rank].getPiece().isUntouched() 
                && !player.getGame().getGameBoard()[1][rank].isOccupied()
                && !player.getGame().getGameBoard()[2][rank].isOccupied()
                && !player.getGame().getGameBoard()[3][rank].isOccupied()
                && !player.getOpponent().isAttacking(player.getGame().getGameBoard()[2][rank])
                && !player.getOpponent().isAttacking(player.getGame().getGameBoard()[3][rank])
                && !player.getOpponent().isAttacking(player.getGame().getGameBoard()[4][rank]);
    }
    
    public void processUndo()
    {
        //executes the undo
        
        //first by moving pieces back to their origins, 
        king.moveTo(kingOrigin, ChessPiece.INCLUDE_IMAGE);
        rook.moveTo(rookOrigin, ChessPiece.INCLUDE_IMAGE);
        
        //then by setting them untouched, since they must have been before a caslte
        king.setUntouched(true);
        rook.setUntouched(true);
    }
}