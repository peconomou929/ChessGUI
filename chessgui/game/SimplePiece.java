package chessgui.game;
/*
A simplified version of chess piece that doesn't require as much data.
These pieces are used in storing information about the board positions.
*/


public class SimplePiece 
{
    private final ChessPlayer player;   //which player owns this piece
    private final boolean inPlay;       //is the piece in play?
    private final int type;             //according to the static integers above
    private boolean[][] legalMove;      //what are the legal moves of this piece

    public SimplePiece(ChessPiece p) 
    {
        //takes a piece in the game, and copies its information in a simpler template
        this.player = p.getPlayer();
        this.inPlay = p.isInPlay();
        this.type = p.getType();
        
        this.legalMove=new boolean[8][8];
        
        for(int i=0; i<=7; i++)
            for(int j=0; j<=7; j++)
                this.legalMove[i][j]=p.getLegalMove()[i][j];
    }

    public boolean[][] getLegalMove() {
        return legalMove;
    }

    public ChessPlayer getPlayer() {
        return player;
    }

    public boolean isInPlay() {
        return inPlay;
    }

    public int getType() {
        return type;
    }

}
