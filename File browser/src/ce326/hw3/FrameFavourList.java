/*diaxeirizetai thn allhlepidrash me ta favourites kai thn afairesh tous*/
package ce326.hw3;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Popup;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FrameFavourList implements ListSelectionListener, ActionListener{
    insideFrame inFrame;
    JPopupMenu popup;
    JList<String> listItem;
    
    public FrameFavourList(insideFrame intoFrame){
        inFrame = intoFrame;
        inFrame.list.addListSelectionListener(this);
        
        popup = createListMenu();
        //vriskei thn epilogh sth lista kai diabazei apo to xml arxeio to katallhlo path
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                if(mouseEvent.getSource() instanceof JList){
                    JList theList = (JList) mouseEvent.getSource();
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    
                    try{
                        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
                        Document document = documentBuilder.parse(new File(System.getProperty("user.home")+File.separator+".java-file-browser"+File.separator+"properties.xml"));

                        NodeList favouritesList = document.getElementsByTagName("favourites");
                        Node favour = favouritesList.item(0);
                        if (favour.getNodeType() == Node.ELEMENT_NODE) {
                            Element favourite = (Element) favour;
                            NodeList dirList = favourite.getChildNodes();                            
                            Node dir = dirList.item(index);
                            if (dir.getNodeType() == Node.ELEMENT_NODE) {
                                Element directory = (Element) dir;
                                NodeList p = directory.getChildNodes();
                                if (p.item(0).getNodeType() == Node.ELEMENT_NODE) {
                                    Element pathName = (Element) p.item(0);
                                    String Name = pathName.getTextContent();
                                    
                                    MoveToFile(Name);
                                }
                            }
                        }
                    }
                    catch(Exception ex){
                        System.out.println("Problem in the choice of the list item.");
                    }
                }
            }
            
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }
 
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }
            
            @SuppressWarnings("unchecked")
            private void maybeShowPopup(MouseEvent e) {
                //elegxei an prepei na bgei to menu kai se poio stoixeio ths listas antistoixei
                if (e.isPopupTrigger()) {
                    listItem = (JList < String >) e.getComponent();
                    listItem.setSelectedIndex(listItem.locationToIndex(e.getPoint()));
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        };
        inFrame.list.addMouseListener(mouseListener);
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e){
        if(e.getValueIsAdjusting()==false){
            inFrame.list.setSelectionBackground(null);
        }
    }
    
    public void MoveToFile(String filename){
        
        Component[] breadcrumbList = inFrame.path.getComponents();
        JButton ReloadFolder = (JButton)breadcrumbList[breadcrumbList.length-1];
        
        inFrame.linkActivated = true;
        ReloadFolder.doClick();
        inFrame.linkActivated = false;
        
        new frameActivity(inFrame, filename, inFrame.myFrame.menu);
    }
    
    private JPopupMenu createListMenu(){
        JPopupMenu popupmenu = new JPopupMenu();
        
        JMenuItem menuItem = new JMenuItem("Delete");
        menuItem.addActionListener(this);
        popupmenu.add(menuItem);
        
        return popupmenu;
    }
    
    //afaireitai to epilegmeno path apo to xml kai th lista
    @Override
    public void actionPerformed(ActionEvent event){
        int index = inFrame.list.getSelectedIndex();
        inFrame.listModel.remove(index);
        
        File favouritesFile = new File(System.getProperty("user.home")+File.separator+".java-file-browser"+File.separator+"properties.xml");
        
        try{
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(favouritesFile);
            
            NodeList favouritesList = document.getElementsByTagName("favourites");
            Node favour = favouritesList.item(0);
            if (favour.getNodeType() == Node.ELEMENT_NODE) {
                Element favourite = (Element) favour;
                NodeList dirList = favourite.getChildNodes();
                Node dir = dirList.item(index);
                if (dir.getNodeType() == Node.ELEMENT_NODE) {
                    Element directory = (Element) dir;
                    favour.removeChild(directory);
                    favour.normalize();
                }
            }
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(favouritesFile);

            transformer.transform(domSource, streamResult);
        }
        catch(Exception ex){
            System.out.println("Problem in list delete.");
        }
        
        int size = inFrame.listModel.getSize();
        if(size!=0){
            if(index==size){
                index--;
            }
            inFrame.list.setSelectedIndex(index);
            inFrame.list.ensureIndexIsVisible(index);
        }
    }
}
