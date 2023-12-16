package chessgui.game;
/*
This is a template for chess pieces.
*/

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ChessPiece {
    
    public final static int PIECE_DIMENSION=ChessGame.SQUARE_DIMENSION*5/6;
    
    //the imageicons for each of the 12 types of pieces (6 types, both colors)
    public final static ImageIcon WHITE_PAWN_IMAGE = new ImageIcon(
                            new ImageIcon("./resources/WhitePawn.png")
                            .getImage().getScaledInstance(PIECE_DIMENSION,PIECE_DIMENSION,Image.SCALE_DEFAULT));
    public final static ImageIcon WHITE_KNIGHT_IMAGE = new ImageIcon(
                            new ImageIcon("./resources/WhiteKnight.png")
                            .getImage().getScaledInstance(PIECE_DIMENSION,PIECE_DIMENSION,Image.SCALE_DEFAULT));
    public final static ImageIcon WHITE_BISHOP_IMAGE = new ImageIcon(
                            new ImageIcon("./resources/WhiteBishop.png")
                            .getImage().getScaledInstance(PIECE_DIMENSION,PIECE_DIMENSION,Image.SCALE_DEFAULT));
    public final static ImageIcon WHITE_ROOK_IMAGE = new ImageIcon(
                            new ImageIcon("./resources/WhiteRook.png")
                            .getImage().getScaledInstance(PIECE_DIMENSION,PIECE_DIMENSION,Image.SCALE_DEFAULT));
    public final static ImageIcon WHITE_QUEEN_IMAGE = new ImageIcon(
                            new ImageIcon("./resources/WhiteQueen.png")
                            .getImage().getScaledInstance(PIECE_DIMENSION,PIECE_DIMENSION,Image.SCALE_DEFAULT));
    public final static ImageIcon WHITE_KING_IMAGE = new ImageIcon(
                            new ImageIcon("./resources/WhiteKing.png")
                            .getImage().getScaledInstance(PIECE_DIMENSION,PIECE_DIMENSION,Image.SCALE_DEFAULT));
    public final static ImageIcon BLACK_PAWN_IMAGE = new ImageIcon(
                            new ImageIcon("./resources/BlackPawn.png")
                            .getImage().getScaledInstance(PIECE_DIMENSION,PIECE_DIMENSION,Image.SCALE_DEFAULT));
    public final static ImageIcon BLACK_KNIGHT_IMAGE = new ImageIcon(
                            new ImageIcon("./resources/BlackKnight.png")
                            .getImage().getScaledInstance(PIECE_DIMENSION,PIECE_DIMENSION,Image.SCALE_DEFAULT));
    public final static ImageIcon BLACK_BISHOP_IMAGE = new ImageIcon(
                            new ImageIcon("./resources/BlackBishop.png")
                            .getImage().getScaledInstance(PIECE_DIMENSION,PIECE_DIMENSION,Image.SCALE_DEFAULT));
    public final static ImageIcon BLACK_ROOK_IMAGE = new ImageIcon(
                            new ImageIcon("./resources/BlackRook.png")
                            .getImage().getScaledInstance(PIECE_DIMENSION,PIECE_DIMENSION,Image.SCALE_DEFAULT));
    public final static ImageIcon BLACK_QUEEN_IMAGE = new ImageIcon(
                            new ImageIcon("./resources/BlackQueen.png")
                            .getImage().getScaledInstance(PIECE_DIMENSION,PIECE_DIMENSION,Image.SCALE_DEFAULT));
    public final static ImageIcon BLACK_KING_IMAGE = new ImageIcon(
                            new ImageIcon("./resources/BlackKing.png")
                            .getImage().getScaledInstance(PIECE_DIMENSION,PIECE_DIMENSION,Image.SCALE_DEFAULT));
    
    
    //integers representing each type of piece
    public final static int PAWN = 0;
    public final static int KNIGHT = 1;
    public final static int BISHOP = 2;
    public final static int ROOK = 3;
    public final static int QUEEN = 4;
    public final static int KING = 5;
    
    //when moving a piece for real, you include the image
    public final static boolean INCLUDE_IMAGE=true;
    
    private ChessPlayer player;         //which player owns this piece
    private boolean inPlay;             //is the piece in play?
    private boolean untouched;          //has the piece been moved?
    private boolean[][] scope;          //would the piece normally be able to move here?
    private boolean[][] legalMove;      //can the piece legally move to this square? Considers check, and adds possibility of special moves.
    private ChessSquare square;         //the square on which the piece resides
    private int type;                   //according to the static integers above
    private int value;                  //chess piece relative value
    private char symbol;                //for algebraic chess notation
    private String name;                //ie "rook"
    private JLabel imageLabel;          //label with an image of the piece

    public ChessPiece(ChessPlayer player, ChessSquare square, int type)
    {
        this.untouched=true;    //pieces are untouched to begin with
        this.player=player;
        this.square=square;
        this.type=type;
        this.inPlay=true;       //and in play to begin with
        this.setTypeValues(); //assigns value, symbol, name, and imagelabel, based on the type and color
        
        //constructs the 2D arrays, and ensures they are cleared
        this.scope = new boolean[8][8];
            ChessGame.clear2DArray(this.scope);
        this.legalMove = new boolean[8][8];
            ChessGame.clear2DArray(this.legalMove);
            
        //places piece on the square
        this.placeOn(this.square, INCLUDE_IMAGE);
    }

    //mutators and accessors
    public ChessSquare getSquare() {
        return square;
    }
    public void setSquare(ChessSquare square) {
        this.square = square;
    }
    public ChessGame getGame() {
        return this.player.getGame();
    }
    public boolean isInPlay() {
        return inPlay;
    }
    public boolean isWhite() {
        return this.player.isWhite();
    }
    public boolean isUntouched() {
        return untouched;
    }
    public boolean[][] getScope() {
        return scope;
    }
    public boolean[][] getLegalMove() {
        return legalMove;
    }
    public int getFile() {
        return this.square.getFile();
    }
    public int getRank() {
        return this.square.getRank();
    }
    public int getType() {
        return type;
    }
    public int getValue() {
        return value;
    }
    public char getSymbol() {
        return symbol;
    }
    public String getName() {
        return name;
    }
    public JLabel getImageLabel() {
        return imageLabel;
    }
    public ChessPlayer getPlayer(){
        return this.player;
    }
    public ChessSquare[][] getGameBoard() {
        return this.getGame().getGameBoard();
    }
    public void setInPlay(boolean inPlay) {
        this.inPlay = inPlay;
    }
    public void setUntouched(boolean untouched) {
        this.untouched = untouched;
    }
    
    public void setScope() {
        
        //clears the scope of the particular piece
        ChessGame.clear2DArray(this.scope);
        
        //if the piece is in play
        if(this.isInPlay())
        {
            //depending on the type
            switch (this.type) {
                case PAWN: this.setPawnScope();
                    break;
                case KNIGHT: this.setKnightScope();
                    break;
                case BISHOP: this.setDiagonalScope();
                    break;
                case ROOK: this.setLinearScope();
                    break;
                case QUEEN: 
                    this.setDiagonalScope(); 
                    this.setLinearScope();
                    break;
                case KING: this.setKingScope();
                    break;
                default:
                    break;
            }  
        }
    }
    
    private void setPawnScope() 
    {
        //sets the scope for a pawn
        
        //first grab the file and rank into more concise variables
        int x=this.getFile();
        int y=this.getRank();
        
        //check whether each type of move is possible, depending on the color of the pawn
        if(this.player.isWhite())
	{   
            //capture up and to the right
            if(x+1<=7 && y+1<=7 && this.getGameBoard()[x+1][y+1].isOccupied() && !this.getGameBoard()[x+1][y+1].getPiece().isWhite()) 
                this.scope[x+1][y+1]=true;
            //capture up and to the left
            if(x-1>=0 && y+1<=7 && this.getGameBoard()[x-1][y+1].isOccupied() && !this.getGameBoard()[x-1][y+1].getPiece().isWhite()) 
                this.scope[x-1][y+1]=true;
            //move up one square
            if(y+1<=7 && !this.getGameBoard()[x][y+1].isOccupied()) 
                this.scope[x][y+1]=true;
            //move up two squares
            if(this.isUntouched() && !this.getGameBoard()[x][y+1].isOccupied() && !this.getGameBoard()[x][y+2].isOccupied()) 
                this.scope[x][y+2]=true;
        }
	else
	{
            //capture down and to the right
            if(x+1<=7 && y-1>=0 && this.getGameBoard()[x+1][y-1].isOccupied() && this.getGameBoard()[x+1][y-1].getPiece().isWhite()) 
                this.scope[x+1][y-1]=true;
            //capture down and to the left
            if(x-1>=0 && y-1>=0 && this.getGameBoard()[x-1][y-1].isOccupied() && this.getGameBoard()[x-1][y-1].getPiece().isWhite()) 
                this.scope[x-1][y-1]=true;
            //move down one square
            if(y-1>=0 && !this.getGameBoard()[x][y-1].isOccupied()) 
                this.scope[x][y-1]=true;
            //move down two squares
            if(this.isUntouched() && !this.getGameBoard()[x][y-1].isOccupied() && !this.getGameBoard()[x][y-2].isOccupied()) 
                this.scope[x][y-2]=true;
	}	
    }

    private void setKnightScope() 
    {
        //sets the scope for a knight
        
        //first grab the file and rank into more concise variables
        int x=this.getFile();
        int y=this.getRank();
        
        //test each of the eight possible moves of the knight
        //if the desired square is on the board && if either it's not occupied, or it's occupied by the opponent, then add it to the scope 
        if((x+2<=7 && y+1<=7) && (!this.getGameBoard()[x+2][y+1].isOccupied() || this.getGameBoard()[x+2][y+1].getPiece().player.isWhite()!=this.player.isWhite())) this.scope[x+2][y+1]=true;
        if((x+1<=7 && y+2<=7) && (!this.getGameBoard()[x+1][y+2].isOccupied() || this.getGameBoard()[x+1][y+2].getPiece().player.isWhite()!=this.player.isWhite())) this.scope[x+1][y+2]=true;
        if((x+2<=7 && y-1>=0) && (!this.getGameBoard()[x+2][y-1].isOccupied() || this.getGameBoard()[x+2][y-1].getPiece().player.isWhite()!=this.player.isWhite())) this.scope[x+2][y-1]=true;
        if((x+1<=7 && y-2>=0) && (!this.getGameBoard()[x+1][y-2].isOccupied() || this.getGameBoard()[x+1][y-2].getPiece().player.isWhite()!=this.player.isWhite())) this.scope[x+1][y-2]=true;
        if((x-2>=0 && y+1<=7) && (!this.getGameBoard()[x-2][y+1].isOccupied() || this.getGameBoard()[x-2][y+1].getPiece().player.isWhite()!=this.player.isWhite())) this.scope[x-2][y+1]=true;
        if((x-1>=0 && y+2<=7) && (!this.getGameBoard()[x-1][y+2].isOccupied() || this.getGameBoard()[x-1][y+2].getPiece().player.isWhite()!=this.player.isWhite())) this.scope[x-1][y+2]=true;
        if((x-2>=0 && y-1>=0) && (!this.getGameBoard()[x-2][y-1].isOccupied() || this.getGameBoard()[x-2][y-1].getPiece().player.isWhite()!=this.player.isWhite())) this.scope[x-2][y-1]=true;
        if((x-1>=0 && y-2>=0) && (!this.getGameBoard()[x-1][y-2].isOccupied() || this.getGameBoard()[x-1][y-2].getPiece().player.isWhite()!=this.player.isWhite())) this.scope[x-1][y-2]=true;
    }

    private void setDiagonalScope() 
    {
        //sets the scope for a bishop or queen
        
        //first grab the file and rank into more concise variables
        int x=this.getFile();
        int y=this.getRank();
        
        //move up and right until edge of board, seeing if piece can move there
        for(int i=x+1, j=y+1; i<=7 && j<=7; i++, j++)
	{
            if(!this.getGameBoard()[i][j].isOccupied()) this.scope[i][j]=true;
            else if(this.getGameBoard()[i][j].getPiece().player.isWhite()!=this.player.isWhite()) {this.scope[i][j]=true; break;}
            else break;
        }
        //move down and right until edge of board
	for(int i=x+1, j=y-1; i<=7 && j>=0; i++, j--)
	{
            if(!this.getGameBoard()[i][j].isOccupied()) this.scope[i][j]=true;
            else if(this.getGameBoard()[i][j].getPiece().player.isWhite()!=this.player.isWhite()) {this.scope[i][j]=true; break;}
            else break;
        }
        //move up and left until edge of board
	for(int i=x-1, j=y+1; i>=0 && j<=7; i--, j++)
	{
            if(!this.getGameBoard()[i][j].isOccupied()) this.scope[i][j]=true;
            else if(this.getGameBoard()[i][j].getPiece().player.isWhite()!=this.player.isWhite()) {this.scope[i][j]=true; break;}
            else break;
	}
        //move down and left until edge of board
	for(int i=x-1, j=y-1; i>=0 && j>=0; i--, j--)
	{
            if(!this.getGameBoard()[i][j].isOccupied()) this.scope[i][j]=true;
            else if(this.getGameBoard()[i][j].getPiece().player.isWhite()!=this.player.isWhite()) {this.scope[i][j]=true; break;}
            else break;
	}
    }

    private void setLinearScope() 
    {
        //sets the scope for a rook or queen
        
        //first grab the file and rank into more concise variables
        int x=this.getFile();
        int y=this.getRank();
        
        //moving to the right until edge of board
        for(int i=x+1; i<=7; i++)
	{
            if(!this.getGameBoard()[i][y].isOccupied()) this.scope[i][y]=true;
            else if(this.getGameBoard()[i][y].getPiece().player.isWhite()!=this.player.isWhite()) {this.scope[i][y]=true; break;}
            else break;
	}
        //moving to the left until edge of board
	for(int i=x-1; i>=0; i--)
	{
            if(!this.getGameBoard()[i][y].isOccupied()) this.scope[i][y]=true;
            else if(this.getGameBoard()[i][y].getPiece().player.isWhite()!=this.player.isWhite()) {this.scope[i][y]=true; break;}
            else break;
	}
        //moving up until edge of board
	for(int j=y+1; j<=7; j++)
	{
            if(!this.getGameBoard()[x][j].isOccupied()) this.scope[x][j]=true;
            else if(this.getGameBoard()[x][j].getPiece().player.isWhite()!=this.player.isWhite()) {this.scope[x][j]=true; break;}
            else break;
	}
        //moving down until edge of board
        for(int j=y-1; j>=0; j--)
	{
            if(!this.getGameBoard()[x][j].isOccupied()) this.scope[x][j]=true;
            else if(this.getGameBoard()[x][j].getPiece().player.isWhite()!=this.player.isWhite()) {this.scope[x][j]=true; break;}
            else break;
	}
    }

    private void setKingScope() 
    {
        //sets the scope for a king
        
        //first grab the file and rank into more concise variables
        int x=this.getFile();
        int y=this.getRank();
                
        //testing each of the eight possible moves of the king
        //if the desired square is on the board && if either it's not occupied, or it's occupied by the opponent, then add it to the scope 
        if((x+1<=7 && y+1<=7)   && (!this.getGameBoard()[x+1][y+1].isOccupied()  || this.getGameBoard()[x+1][y+1] .getPiece().player.isWhite()!=this.player.isWhite()))  this.scope[x+1][y+1]=true; 
	if((x+1<=7)             && (!this.getGameBoard()[x+1][y].isOccupied()    || this.getGameBoard()[x+1][y]   .getPiece().player.isWhite()!=this.player.isWhite()))  this.scope[x+1][y]=true;
        if((x+1<=7 && y-1>=0)   && (!this.getGameBoard()[x+1][y-1].isOccupied()  || this.getGameBoard()[x+1][y-1] .getPiece().player.isWhite()!=this.player.isWhite()))  this.scope[x+1][y-1]=true;
        if((y+1<=7)             && (!this.getGameBoard()[x][y+1].isOccupied()    || this.getGameBoard()[x][y+1]   .getPiece().player.isWhite()!=this.player.isWhite()))  this.scope[x][y+1]=true;
        if((y-1>=0)             && (!this.getGameBoard()[x][y-1].isOccupied()    || this.getGameBoard()[x][y-1]   .getPiece().player.isWhite()!=this.player.isWhite()))  this.scope[x][y-1]=true;
        if((x-1>=0 && y+1<=7)   && (!this.getGameBoard()[x-1][y+1].isOccupied()  || this.getGameBoard()[x-1][y+1] .getPiece().player.isWhite()!=this.player.isWhite()))  this.scope[x-1][y+1]=true;
        if((x-1>=0)             && (!this.getGameBoard()[x-1][y].isOccupied()    || this.getGameBoard()[x-1][y]   .getPiece().player.isWhite()!=this.player.isWhite()))  this.scope[x-1][y]=true;
        if((x-1>=0 && y-1>=0)   && (!this.getGameBoard()[x-1][y-1].isOccupied()  || this.getGameBoard()[x-1][y-1] .getPiece().player.isWhite()!=this.player.isWhite()))  this.scope[x-1][y-1]=true;
    }
   
    public String getInfo()
    {
        //ie "white pawn"
        return (this.getPlayer().isWhite()? "white":"black") + " " + this.getName();
    }
    
    public boolean hasLegalMove()
    {
        //go through all chess squares, see if piece can legally move there
        for(int i=0; i<=7; i++)
        {
            for(int j=0; j<=7; j++)
            {   
                if(this.isInPlay() && this.getLegalMove()[i][j]) return true;
            }  
        }
        return false;
    }
    
    public void changePieceType(int type)
    {
        //used during promotions
        this.type=type;
        this.setTypeValues(); //assigns value, symbol, name, and imagelabel
    }
    
    public void setTypeValues()
    {
        //assigns value, symbol, name, and imageLabel based on the type and color of the piece
        switch (this.type) {
            case PAWN:
                this.value=1; 
                this.symbol=' ';
                this.name="pawn";
                this.imageLabel=new JLabel(this.player.isWhite()? WHITE_PAWN_IMAGE:BLACK_PAWN_IMAGE);
                break;
            case KNIGHT:
                this.value=3; 
                this.symbol='N';
                this.name="knight";
                this.imageLabel=new JLabel(this.player.isWhite()? WHITE_KNIGHT_IMAGE:BLACK_KNIGHT_IMAGE);
                    break;
            case BISHOP:
                this.value=3; 
                this.symbol='B';
                this.name="bishop";
                this.imageLabel=new JLabel(this.player.isWhite()? WHITE_BISHOP_IMAGE:BLACK_BISHOP_IMAGE);
                break;
            case ROOK:
                this.value=5; 
                this.symbol='R';
                this.name="rook";
                this.imageLabel=new JLabel(this.player.isWhite()? WHITE_ROOK_IMAGE:BLACK_ROOK_IMAGE);
                break;
            case QUEEN:
                this.value=8;
                this.symbol='Q';
                this.name="queen";
                this.imageLabel=new JLabel(this.player.isWhite()? WHITE_QUEEN_IMAGE:BLACK_QUEEN_IMAGE);
                break;
            case KING:
                this.value=100; 
                this.symbol='K';
                this.name="king";
                this.imageLabel=new JLabel(this.player.isWhite()? WHITE_KING_IMAGE:BLACK_KING_IMAGE);
                break;
            default:
                this.value=0; 
                this.symbol=0;
                this.name=null;
                this.imageLabel=null;
                break;
        }
        
        //fits the image of the piece into the inner panel of each sqaure
        this.imageLabel.setBounds(0,0,ChessGame.SQUARE_DIMENSION-2*ChessGame.BORDER_THICKNESS,ChessGame.SQUARE_DIMENSION-2*ChessGame.BORDER_THICKNESS);
    }
    
    public void liftUp(boolean includeImage)
    {
        //represents the action of lifting the chess piece off the board
        if(includeImage) this.square.getInnerPanel().remove(this.imageLabel);
        this.square.setOccupied(false);     //the square is no longer occupied
        this.square.setPiece(null);         //and therefore has no piece on it
        this.square = null;                 //the piece does not have a square
        this.inPlay = false;                //and it is currently out of play
    }
    
    public void placeOn(ChessSquare destination, boolean includeImage)
    {
        //represents the action of placing the chess piece on a square
        
        this.inPlay = true;             //the piece is now in play        
        this.square = destination;      //its square is wherever it is being placed
        square.setPiece(this);          //that square now contains this piece
        square.setOccupied(true);       //and is now occupied
        if(includeImage) square.getInnerPanel().add(this.imageLabel);
    }       
            
    public void moveTo(ChessSquare destination, boolean includeImage)
    {
        //moves the piece from one square to another
        
        //if the image is included, this indicates the move is a real move and the piece is no longer untouched
        if(includeImage) untouched=false;
        
        //first lifts up the piece
        this.liftUp(includeImage);
        
        //then it places it on the appropriate square
        this.placeOn(destination, includeImage);
    }
    
    public void removeCaptured()
    {
        //first lifts the piece off the board
        this.liftUp(INCLUDE_IMAGE);
        
        //then calls the takenObj to handle it (add it to taken board)
        this.getGame().getTakenObj().addTakenPiece(this);
    }
}