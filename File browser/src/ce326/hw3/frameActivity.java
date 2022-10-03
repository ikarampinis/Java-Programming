/*diaxeirizetai thn allhlepidrash tou xrhsth me to breadcrump kai me ta files
tou trexontos katalogou dhmiourgei to arxeio xml an den uparxei kai to popup menu*/
package ce326.hw3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class frameActivity implements MouseListener, ActionListener{
    List<File> fileList;
    JPanel[] DirectoryFiles;
    JLabel[] FileName;
    JLabel[] FileIcon;
    String currDir;
    insideFrame inframe;
    windowMenu mymenu;
    JPanel lastChoice;
    Component FilePopup;
    int clickCounter=0;
    boolean AwayFromIconClick;
    
    public frameActivity(insideFrame intoframe, String filepath, windowMenu menu){
        FilePopup = null;
        inframe = intoframe;
        mymenu = menu;
        Component[] componentList = inframe.path.getComponents();
        
        //exw anoiksei neo parathuro
        if("".equals(filepath)){
            //ftiaxnw to breadcrumb
            currDir = System.getProperty("user.home");
            int nextseparator=0;
            int currseparator=0;
            String nextfolder="";
            while(currDir.indexOf(File.separator,currseparator)!=-1){
                
                if(currseparator!=0){
                    JLabel space = new JLabel(" > ");
                    inframe.path.add(space);
                }
                nextseparator = currDir.indexOf(File.separator,currseparator);
                nextfolder = currDir.substring(currseparator, nextseparator);
                currseparator = nextseparator+1;
                JButton newDirInPath = new JButton(nextfolder);
                newDirInPath.addActionListener(this);
                newDirInPath.setContentAreaFilled(false);
                newDirInPath.setBorder(null);
                inframe.path.add(newDirInPath);
            }
            
            if(currDir.charAt(currDir.length()-1)!=File.separatorChar){
                JLabel space = new JLabel(" > ");
                inframe.path.add(space);
            }
            
            nextfolder = currDir.substring(currseparator, currDir.length());
            JButton newDirInPath = new JButton(nextfolder);
            newDirInPath.addActionListener(this);
            newDirInPath.setContentAreaFilled(false);
            newDirInPath.setBorder(null);
            inframe.path.add(newDirInPath);
           
            
            File favouritesFile = new File(currDir+File.separator+".java-file-browser");
            //ean to arxeio xml uparxei pairnei olh thn plroforia kai thn prosthetei sta favourites
            if(favouritesFile.exists()){
                favouritesFile = new File(currDir+File.separator+".java-file-browser"+File.separator+"properties.xml");
                
                try{
                    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
                    Document document = documentBuilder.parse(favouritesFile);
                    
                    NodeList favouritesList = document.getElementsByTagName("favourites");
                    Node favour = favouritesList.item(0);
                    if (favour.getNodeType() == Node.ELEMENT_NODE) {
                        Element favourite = (Element) favour;
                        NodeList dirList = favourite.getChildNodes();
                        for(int i=0; i<dirList.getLength();i++){
                            Node dir = dirList.item(i);
                            if (dir.getNodeType() == Node.ELEMENT_NODE) {
                                Element directory = (Element) dir;
                                NodeList p = directory.getChildNodes();
                                if (p.item(0).getNodeType() == Node.ELEMENT_NODE) {
                                    Element pathName = (Element) p.item(0);
                                    String Name = pathName.getTextContent();
                                    int lastSeparator = Name.lastIndexOf(File.separator);
                                    Name = Name.substring(lastSeparator+1, Name.length());
                                    
                                    int index = inframe.list.getSelectedIndex();
                                    if (index == -1) {
                                        index = 0;
                                    } 
                                    else {
                                        index++;
                                    }

                                    inframe.listModel.insertElementAt(Name, index);

                                    inframe.list.setSelectedIndex(index);
                                    inframe.list.ensureIndexIsVisible(index);
                                }
                            }
                        }
                    }
                }
                catch(Exception ex){
                    System.out.println("Problem in the reading of favourites file.");
                }
            }
            else{   //an to arxeio xml den uparxei to dhmiourgei
                favouritesFile.mkdir();
                favouritesFile = new File(currDir+File.separator+".java-file-browser"+File.separator+"properties.xml");
                try{
                    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
                    Document document = documentBuilder.newDocument();
                    
                    Element root = document.createElement("favourites");
                    document.appendChild(root);
                    
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource domSource = new DOMSource(document);
                    StreamResult streamResult = new StreamResult(favouritesFile);
            
                    transformer.transform(domSource, streamResult);
                }
                catch(Exception ex){
                    System.out.println("Problem in the creation of favourites file.");
                }
            }
        }
        else{
            currDir = filepath;
            //ftiaxnw to neo breadcrumb
            int nextseparator=0;
            int currseparator=0;
            String nextfolder="";
            while(currDir.indexOf(File.separator,currseparator)!=-1){
                
                if(currseparator!=0){
                    JLabel space = new JLabel(" > ");
                    inframe.path.add(space);
                }
                nextseparator = currDir.indexOf(File.separator,currseparator);
                nextfolder = currDir.substring(currseparator, nextseparator);
                currseparator = nextseparator+1;
                JButton newDirInPath = new JButton(nextfolder);
                newDirInPath.addActionListener(this);
                newDirInPath.setContentAreaFilled(false);
                newDirInPath.setBorder(null);
                inframe.path.add(newDirInPath);
            }
            
            if (currseparator != 0 && currDir.charAt(currDir.length()-1)!=File.separatorChar) {
                JLabel space = new JLabel(" > ");
                inframe.path.add(space);
            }
            nextfolder = currDir.substring(currseparator, currDir.length());
            JButton newDirInPath = new JButton(nextfolder);
            newDirInPath.addActionListener(this);
            newDirInPath.setContentAreaFilled(false);
            newDirInPath.setBorder(null);
            inframe.path.add(newDirInPath);
        }
        
        File dir = new File(currDir);
        fileList = new ArrayList<>();
        File[] files = dir.listFiles();
        
        if(files!=null){
            //dhmiourgw mia lista me ta arxeia tou trexontos katalogou
            //prwta topothetw tous katalogous kai meta ta arxeia
            
            for(int i=1; i<files.length; i++){
                File check_file = files[i];
                int j = i -1;
                
                while(j>=0 && files[j].toString() .compareTo(check_file.toString()) > 0){
                    files[j+1] = files[j];
                    j--;
                }
                files[j+1] = check_file;
            }
            
            for(int i=0; i<files.length; i++){
                if(files[i].isDirectory()){
                    fileList.add(files[i]);
                }
            }
            for(int i=0; i<files.length; i++){
                if(!files[i].isDirectory()){
                    fileList.add(files[i]);
                }
            }
        }
        
        /*dhmiourgia popup menu*/
        JPopupMenu popup = createPopupMenu();
        MouseListener popupListener = new PopupListener(popup);
        
        String matchedIcon;
        String imagesFilepath = "."+File.separator+"icons" +File.separator;
        File images = new File(imagesFilepath);
        String[] icon = images.list();
        
        DirectoryFiles = new JPanel[fileList.size()];
        FileName = new JLabel[fileList.size()];
        FileIcon = new JLabel[fileList.size()];
        int counter = 0;
        
        //ftiaxnw ta eikonidia twn arxeiwn tou katalogou
        Iterator<java.io.File> it = fileList.iterator();
        while(it.hasNext()){
            File nextFile = it.next();
            String str = nextFile.toString();
            String name = str.substring(currDir.length());
            if(name.charAt(0)==File.separatorChar){
                name = name.substring(1);
            }
            //briskw poio icon tairiazei sto arxeio
            String fileType = name.substring(name.lastIndexOf(".")+1, name.length());
            matchedIcon = null;
            if(nextFile.isDirectory()){
                matchedIcon = imagesFilepath + File.separator + "folder.png";
            }
            else{
                for(String s:icon){
                    String code = s.substring(0, s.indexOf("."));
                    if(code.equals(fileType)){
                        matchedIcon = imagesFilepath + File.separator + s;
                    }
                }
            }
            if(matchedIcon == null){
                matchedIcon = imagesFilepath + File.separator + "question.png";
            }
            
            ImageIcon imIcon = new ImageIcon(matchedIcon);
            FileIcon[counter] = new JLabel(imIcon);
            FileName[counter] = new JLabel(name, JLabel.CENTER);
            
            DirectoryFiles[counter] = new JPanel();
            DirectoryFiles[counter].setLayout(new BorderLayout());
            DirectoryFiles[counter].add(FileIcon[counter], BorderLayout.CENTER);
            DirectoryFiles[counter].add(FileName[counter], BorderLayout.SOUTH);
            DirectoryFiles[counter].setPreferredSize(new Dimension(80,100));
            DirectoryFiles[counter].addMouseListener(this);
            DirectoryFiles[counter].addMouseListener(popupListener);
            inframe.files.add(DirectoryFiles[counter]);
            
            counter++;
        }
        if(currDir.charAt(currDir.length()-1)==File.separatorChar){
            currDir = currDir.substring(0, currDir.length()-1);
        }
        inframe.files.addMouseListener(this);
        
        inframe.path.revalidate();
        inframe.path.repaint();
        inframe.files.revalidate();
        inframe.files.repaint();
    }
    
    class PopupListener extends MouseAdapter {
        JPopupMenu popup;
 
        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }
 
        @Override
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }
 
        @Override
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }
 
        private void maybeShowPopup(MouseEvent e) {
            if(e.isPopupTrigger()) {
                FilePopup = e.getComponent();
                popup.show(FilePopup,e.getX(), e.getY());
            }
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e){
        int NumOfIcon=0;
        JPanel chosenIcon = (JPanel) e.getSource();
        AwayFromIconClick = false;
        Component[] componentList = inframe.path.getComponents();
        
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
            NumOfIcon++;
        }
        
        //egine click sto panel alla oxi panw se eikonidio
        if(!AwayFromIconClick){
            if(lastChoice!=null){
                mymenu.EditMenu.setEnabled(false);
                lastChoice.setBackground(null);
                lastChoice = null;
            }
            clickCounter=0;
        }
        
        //egine to prwto click se eikonidio
        if(clickCounter == 1){
            mymenu.EditMenu.setEnabled(true);
            chosenIcon.setBackground(new Color(	51, 204, 255));
        }
        
        //egine deytero click se eikonidio
        if(clickCounter==2){
            chosenIcon.setBackground(null);
            mymenu.EditMenu.setEnabled(false);
            clickCounter = 0;
            File chosenFile = new File(currDir + File.separator + FileName[NumOfIcon].getText());
            if(chosenFile.isDirectory()){
                for(JPanel panel : DirectoryFiles){
                    panel.removeMouseListener(this);
                    inframe.files.remove(panel);
                }
                
                inframe.files.removeMouseListener(this);
                
                for(Component c:componentList){
                    if(c instanceof JButton){
                        ((JButton) c).removeActionListener(this);
                    }
                    inframe.path.remove(c);
                }
                new frameActivity(inframe, currDir+File.separator+FileName[NumOfIcon].getText(), mymenu);
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
    
    @Override
    public void actionPerformed(ActionEvent e){
        //egine epilogh katalogou tou breadcrumb
        if (e.getSource() instanceof JButton) {
            int pathLength = 0;
            String PrevDirPath = "";
            String character = "";

            Component[] componentList = inframe.path.getComponents();

            for (Component c : componentList) {
                if (c == e.getSource()) {
                    pathLength = (pathLength / 2);
                    for (int i = 0; i < currDir.length(); i++) {
                        character = character + currDir.charAt(i);
                        if (File.separator.equals(character)) {
                            pathLength--;
                        }

                        if ((pathLength == 0 && (character.equals(File.separator))) || (character.equals(File.separator) && pathLength == -1)) {
                            break;
                        }
                        PrevDirPath = PrevDirPath + character;
                        character = "";
                    }
                }
                if (!PrevDirPath.equals("")) {
                    if (c != e.getSource()) {
                        inframe.path.remove(c);
                    }
                }
                pathLength++;
            }

            for (JPanel panel : DirectoryFiles) {
                panel.removeMouseListener(this);
                inframe.files.remove(panel);
            }

            inframe.files.removeMouseListener(this);
            
            Component[] newcomponentList = inframe.path.getComponents();
            for (Component c : newcomponentList) {
                if (c instanceof JButton) {
                    ((JButton) c).removeActionListener(this);
                }
                inframe.path.remove(c);
            }
            
            if (!inframe.linkActivated) {
                if (pathLength != componentList.length - 1) {
                    new frameActivity(inframe, PrevDirPath + File.separator + e.getActionCommand(), mymenu);
                } else {
                    new frameActivity(inframe, PrevDirPath+File.separator, mymenu);
                }
            }
        }
        else{   //egine epilogh sto popup menu
            switch (e.getActionCommand()) {
                case "Cut":
                    mymenu.cut(currDir, FilePopup);
                    FilePopup = null;
                    break;
                case "Copy":
                    mymenu.copy(currDir, FilePopup);
                    FilePopup = null;
                    break;
                case "Paste":
                    mymenu.paste(currDir, FilePopup);
                    FilePopup = null;
                    Component[] componentslist = inframe.path.getComponents();
                    ((JButton)componentslist[componentslist.length-1]).doClick();
                    break;
                case "Rename":
                    mymenu.rename(FilePopup);
                    FilePopup = null;
                    break;
                case "Delete":
                    mymenu.delete(FilePopup);
                    FilePopup = null;
                    break;
                case "Add To Favourites":
                    mymenu.favourites(currDir,FilePopup);
                    FilePopup = null;
                    break;
                case "Properties":
                    mymenu.properties(currDir, FilePopup);
                    FilePopup = null;
                    break;
                default:
                    break;
            }
        }
    }
    
    private JPopupMenu createPopupMenu(){
        JMenuItem menuItem;
        
        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("Cut");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Copy");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Paste");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Rename");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Delete");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Add To Favourites");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Properties");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        
        return popup;
    }
}
