/*leitourgies twn epilogwn tou menu*/
package ce326.hw3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.regex.Pattern;
import javax.print.DocFlavor;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.DefaultEditorKit;
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

public class windowMenu implements ActionListener, ItemListener{
    private final JFrame myFrame;
    insideFrame inFrame;
    JMenu EditMenu;
    private final Color color = new Color(51, 204, 255);
    private String OldName;
    private JDialog ModalBox;
    private JTextField NewName;
    private String CopyFile = null;
    private String CutFile = null;
    private File source = null;
    private File destination = null;
    private String filepath;
    private JCheckBox readButton, writeButton, executeButton;
    FrameFavourList favourList;
    
    public windowMenu(JFrame frame, insideFrame intoframe, FrameFavourList FavouriteFilesList){
        myFrame = frame;
        inFrame = intoframe;
        favourList = FavouriteFilesList;
        
        //File menu
        JMenu FileMenu = new JMenu("File");
        
        JMenuItem FileNewWindow = new JMenuItem("New Window");
        FileNewWindow.addActionListener(this);
        FileMenu.add(FileNewWindow);
    
        JMenuItem FileExit = new JMenuItem("Exit");
        FileExit.addActionListener(this);
        FileMenu.add(FileExit);
        
        //Edit menu
        EditMenu = new JMenu("Edit");
        EditMenu.setEnabled(false);
        
        JMenuItem EditCut = new JMenuItem("Cut");
        EditCut.addActionListener(this);
        EditMenu.add(EditCut);
        
        JMenuItem EditCopy = new JMenuItem("Copy");
        EditCopy.addActionListener(this);
        EditMenu.add(EditCopy);
        
        JMenuItem EditPaste = new JMenuItem("Paste");
        EditPaste.addActionListener(this);
        EditMenu.add(EditPaste);
        
        JMenuItem EditRename = new JMenuItem("Rename");
        EditRename.addActionListener(this);
        EditMenu.add(EditRename);
        
        JMenuItem EditDelete = new JMenuItem("Delete");
        EditDelete.addActionListener(this);
        EditMenu.add(EditDelete);
        
        JMenuItem EditFavourites = new JMenuItem("Add To Favourites");
        EditFavourites.addActionListener(this);
        EditMenu.add(EditFavourites);
        
        JMenuItem EditProperties = new JMenuItem("Properties");
        EditProperties.addActionListener(this);
        EditMenu.add(EditProperties);
        
        //Search button in toolbar
        JButton search = new JButton("Search");
        
        search.addActionListener(this);
        search.setContentAreaFilled(false);
        search.setBorder(null);
        
        JMenuBar bar = new JMenuBar();
        bar.add(FileMenu);
        bar.add(EditMenu);
        bar.add(search);
        myFrame.setJMenuBar(bar);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        filepath="";
        File oldFile;
        JButton ReloadFolder = null;
        //vriskei to path tou katalogou pou vrisketai to epilegmeno arxeio
        Component[] breadcrumbList = inFrame.path.getComponents();
        for(Component c:breadcrumbList){
            if(c instanceof JButton){
                filepath = filepath + ((JButton)c).getText();
                ReloadFolder = (JButton)c;
            }
            else{
                filepath = filepath + File.separator;
            }
        }
        
        String menuChoice = e.getActionCommand();
        switch (menuChoice) {
            case "New Window":
                NewFrame window = new NewFrame();
                break;
            case "Exit":
                myFrame.dispose();
                break;
            case "Cut":
                cut(filepath, null);
                break;
            case "Copy":
                copy(filepath, null);
                break;
            case "Paste":
                paste(filepath, null);
                ReloadFolder.doClick();
                break;
            case "Rename":
                rename(null);
                break;
            case "Delete":
                delete(null);
                break;
            case "Add To Favourites":
                favourites(filepath, null);
                break;
            case "Properties":
                properties(filepath, null);
                break;
            case "Search":
                enableSearch();
                break;
            case "Cancel":
                myFrame.setEnabled(true);
                ModalBox.dispose();
                break;
            case "Change":
                String filename = NewName.getText();
                
                String newfilepath = filepath + File.separator + filename;
                File newFile = new File(newfilepath);
                
                if(newFile.exists()){
                    JDialog warning = new JDialog(ModalBox);
                    warning.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    warning.setSize(300,100);
                    warning.setLocationRelativeTo(null);
                    warning.setLayout(new BorderLayout());
                    warning.setResizable(false);
                    warning.setVisible(true);
                    
                    JLabel WarningMessage = new JLabel("File exists!", JLabel.CENTER);
                    warning.add(WarningMessage, BorderLayout.CENTER);
                    warning.revalidate();
                    warning.repaint();
                }
                else{
                    String oldfilepath = filepath + File.separator + OldName;
                    oldFile = new File(oldfilepath);
                    
                    if(oldFile.renameTo(newFile)){
                        myFrame.setEnabled(true);
                        ModalBox.dispose();
                        ReloadFolder.doClick();
                    }
                    else{
                        System.out.println("Fail to rename");
                    }
                }
                break;
            case "Delete File":
                oldFile = new File(filepath, OldName);
                if(oldFile.isDirectory()){
                    DeleteFolderItems(oldFile);
                    oldFile.delete();
                }
                else{
                    oldFile.delete();
                }
                
                myFrame.setEnabled(true);
                ModalBox.dispose();
                ReloadFolder.doClick();
                
                break;
            case "Overwrite":
                //diagrafei to palio arxeio/katalogo
                if(destination.isDirectory()){
                    DeleteFolderItems(destination);
                    destination.delete();
                }
                else{
                    destination.delete();
                }
                //prosthetei to neo arxeio/katalogo
                PasteFile(source, destination);
                
                if(CutFile!=null){
                    if(source.isDirectory()){
                        DeleteFolderItems(source);
                        source.delete();
                    }
                    else{
                        source.delete();
                    }
                }
                
                CutFile = null;
                CopyFile = null;
                
                myFrame.setEnabled(true);
                ModalBox.dispose();
                ReloadFolder.doClick();                
                
                break;
            default:
                break;
        }
    }
    
