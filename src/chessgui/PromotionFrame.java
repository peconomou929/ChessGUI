package chessgui;
/*
Frame which appears when a promotion is possible to prompt the user to decide what piece he wants.
*/

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class PromotionFrame extends JFrame implements MouseListener
{
    public final static int PROMOTION_FRAME_WIDTH=400;
    public final static int PROMOTION_FRAME_HEIGHT=400;
    private final static Color BACKGROUND = Color.WHITE;
    
    private PromotionMove promotionMoveObj;     //reference back to the move that this came from
    private JPanel textPanel;                   //holds explanatory text
        private JTextArea textLabel;            
    private JPanel piecePanel;                  //holds images of four pieces
        private JPanel rookPanel;                
        private JPanel knightPanel;
        private JPanel bishopPanel;
        private JPanel queenPanel;
                
    public PromotionFrame(PromotionMove promotionMoveObj) 
    {
        //format frame and attributes
        super("Chess");
        this.promotionMoveObj = promotionMoveObj;
        this.promotionMoveObj.game.setGameStatus(ChessGame.DIALOGUE_BOX);
        this.setBounds(ChessGame.SCREEN_WIDTH/2-PROMOTION_FRAME_WIDTH/2,ChessGame.SCREEN_HEIGHT/2-PROMOTION_FRAME_HEIGHT/2,PROMOTION_FRAME_WIDTH,PROMOTION_FRAME_HEIGHT);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.getContentPane().setBackground(BACKGROUND);
        this.setLayout(null);
        
        //format the text panel
        textPanel = new JPanel(null);
        textPanel.setBackground(BACKGROUND);
        textPanel.setBounds(0, 0, this.getWidth(),this.getHeight()/4);
            textLabel = new JTextArea("Your pawn has reached the end! Select the type of piece to which you would like it to be promoted.");
            textLabel.setForeground(Color.BLACK);
            textLabel.setBounds(ChessGame.MARGIN, ChessGame.MARGIN, textPanel.getWidth()-2*ChessGame.MARGIN,textPanel.getHeight()-2*ChessGame.MARGIN);
            textLabel.setEditable(false);
            textLabel.setLineWrap(true);
            textLabel.setWrapStyleWord(true);
            textPanel.add(textLabel);
        
        //the piece panel is a 2 by 2 grid that holds 4 images: knight, bishop, rook, and queen
        piecePanel = new JPanel(new GridLayout(2,2));
        piecePanel.setBounds(0, textPanel.getHeight(), this.getWidth(), this.getHeight()-textPanel.getHeight());
            
            knightPanel = new JPanel(new GridBagLayout());
            knightPanel.setBackground(BACKGROUND);
            knightPanel.add(new JLabel(promotionMoveObj.pieceMoving.isWhite()? ChessPiece.WHITE_KNIGHT_IMAGE: ChessPiece.BLACK_KNIGHT_IMAGE));
            knightPanel.addMouseListener(this);
            piecePanel.add(knightPanel);
            
            bishopPanel = new JPanel(new GridBagLayout());
            bishopPanel.setBackground(BACKGROUND);
            bishopPanel.add(new JLabel(promotionMoveObj.pieceMoving.isWhite()? ChessPiece.WHITE_BISHOP_IMAGE: ChessPiece.BLACK_BISHOP_IMAGE));
            bishopPanel.addMouseListener(this);
            piecePanel.add(bishopPanel);
            
            rookPanel = new JPanel(new GridBagLayout());
            rookPanel.setBackground(BACKGROUND);
            rookPanel.add(new JLabel(promotionMoveObj.pieceMoving.isWhite()? ChessPiece.WHITE_ROOK_IMAGE: ChessPiece.BLACK_ROOK_IMAGE));
            rookPanel.addMouseListener(this);
            piecePanel.add(rookPanel);
            
            queenPanel = new JPanel(new GridBagLayout());
            queenPanel.setBackground(BACKGROUND);
            queenPanel.add(new JLabel(promotionMoveObj.pieceMoving.isWhite()? ChessPiece.WHITE_QUEEN_IMAGE: ChessPiece.BLACK_QUEEN_IMAGE));
            queenPanel.addMouseListener(this);
            piecePanel.add(queenPanel);
            
        this.add(textPanel);
        this.add(piecePanel);
        this.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) 
    {
        //when the mouse is clicked
        Component c = e.getComponent();
        
        //complete the rest of the move based on the piece chosen
        if(c==knightPanel)
            promotionMoveObj.finishMove(ChessPiece.KNIGHT);
            
        else if(c==bishopPanel)
            promotionMoveObj.finishMove(ChessPiece.BISHOP);
            
        else if(c==rookPanel)
            promotionMoveObj.finishMove(ChessPiece.ROOK);
            
        else if(c==queenPanel)
            promotionMoveObj.finishMove(ChessPiece.QUEEN);
            
        //dispose this frame and continue playing the game
        this.dispose();
        promotionMoveObj.game.setGameStatus(ChessGame.PLAYING);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) 
    {
        //highlights the panel with a blue color whenever the mouse enters 
        Component c = e.getComponent();
        if(c==knightPanel || c==bishopPanel || c==rookPanel || c==queenPanel)
        {
            c.setBackground(ChessGame.mixColors(c.getBackground(), Color.BLUE, 0.2));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) 
    {
        //resets the panel to white whenever mouse exits
        e.getComponent().setBackground(Color.WHITE);
    }
}
