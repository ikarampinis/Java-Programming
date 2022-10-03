/*xwrizei to parathuro se perioxes kai diaxeirizetai thn allhlepidrash
me to koumpi search*/
package ce326.hw3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

public class insideFrame implements ActionListener{
    public JList<String> list;
    public DefaultListModel<String> listModel;
    private static final int LENGTH_OF_TEXT=40;
    public JTextField text;
    public JButton SearchButton;
    public JPanel path, files, search;
    public boolean linkActivated;
    public NewFrame myFrame;
    
    public insideFrame(NewFrame window){
        myFrame = window;
        linkActivated = false;
        organization();
    }
    
    public final void organization(){
        //favourites
        myFrame.frame.setLayout(new BorderLayout());
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(list);
        listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listScrollPane.setPreferredSize(new Dimension(150,0));
        Border emptyBorderFavourites = BorderFactory.createEmptyBorder(5, 5, 5, 0);
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
        Border compound = BorderFactory.createCompoundBorder(emptyBorderFavourites, lineBorder);
        listScrollPane.setBorder(compound);
        myFrame.frame.add(listScrollPane, BorderLayout.WEST);
        
        //others
        JPanel others = new JPanel();
        others.setLayout(new BorderLayout());
        myFrame.frame.add(others, BorderLayout.CENTER);
        
        JPanel search_path = new JPanel();
        search_path.setLayout(new BorderLayout());
        others.add(search_path, BorderLayout.NORTH);
        
        search = new JPanel();
        search.setLayout(new FlowLayout(FlowLayout.LEFT));
        search_path.add(search, BorderLayout.NORTH);
        
        path = new JPanel();
        path.setLayout(new FlowLayout(FlowLayout.LEFT));
        search_path.add(path, BorderLayout.CENTER);
        
        //search
        text = new JTextField(LENGTH_OF_TEXT);
        SearchButton = new JButton("Search");
        SearchButton.setFocusPainted(false);
        SearchButton.addActionListener(this);
        
        //path
        Border emptyBorderPath = BorderFactory.createEmptyBorder(0,5,5,5);
        Border compoundPath = BorderFactory.createCompoundBorder(emptyBorderPath, lineBorder);
        path.setBorder(compoundPath);
        
        //files
        files = new JPanel();
        files.setLayout(new WrapLayout(FlowLayout.LEFT));
        JScrollPane scroll = new JScrollPane(files);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        others.add(scroll, BorderLayout.CENTER);
        Border emptyBorderFiles = BorderFactory.createEmptyBorder(5,5,5,5);
        Border compoundFiles = BorderFactory.createCompoundBorder(emptyBorderFiles, lineBorder);
        scroll.setBorder(compoundFiles);
        
    }
    
    //energopoihsh anazhthshs
    @Override
    public void actionPerformed(ActionEvent e){
        String result = text.getText();
        linkActivated = true;
        new frameSearch(result, this);
    }
}
