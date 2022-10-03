/*ulopoiei thn leitourgia ths anazhthshs arxeiwn*/
package ce326.hw3;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.w3c.dom.css.Counter;

public class frameSearch implements MouseListener{
    String name;
    String type;
    insideFrame inframe;
    JPanel[] DirectoryFiles;
    JPanel lastChoice;
    int clickCounter=0;
    boolean AwayFromIconClick;
    int FilesCounter=0;
    
    public frameSearch(String searchResult, insideFrame intoFrame){
        name = searchResult;
        type = "";
        if(name.contains(" type:")){
            type = name.substring(name.lastIndexOf(" type:")+6, name.length());
            name = name.substring(0,name.lastIndexOf(" type:"));
            if(!type.contains(".") && !type.equals("dir")){
                type = "."+type;
            }
        }
        inframe = intoFrame;
        String filepath="";
        JButton ReloadFolder=null;
        
        Component[] breadcrumbList = inframe.path.getComponents();
        for(Component c:breadcrumbList){
            if(c instanceof JButton){
                filepath = filepath + ((JButton)c).getText();
                ReloadFolder = (JButton)c;
            }
            else{
                filepath = filepath + File.separator;
            }
        }
        
        //afairw ta files pou periexontan sto xwro twn arxeiwn tou katalogou
        ReloadFolder.doClick();
        inframe.linkActivated = false;
        //ruthmizw to layout gia na emfanistoun katakorufa ta arxeia poy vrethhkan
        inframe.files.setLayout(new BoxLayout(inframe.files, BoxLayout.PAGE_AXIS));
        
        searchFile(new File(filepath));
        
        inframe.files.addMouseListener(this);
        
        inframe.files.revalidate();
        inframe.files.repaint();
        inframe.path.revalidate();
        inframe.path.repaint();  
    }
    
    //psaxnw anadronika tous fakelous kai emfanizw ta eikonidia pou brethhkan
    private void searchFile(File currFolder){
        String imagesFilepath="."+File.separator+"icons"+File.separator;
        String matchedIcon;
        File Images = new File(imagesFilepath);
        String[] icons = Images.list();
        String[] files = currFolder.list();
        JPanel[] temp;
        
        for(String s:files){
            matchedIcon = null;
            File nxtFile = new File(currFolder, s);
            
            if(nxtFile.isDirectory()){
                searchFile(nxtFile);
                if(s.toLowerCase().contains(name.toLowerCase()) && (type.equals("dir")||type.equals(""))){
                    temp = DirectoryFiles;
                    DirectoryFiles = new JPanel[++FilesCounter];
                    for(int i=0; i<FilesCounter-1;i++){
                        DirectoryFiles[i] = temp[i];
                    }
                    DirectoryFiles[FilesCounter-1] = new JPanel();
                    DirectoryFiles[FilesCounter-1].setLayout(new BoxLayout(DirectoryFiles[FilesCounter-1], BoxLayout.X_AXIS));
                    matchedIcon = imagesFilepath + File.separator + "folder.png";
                    
                    DirectoryFiles[FilesCounter-1].add(new JLabel(new ImageIcon(matchedIcon)));
                    DirectoryFiles[FilesCounter-1].add(new JLabel(nxtFile.toString(), JLabel.LEFT));
                    DirectoryFiles[FilesCounter-1].setAlignmentX(Component.LEFT_ALIGNMENT);
                    DirectoryFiles[FilesCounter-1].addMouseListener(this);
                    inframe.files.add(DirectoryFiles[FilesCounter-1]);
                }
            }
            else{
                if(s.toLowerCase().contains(name.toLowerCase())){
                    if(("."+s.substring(s.lastIndexOf(".")+1, s.length())).equals(type) || type.equals("")){
                        temp = DirectoryFiles;
                        DirectoryFiles = new JPanel[++FilesCounter];
                        for(int i=0; i<FilesCounter-1;i++){
                            DirectoryFiles[i] = temp[i];
                        }
                        DirectoryFiles[FilesCounter-1] = new JPanel();
                        DirectoryFiles[FilesCounter-1].setLayout(new BoxLayout(DirectoryFiles[FilesCounter-1], BoxLayout.X_AXIS));
                    
                        String fileType = s.substring(s.lastIndexOf(".")+1, s.length());
                    
                        for(String image:icons){
                            String code = image.substring(0,image.indexOf("."));
                        
                            if(code.equals(fileType)){
                                matchedIcon = imagesFilepath + File.separator + image;
                            }
                        }
                        if(matchedIcon == null){
                            matchedIcon = imagesFilepath + File.separator + "question.png";
                        }
                    
                        DirectoryFiles[FilesCounter-1].add(new JLabel(new ImageIcon(matchedIcon)));
                        DirectoryFiles[FilesCounter-1].add(new JLabel(nxtFile.toString(), JLabel.LEFT));
                        DirectoryFiles[FilesCounter-1].setAlignmentX(Component.LEFT_ALIGNMENT);
                        DirectoryFiles[FilesCounter-1].addMouseListener(this);
                        inframe.files.add(DirectoryFiles[FilesCounter-1]);
                    }
                }
            }
        }
    }
    
    //epilexthhke kapoio arxeio/katalogos
    @Override
    public void mouseClicked(MouseEvent e){
        JPanel chosenIcon = (JPanel) e.getSource();
        AwayFromIconClick = false;
        
        for(JPanel panel : DirectoryFiles){
            if(panel == chosenIcon){
                AwayFromIconClick = true;
                if(lastChoice != chosenIcon){
                    if(lastChoice != null){
                        lastChoice.setBackground(null);
                    }
                    lastChoice = chosenIcon;
                    clickCounter = 0;
                }
                clickCounter++;
                break;
            }
        }
        
        if(!AwayFromIconClick){
            if(lastChoice!=null){
                lastChoice.setBackground(null);
                lastChoice = null;
            }
            clickCounter=0;
        }
        
        if(clickCounter == 1){
            chosenIcon.setBackground(new Color(	51, 204, 255));
        }
        
        if(clickCounter==2){
            chosenIcon.setBackground(null);
            clickCounter = 0;
            
            Component[] fileDetails = chosenIcon.getComponents();
            File chosenFile = new File(((JLabel)fileDetails[1]).getText());
            if(chosenFile.isDirectory()){
                for(JPanel panel : DirectoryFiles){
                    panel.removeMouseListener(this);
                    inframe.files.remove(panel);
                }
                
                inframe.files.removeMouseListener(this);
                
                inframe.files.setLayout(new WrapLayout(FlowLayout.LEFT));
                
                new frameActivity(inframe, ((JLabel)fileDetails[1]).getText(), inframe.myFrame.menu);
            }
            else{
                try{
                    Desktop.getDesktop().open(chosenFile);
                }
                catch(IOException ex){
                    System.out.println("fail to open file");
                }
            }
        }
    }
   
    @Override
    public void mouseEntered(MouseEvent e){
        //no action
    }
    
    @Override
    public void mouseExited(MouseEvent e){
        //no action
    }
    
    @Override
    public void mousePressed(MouseEvent e){
        //no action
    }
    
    @Override
    public void mouseReleased(MouseEvent e){
        //no action
    }
}
