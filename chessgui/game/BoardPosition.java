package chessgui.game;
/*
This class holds objects of type board position, which hold information about how a board is laid out.
Which squares are occupied, and if so, by what piece. 
The board position added after every move to an array.
This is useful for determining when a board position has been repeated.
It is a rule in chess that when a board position has been repeated three times, a stalemate can be accepted.
*/

public class BoardPosition
{
    public final ChessGame game;        //the game to which it belongs
    public final int[][] gameBoard;     //0-31 indicates which chess piece, 32 empty
    public final SimplePiece[] pieces;  //an array of pieces on the current board. 
    //"SimplePiece" is a simplified version of ChessPiece that doesn't require as much storage.

    public BoardPosition(ChessGame game) 
    {
        this.game = game;
        this.gameBoard = new int[8][8];
        this.pieces = new SimplePiece[32];
        
        for(int i=0; i<=7; i++)
            for(int j=0; j<=7; j++)
                gameBoard[i][j]=32;
        
        //go through all the chess pieces in the current board
        for(int i=0; i<this.game.getChessPieces().length; i++)
        {
            //copies over each piece into a simple piece template 
            pieces[i] = new SimplePiece(this.game.getChessPieces()[i]);
            
            //places the index of the simple pieces on the game board of the board position
            if(pieces[i].isInPlay()) gameBoard[this.game.getChessPieces()[i].getFile()][this.game.getChessPieces()[i].getRank()]=i;
        }
    }
    
    public boolean isIdenticalTo(BoardPosition otherPosition)
    {
        //checks to see if two board positions are identical
        //they are identical whenver the same squares have the same type and color of pieces, 
        //and whenever the legal moves of any piece are identical
        BoardPosition thisPosition=this;
        
        //goes through each square in the board position
        for(int i=0; i<=7; i++)
        {
            for(int j=0; j<=7; j++)
            {
                //if the square is occupied in one and not in the other, the two boards are not identical
                if(thisPosition.gameBoard[i][j]==32 && otherPosition.gameBoard[i][j]!=32
                        || thisPosition.gameBoard[i][j]!=32 && otherPosition.gameBoard[i][j]==32) return false;
               
                //otherwise, if they are both occupied, 
                else if(thisPosition.gameBoard[i][j]!=32)
                {
                    SimplePiece thisPiece = pieces[thisPosition.gameBoard[i][j]];
                    SimplePiece otherPiece = pieces[otherPosition.gameBoard[i][j]];

                    //they must have the same type of piece (including color)
                    if(thisPiece.getPlayer()!=otherPiece.getPlayer() || thisPiece.getType()!=otherPiece.getType()) return false;
                    
                    //also go through all the legal moves
                    for(int k=0; k<=7; k++)
                    {
                        for(int l=0; l<=7; l++)
                        {
                            //to see if they are the same. If not, the two board positions are not identical
                            if(thisPiece.getLegalMove()[k][l]!=otherPiece.getLegalMove()[k][l]) return false;
                        }
                    }
                }
            }
        }
        
        //if the method was able to get through all those criteria without returning, the two boards are identical
        return true;
    }
}
