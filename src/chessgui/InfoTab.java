package chessgui;
/*
A sub class of jpanel. Each tab has a label and array of strings which holds its information.
*/

import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoTab extends JPanel 
{

    JLabel tabLabel;
    String[] textAreaContent;

    public InfoTab(String text, String[] tabContents) 
    {
        super(new GridBagLayout());
        this.setBackground(Color.WHITE);
        
            this.tabLabel = new JLabel(text);
            this.add(this.tabLabel);
            
        this.textAreaContent = tabContents;
    }
}