    public void enableSearch(){
        Component[] componentList = inFrame.search.getComponents();
        if(componentList.length == 0){
            inFrame.search.add(inFrame.text);
            inFrame.search.add(inFrame.SearchButton);
            inFrame.search.revalidate();
            inFrame.search.repaint();
        }
        else{
            inFrame.search.remove(inFrame.text);
            inFrame.search.remove(inFrame.SearchButton);
            inFrame.search.revalidate();
            inFrame.search.repaint();
        }
    }
    
    public void rename(Component chosenFile){
        Component[] componentList = inFrame.files.getComponents();
        for(Component c:componentList){
            if(c.getBackground().equals(color)){
                chosenFile = c;
            }
        }
        
        ModalBox = new JDialog(myFrame);
        ModalBox.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        ModalBox.setSize(300,120);
        myFrame.setEnabled(false);
        ModalBox.setLocationRelativeTo(null);
        ModalBox.setLayout(new GridLayout(2,1));
        ModalBox.setResizable(false);

        JPanel TextPart = new JPanel(new GridLayout(2, 1));
        JLabel ModalBoxUse = new JLabel("Enter the new name of the file: ", JLabel.CENTER);
        TextPart.add(ModalBoxUse);

        Component[] IconDetails = ((Container)chosenFile).getComponents();
        OldName = ((JLabel)IconDetails[1]).getText();
        NewName = new JTextField(OldName, 30);
        TextPart.add(NewName);

        ModalBox.add(TextPart);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 2));
        JButton ChangeName = new JButton("Change");
        ChangeName.setContentAreaFilled(false);
        ChangeName.addActionListener(this);
        buttons.add(ChangeName);
        JButton Cancel = new JButton("Cancel");
        Cancel.setContentAreaFilled(false);
        Cancel.addActionListener(this);
        buttons.add(Cancel);

        ModalBox.add(buttons);

        ModalBox.setVisible(true);
    }
    
    public void delete(Component chosenFile){
        Component[] componentList = inFrame.files.getComponents();
        for(Component c:componentList){
            if(c.getBackground().equals(color)){
                chosenFile = c;
            }
        }
        ModalBox = new JDialog(myFrame);
        ModalBox.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        ModalBox.setSize(300,120);
        myFrame.setEnabled(false);
        ModalBox.setLocationRelativeTo(null);
        ModalBox.setLayout(new GridLayout(2,1));
        ModalBox.setResizable(false);

        Component[] IconDetails = ((Container)chosenFile).getComponents();
        OldName = ((JLabel)IconDetails[1]).getText();

        JLabel ModalBoxUse = new JLabel("Are you sure that you want to delete this file?", JLabel.CENTER);
        ModalBox.add(ModalBoxUse);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 2));
        JButton DeleteFile = new JButton("Delete File");
        DeleteFile.setContentAreaFilled(false);
        DeleteFile.addActionListener(this);
        buttons.add(DeleteFile);
        JButton Cancel = new JButton("Cancel");
        Cancel.setContentAreaFilled(false);
        Cancel.addActionListener(this);
        buttons.add(Cancel);

        ModalBox.add(buttons);

        ModalBox.setVisible(true);
    }
    
    //diagrafei anadromika ta arxeia pou periexontai ston katalogo
    public void DeleteFolderItems(File parentFile){
        String[] DirFiles = parentFile.list();
        for(String s: DirFiles){
            File currentFile = new File(parentFile.getPath(),s);
            if(currentFile.isDirectory()){
                DeleteFolderItems(currentFile);
                currentFile.delete();
            }
            else{
                currentFile.delete();                
            }
        }
    }
    
    public void cut(String filepath, Component chosenFile){
        Component[] componentList = inFrame.files.getComponents();
        for(Component c:componentList){
            if(c.getBackground().equals(color)){
                chosenFile = c;
            }
        }
        Component[] IconDetails = ((Container)chosenFile).getComponents();
        CutFile = filepath + File.separator + ((JLabel)IconDetails[1]).getText();
        CopyFile = null;
    }
    
    public void copy(String filepath, Component chosenFile){
        Component[] componentList = inFrame.files.getComponents();
        for(Component c:componentList){
            if(c.getBackground().equals(color)){
                chosenFile = c;
            }
        }
        Component[] IconDetails = ((Container)chosenFile).getComponents();
        CopyFile = filepath + File.separator + ((JLabel)IconDetails[1]).getText();
        CutFile = null;
    }
    
    public void paste(String filepath, Component chosenFile){
        String PasteIn = null;
        Component[] componentList = inFrame.files.getComponents();
        for(Component c:componentList){
            if(c.getBackground().equals(color)){
                chosenFile = c;
            }
        }
        
        Component[] IconDetails = ((Container)chosenFile).getComponents();
        PasteIn = filepath + File.separator + ((JLabel)IconDetails[1]).getText();
        
        //checking if we are going to paste the object in a file
        destination = new File(PasteIn);
        if(!destination.isDirectory()){
            ModalBox = new JDialog(myFrame);
            ModalBox.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            ModalBox.setSize(300,120);
            myFrame.setEnabled(false);
            ModalBox.setLocationRelativeTo(null);
            ModalBox.setLayout(new GridLayout(2,1));
            ModalBox.setResizable(false);
            
            JLabel ModalBoxUse = new JLabel("Destination is not a folder", JLabel.CENTER);
            ModalBox.add(ModalBoxUse);
            
            JPanel buttons = new JPanel();
            JButton Cancel = new JButton("Cancel");
            Cancel.setContentAreaFilled(false);
            Cancel.addActionListener(this);
            buttons.add(Cancel);
                
            ModalBox.add(buttons);
            
            ModalBox.setVisible(true);
            
            return;
        }

        if(CopyFile!= null){
           source = new File(CopyFile); 
        }
        else{
           source = new File(CutFile);
        }
        PasteIn = PasteIn + File.separator + source.toString().substring(source.toString().lastIndexOf(File.separator)+1, source.toString().length());
        destination = new File(PasteIn);
        
        if(PasteIn.contains(source.toString())){
            ModalBox = new JDialog(myFrame);
            ModalBox.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            ModalBox.setSize(350,120);
            myFrame.setEnabled(false);
            ModalBox.setLocationRelativeTo(null);
            ModalBox.setLayout(new GridLayout(2,1));
            ModalBox.setResizable(false);
            
            JLabel ModalBoxUse = new JLabel("Destination folder is a subfolder of the source folder", JLabel.CENTER);
            ModalBox.add(ModalBoxUse);
            
            JPanel buttons = new JPanel();
            JButton Cancel = new JButton("Cancel");
            Cancel.setContentAreaFilled(false);
            Cancel.addActionListener(this);
            buttons.add(Cancel);
                
            ModalBox.add(buttons);
            
            ModalBox.setVisible(true);
            
            return;
        }

        if(source.exists()){
            if(destination.exists()){
                ModalBox = new JDialog(myFrame);
                ModalBox.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                ModalBox.setSize(300,120);
                myFrame.setEnabled(false);
                ModalBox.setLocationRelativeTo(null);
                ModalBox.setLayout(new GridLayout(2,1));
                ModalBox.setResizable(false);
                
                JLabel ModalBoxUse = new JLabel("File exists! Do you want to overwrite it?", JLabel.CENTER);
                ModalBox.add(ModalBoxUse);
                
                JPanel buttons = new JPanel();
                buttons.setLayout(new GridLayout(1, 2));
                JButton Overwrite = new JButton("Overwrite");
                Overwrite.setContentAreaFilled(false);
                Overwrite.addActionListener(this);
                buttons.add(Overwrite);
                JButton Cancel = new JButton("Cancel");
                Cancel.setContentAreaFilled(false);
                Cancel.addActionListener(this);
                buttons.add(Cancel);
                
                ModalBox.add(buttons);
                
                ModalBox.setVisible(true);
            }
            else{
                PasteFile(source, destination);
                
                if(CutFile!=null){
                    if(source.isDirectory()){
                        DeleteFolderItems(source);
                        source.delete();
                    }
                    else{
                        source.delete();
                    }
                }
                
                CutFile = null;
                CopyFile = null;
            }
        }
    }
    
    //anadromika antigrafei ta arxeia
    public void PasteFile(File sourceFile, File destinationFile){
        if(sourceFile.isDirectory()){
            destinationFile.mkdir();
            
            String[] DirFiles = sourceFile.list();
            for(String s:DirFiles){
                File srcFile = new File(sourceFile, s);
                File destFile = new File(destinationFile, s);
                
                PasteFile(srcFile, destFile);
            }
        }
        else{
            FileInputStream input = null;
            FileOutputStream output = null;
            
            try{
                input = new FileInputStream(sourceFile);
                output = new FileOutputStream(destinationFile);
                byte[] buffer = new byte[1024];

                int length;
                while ((length = input.read(buffer)) > 0){
                    output.write(buffer, 0, length);
                }
            }
            catch(IOException ex){
                System.out.println("Problem in PasteFile for the file :"+sourceFile.toPath());
            }
            finally{
                try{
                    input.close();
                    output.close();
                }
                catch(IOException exClose){
                    System.out.println("Problem in files close.");
                }
            }
        }
    }
    
    public void properties(String path, Component chosenFile){
        Component[] componentList = inFrame.files.getComponents();
        for(Component c:componentList){
            if(c.getBackground().equals(color)){
                chosenFile = c;
            }
        }
        
        Component[] IconDetails = ((Container)chosenFile).getComponents();
        filepath = path + File.separator + ((JLabel)IconDetails[1]).getText();

        
        File selectedFile = new File(filepath);
        
        ModalBox = new JDialog(myFrame);
        ModalBox.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        ModalBox.setLocationRelativeTo(null);
        ModalBox.setLayout(new BoxLayout(ModalBox.getContentPane(), BoxLayout.Y_AXIS));

        JPanel FileNamePanel = new JPanel();
        FileNamePanel.setLayout(new BorderLayout());
        String pathPart = filepath.substring(filepath.lastIndexOf(File.separator), filepath.length());
        JLabel FileNameLabel = new JLabel("Name: "+pathPart, JLabel.LEFT);
        FileNamePanel.add(FileNameLabel, BorderLayout.WEST);
        ModalBox.add(FileNamePanel);
        
        JPanel FilePathPanel = new JPanel();
        FilePathPanel.setLayout(new BorderLayout());
        JLabel FilePathLabel = new JLabel("Filepath: "+filepath, JLabel.LEFT);
        FilePathPanel.add(FilePathLabel, BorderLayout.WEST);
        ModalBox.add(FilePathPanel);
        
        JPanel FileSizePanel = new JPanel();
        FileSizePanel.setLayout(new BorderLayout());
        JLabel FileSizeLabel = new JLabel("Size: "+GetFileSize(selectedFile) + " bytes" , JLabel.LEFT);
        FileSizePanel.add(FileSizeLabel, BorderLayout.WEST);
        ModalBox.add(FileSizePanel);
        
        JPanel Permissions = new JPanel();
        Permissions.setLayout(new BoxLayout(Permissions, BoxLayout.X_AXIS));
        JLabel FilePermitions = new JLabel("Permitions: ", JLabel.LEFT);
        Permissions.add(FilePermitions);
        
        //ftiaxnw ta check boxes ki elegxw an metavalontai ta permissions
        readButton = new JCheckBox("Read");
        if(selectedFile.canRead()){
            readButton.setSelected(true);
            if(selectedFile.setReadable(false, false)){
                selectedFile.setReadable(true, false);
            }
            else{
                readButton.setEnabled(false);
            }
        }
        else{
            readButton.setSelected(false);
            if(selectedFile.setReadable(true, false)){
                selectedFile.setReadable(false, false);
            }
            else{
                readButton.setEnabled(false);
            }
        }
        
        writeButton = new JCheckBox("Write");
        if(selectedFile.canWrite()){
            writeButton.setSelected(true);
            if(selectedFile.setWritable(false, false)){
                selectedFile.setWritable(true, false);
            }
            else{
                writeButton.setEnabled(false);
            }
        }
        else{
            writeButton.setSelected(false);
            if(selectedFile.setWritable(true, false)){
                selectedFile.setWritable(false, false);
            }
            else{
                writeButton.setEnabled(false);
            }
        }
 
        executeButton = new JCheckBox("Execute");
        if(selectedFile.canExecute()){
            executeButton.setSelected(true);
            if(selectedFile.setExecutable(false, false)){
                selectedFile.setExecutable(true, false);
            }
            else{
                executeButton.setEnabled(false);
            }
        }
        else{
            executeButton.setSelected(false);
            if(selectedFile.setExecutable(true, false)){
                selectedFile.setExecutable(false, false);
            }
            else{
                executeButton.setEnabled(false);
            }
        }
        
        readButton.addItemListener(this);
        writeButton.addItemListener(this);
        executeButton.addItemListener(this);
        
        Permissions.add(readButton);
        Permissions.add(writeButton);
        Permissions.add(executeButton);
        
        ModalBox.add(Permissions);
        
        //ModalBox.add(FileID);
        ModalBox.pack();
        ModalBox.setResizable(false);
        ModalBox.setVisible(true);
    }
    
    private long GetFileSize(File file){
        long length = 0;
        if(file.isDirectory()){
            for(String s: file.list()){
                length  = length + GetFileSize(new File(file, s));
            }
        }
        else{
            length  = length + file.length();
        }
        return length;
    }
    
    //allagh permission
    @Override
    public void itemStateChanged(ItemEvent event){
        File selectedFile = new File(filepath);
        Object permissionChanged = event.getItemSelectable();
        
        if(permissionChanged == readButton){
            if(selectedFile.canRead()){ 
                selectedFile.setReadable(false);
            }
            else{
                selectedFile.setReadable(true);
            }
        }
        else if(permissionChanged == writeButton){
            if(selectedFile.canWrite()){
                selectedFile.setWritable(false);
            }
            else{
                selectedFile.setWritable(true);
            }
        }
        else if(permissionChanged == executeButton){
            if(selectedFile.canExecute()){
                selectedFile.setExecutable(false);
            }
            else{
                selectedFile.setExecutable(true);
            }
        }
    }
    
    public void favourites(String path, Component chosenFile){
        Component[] IconDetails = null;
        Component[] componentList = inFrame.files.getComponents();
        for(Component c:componentList){
            if(c.getBackground().equals(color)){
                chosenFile = c;
            }
        }
        
        IconDetails = ((Container)chosenFile).getComponents();
        String filename = ((JLabel)IconDetails[1]).getText();
        filepath = path + File.separator + filename;
        
        if(!(new File(filepath)).isDirectory()){
            return;
        }
        
        File favouritesFile = new File(System.getProperty("user.home")+File.separator+".java-file-browser"+File.separator+"properties.xml");
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(favouritesFile);
            
            NodeList favouritesList = document.getElementsByTagName("favourites");
            Node favour = favouritesList.item(0);
            if (favour.getNodeType() == Node.ELEMENT_NODE) {
                Element favourite = (Element) favour;
                NodeList dirList = favourite.getChildNodes();
                for (int i = 0; i < dirList.getLength(); i++) {
                    Node dir = dirList.item(i);
                    if (dir.getNodeType() == Node.ELEMENT_NODE) {
                        Element directory = (Element) dir;
                        NodeList p = directory.getChildNodes();
                        if (p.item(0).getNodeType() == Node.ELEMENT_NODE) {
                            Element pathName = (Element) p.item(0);
                            if(pathName.getTextContent().equals(filepath)){
                                return;
                            }
                        }
                    }
                }
            }
            
            Element Directory = document.createElement("Directory");
            document.getChildNodes().item(0).appendChild(Directory);

            Element NewPath = document.createElement("path");
            NewPath.appendChild(document.createTextNode(filepath));
            Directory.appendChild(NewPath);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(favouritesFile);

            transformer.transform(domSource, streamResult);
        }
        catch(Exception ex){
            System.out.println("Problem in list add.");
        }
        
        int index = inFrame.list.getSelectedIndex();
        if(index==-1){
            index=0; 
        }
        else{
            index++;
        }
        
        inFrame.listModel.insertElementAt(filename, index);
        
        inFrame.list.setSelectedIndex(index);
        inFrame.list.ensureIndexIsVisible(index);
    }
}
