package chessgui;
/*
An abstraction of each player.
*/

import java.awt.Color;
import javax.swing.JPanel;

public class ChessPlayer 
{
    //the timer bar can be any of these colors, and mixtures of these colors.
    public static final Color[] timerColors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN};
    
    private boolean white;                  //is the player white?
    private String name;                    //what is the player's name? Information entered in welcome frame
    private ChessPlayer opponent;           //allows access to the player's opponent
    public ChessPiece playerKing;           //direct access to the player's king
    private final ChessGame game;           //the game to which they belong
    private int secondsRemaining;           //how much time they have left
    int piecesTaken;                        //how many of your pieces have been taken?
    JPanel[][] takenBoard;                  //the half of the taken board belonging to your pieces
    
    public ChessPlayer(String name, boolean white, ChessGame game, int secondsRemaining) {
        this.white = white;
        this.game=game;
        this.name=name;
        this.secondsRemaining=secondsRemaining;
        this.takenBoard = new JPanel[8][2];
        this.piecesTaken=0;
    }

    //mutators and accessors
    public boolean isWhite() {
        return white;
    }
    public void setWhite(boolean white) {
        this.white = white;
    }
    public void setPlayerKing(ChessPiece king)
    {
        this.playerKing=king;
    }
    public String getName() {
        return name;
    }
    public ChessPlayer getOpponent() {
        return opponent;
    }
    public void setOpponent(ChessPlayer opponent) {
        this.opponent = opponent;
        opponent.opponent = this;
    }
    public ChessPiece getPLAYER_KING() {
        return playerKing;
    }
    public ChessGame getGame() {
        return game;
    }
    public int getSecondsRemaining() {
        return secondsRemaining;
    }
    public void setSecondsRemaining(int secondsRemaining) {
        this.secondsRemaining = secondsRemaining;
    }
    
    public int getPiecesTaken() {
        return piecesTaken;
    }
    
    public int getScore()
    {
        //the score of a player is the sum of the relative values of all his remaining pieces (except the king)
        int total=0;
        
        //goes through all the pieces
        for(ChessPiece p : game.getChessPieces())
        {
            //if a piece is in play and it belongs to this player and it's not the king
            if(p.isInPlay() && p.isWhite()==this.isWhite() && p.getType()!=ChessPiece.KING)
                //then add its value to the total
                total+=p.getValue();
        }
        return total;
    }
    public JPanel[][] getTakenBoard() {
        return takenBoard;
    }
   
    public boolean isAttacking(ChessSquare square)
    {
        //tells if a player (any of his pieces) is attacking a particular square
        
        //goes through all the pieces
	for(ChessPiece p: this.getGame().getChessPieces()) 
            //if a piece belongs to this player, and it's in play, and the given square is within the scope
            if(p.getPlayer()==this && p.isInPlay() && p.getScope()[square.getFile()][square.getRank()]) 
                //then this player is attacking that square
                return true;
        
        //if not for any of the pieces, he's not
        return false;
    }  
    
    public boolean isInCheck()
    {
        //sees if the other player is attacking this square of this player's king
        return this.getOpponent().isAttacking(this.playerKing.getSquare());
    }
    
    public boolean hasLegalMoves()
    {
        //does the player have any legal moves?
        
        //goes through all chess pieces
        for(ChessPiece p: this.getGame().getChessPieces())
        {
            //if a piece belongs to this player and has a legal move
            if(p.getPlayer()==this && p.hasLegalMove())
                //this player has a legal move
                return true;
        }
        //otherwise not
        return false;
    }
    
    public Color getTimeBarColor()
    {
        //if the clock is not enabled, the time bar is always green for the player who is moving, white for the other
        if(!game.clockEnabled) return (this==game.getTurnObj().getPlayerOnTurn())? Color.GREEN:Color.WHITE;
        
        //otherwise, if the player is not moving, his time bar is gray
        else if(this!=game.getTurnObj().getPlayerOnTurn()) return Turn.LIGHT_GRAY;
        
        //otherwise, the color is determine by the proportion of time remaining for the player
        
        //will be anywhere from 0 to 3. Corresponds with indices of "timerColors" array which are 0 to 3
        double locationOfColor = 3 * (double) secondsRemaining / (double) game.secondsToPlay;
        //lower numbers indicate less time remaining, and will result in a more red time bar
        //if this value is not an integer, the colors will be blended 
        
        int index = (int) locationOfColor;
        double remainder = locationOfColor - (double) index;
        
        return (index==3)? timerColors[3]:ChessGame.mixColors(timerColors[index], timerColors[index+1], remainder);
    }
    
    public int getTimeBarLength()
    {
        //the length of the time bar is proportion to the amount of time remaining
        return this.game.getTurnObj().getTurnDisplayPanel().getWidth() * secondsRemaining/game.secondsToPlay;
    }
}
