package chessgui;
/*
An abstaction of a move. During every move in the game, an instance of this class 
is constructed and added to an array which is the move records of the game.
*/

public class Move 
{
    //values that moveType can take
    public static final int NORMAL_MOVE=0;
    public static final int CASTLE_MOVE=1;
    public static final int PROMOTION_MOVE=2;
    public static final int EN_PASSANT_MOVE=3;
    
    public ChessPiece pieceMoving;           //what is the piece in the game that's moving
    public int pieceType;                    //what type of piece is it
    public boolean firstTouch;               //is it moving for the first time
    public ChessSquare origin;               //where is it coming from
    public ChessSquare destination;          //where is it going
    public boolean capture;                  //is this move a capture
    public int moveType;                     //what kind of move is it
    public ChessGame game;                   //what game are we in
    public String messageText;               //what message should be displayed after the move   
    public String moveString;                //what is the algebraic notation for this move
    public int checkResult;                  //does this move result in check, checkmate, stalemate etc.
    
    public Move(ChessPiece pieceMoving, ChessSquare destination, int moveType)
    {
        this.pieceMoving=pieceMoving;
        this.pieceType=pieceMoving.getType();
        this.firstTouch=pieceMoving.isUntouched();
        this.origin=pieceMoving.getSquare();
        this.destination=destination;
        this.capture=destination.isOccupied() || moveType==EN_PASSANT_MOVE;
        this.moveType=moveType;
        this.game=pieceMoving.getGame();
        this.messageText= "";
        this.moveString = "";    
        this.checkResult = ChessGame.NO_CHECK;
    }

    //dummy methods
    public void performMove() {}
    public void processUndo() {}
    
    //this method is called when the user clicks undo
    public void undo()
    {
        //removes last move from move history, from move table, removes last two board positions from board history
        game.removeLastMoveFromTable();
        game.getMoveHistory().remove(game.getMoveHistory().size()-1);
        game.getBoardHistory().remove(game.getBoardHistory().size()-1);
        game.getBoardHistory().remove(game.getBoardHistory().size()-1);  //this board position will be re-added in setGameBoard()

        this.processUndo();
        game.setGameBoard();
    }
    
    public boolean includeFile()
    {
        //determines whether the file of a moving piece should be included in the algebraic notation
        
        //either because the piece is a pawn
        if(pieceType==ChessPiece.PAWN && destination.getFile()!=origin.getFile()) return true;
        
        //or because the move would otherwise be ambiguous
        
        //goes through all pieces 
        for(ChessPiece p: game.getChessPieces())
            {
                //if a piece is in play, and it's not the one moving, and it's of the same type and color, and it can move to the square in question
                if(p.isInPlay() 
                        && p!=pieceMoving 
                        && p.isWhite()==pieceMoving.isWhite() 
                        && p.getType()==pieceType 
                        && p.getLegalMove()[destination.getFile()][destination.getRank()])
                {
                    //then the piece moving is ambiguous and the file of origin should be included
                    return true;
                }
            }  
        return false;
    }
    
    public boolean includeRank()
    {
        //determines whether the rank of a moving piece should be included in the algebraic notation in addition to the file
        
        for(ChessPiece p: game.getChessPieces())
        {
            if(p.isInPlay() 
                    && p!=pieceMoving 
                    && p.getFile()==origin.getFile()
                    && p.getType()==pieceType 
                    && p.isWhite()==pieceMoving.isWhite() 
                    && p.getLegalMove()[destination.getFile()][destination.getRank()])
            {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isCastleMove(ChessPiece pieceMoving, ChessSquare destination)
    {
        //determines whether the move is an attempt at a castle
        
        //if the piece moving is a king, it is untouched, it is moving two squares, and on the same rank
        return pieceMoving.getType()==ChessPiece.KING 
                && pieceMoving.isUntouched()
                && Math.abs(destination.getFile()-pieceMoving.getFile())==2 
                && destination.getRank()==pieceMoving.getRank();
    }
    
    public static boolean isEnPassantMove(ChessPiece pieceMoving, ChessSquare destination)
    {
        //determines whether the move is an attempt at en passant
        
        //if the piece moving is a pawn, it moving to piece that is in a different file but not occupied
        return pieceMoving.getType()==ChessPiece.PAWN 
                && destination.getFile()!=pieceMoving.getFile() 
                && !destination.isOccupied();
    }

    public static boolean isPromotionMove(ChessPiece pieceMoving, ChessSquare destination)
    {
        //determines whether the move would constitute a promotion
        
        //if the piece moving is a pawn and it has reached an end rank
        return pieceMoving.getType()==ChessPiece.PAWN 
                && (destination.getRank()==0 || destination.getRank()==7);
    }
    
    public static void processMove(ChessPiece pieceMoving, ChessSquare destination)
    {
        //takes a move that has been entered, determines which type it is, and performs it
        
        //precondition: move is legal
        Move move=null;
        
        if(Move.isCastleMove(pieceMoving, destination))
            move = new CastleMove(pieceMoving, destination);
        
        else if(Move.isEnPassantMove(pieceMoving, destination))
            move = new EnPassantMove(pieceMoving, destination);
        
        else if(Move.isPromotionMove(pieceMoving, destination))
            move = new PromotionMove(pieceMoving, destination);
        
        else //normal move
            move = new NormalMove(pieceMoving, destination);

        move.performMove(); //different overridden methods are called based on the subclass to which "move" belongs
        // afterMove is called eventually
    }
    public void afterMove()
    {
        //finalises values for movestring and message text, adds move to table to move history, sets gameboard
        //after this method, the values encoded in this move will not be altered at all
        this.game.setAllScopes();
        this.game.setAllLegalMoves();
        ChessPlayer nextPlayer=this.pieceMoving.getPlayer().getOpponent();
        
        if(nextPlayer.isInCheck()) 
        {
            if(nextPlayer.hasLegalMoves())
            {
                //then check
                moveString=moveString+"+";
                messageText = messageText + nextPlayer.getName() + ", it's your move. You're in check!";
                checkResult = ChessGame.IN_CHECK;
            }
            else
            {
                //then checkmate
                moveString=moveString+"++";
                messageText = messageText + nextPlayer.getName() + ", you've been mated! " + nextPlayer.getOpponent().getName() + " wins!";
                checkResult = ChessGame.CHECKMATE;
            }
        }
        else
        {
            if(nextPlayer.hasLegalMoves())
            {
                //then normal move
                messageText = messageText + nextPlayer.getName() + ", it's your move.";
                checkResult = ChessGame.NO_CHECK;
            }
            else
            {
                //then stalemate
                messageText = messageText + "It's a stalemate!";
                checkResult = ChessGame.STALEMATE;
            }
        }
        
        //add this move to the table
        this.game.addMoveToTable(this);
        //and to the move records
        game.getMoveHistory().add(this);
        //then set up the game board for the next move
        this.game.setGameBoard();
    }
}

