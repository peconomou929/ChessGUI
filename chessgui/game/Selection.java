package chessgui.game;
/*
Holds information about any selection that has been made. 
A selection is whenever the user clicks on a piece, to potentially move it somewhere.
*/

public class Selection {
    
    private boolean somethingSelected;      //is a piece currently selected?
    private ChessPiece piece;               //if so, what piece
    private final ChessGame game;                 //in what game

    public Selection(ChessGame game) {
        this.somethingSelected = false;
        this.piece = null;
        this.game = game;
    }

    public boolean isSomethingSelected() {
        return somethingSelected;
    }

    public void setSomethingSelected(boolean somethingSelected) {
        this.somethingSelected = somethingSelected;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }

    public void selectPiece(ChessPiece piece) {
        this.clearSelection();
        this.somethingSelected=true;
        this.piece = piece;
    }
    
    public void clearSelection()
    {
        if(this.isSomethingSelected())
        {
            this.game.restoreSquareColor();
            this.setSomethingSelected(false);
            this.setPiece(null);
        }

    }
    
}
