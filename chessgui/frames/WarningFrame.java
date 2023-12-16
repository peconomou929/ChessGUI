package chessgui.frames;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class WarningFrame extends JFrame {
   
    JTextArea text;  

    public WarningFrame(String message)
    {
        super("Warning");
        this.setBounds(400,400,400,200);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        text = new JTextArea(message);
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setBackground(new Color(240,240,240));
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridheight=1;
        gbc.gridheight=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        this.add(text, gbc);
        
        
        this.setVisible(true);
    }
}