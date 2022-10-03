/*kataskeuh neou parathurou*/
package ce326.hw3;

import javax.swing.JFrame;

public class NewFrame {
    public JFrame frame;
    public windowMenu menu;
    
    public NewFrame(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(710, 500);
        frame.setLocationRelativeTo(null);
        insideFrame inframe = new insideFrame(this);
        FrameFavourList favourlist = new FrameFavourList(inframe);
        menu = new windowMenu(frame, inframe, favourlist);
        frameActivity activity = new frameActivity(inframe, "", menu);
        frame.setVisible(true);
    }
}
