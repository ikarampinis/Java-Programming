/*arxh tou programmatos kai kataskeuh tou arxikou frame*/
package ce326.hw3;

public class myFilebrowser{
    public String selectedFile;
    
    public static void GUI(){
        NewFrame frame = new NewFrame();
    }
    
    public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
          GUI();
      }
    });
  }
}
