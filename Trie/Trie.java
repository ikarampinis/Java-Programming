/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cleo
 */
package ce326.hw1;

public class Trie {
    TrieNode root;
    
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    
    public Trie(String []words){
        root = new TrieNode();
        for (String word : words) {
            if (word != null) {
                boolean flag = add(word);
            } else {
                break;
            }
        }
        
    }
    /*public Trie(){
        root = new TrieNode();
    }*/
    
    public final boolean add(String word){
        int i;
        int pos;
        int len = word.length();
        
        TrieNode crawl = root;
        
        if(contains(word)==true){
            return false;
        }
        
        for(i=0; i<len; i++){
            pos = word.charAt(i) - 'a';
            
            if(crawl.children[pos]==null){
                crawl.children[pos] = new TrieNode();
            }
            
            crawl = crawl.children[pos];
        }
        
        crawl.isTerminal = true;
        return true;
    }
    
    public boolean contains(String word){
        int i;
        int len = word.length();
        int pos;
        
        TrieNode crawl = root;
        
        for(i=0; i<len; i++){
           pos = word.charAt(i) - 'a';
           
           if(crawl.children[pos]==null){
               return false;
           }
           
           crawl = crawl.children[pos];
        }
        
        return true;
    }
    
    public int size(){
        int num;
        TrieNode search = root;
        
        num = sizeCont(search);
        
        return(num);
    }
    
    public int sizeCont(TrieNode crawl){
        int res=0, i;
        
        if(crawl.isTerminal == true){
            res = res + 1;
        }
        
        for(i=0; i<26; i++){
            if(crawl.children[i] != null){
                res = res + sizeCont(crawl.children[i]);
            }
        }
        return(res);
    }
    
    public String[] differByOne(String word){
        TrieNode crawl = root;
        int size = size();
        String[] testarray = new String[size];
        
        if(crawl==null){
            return null;
        }
        
        return differCont(crawl, word, testarray, 0, "", 0, 1);
    }
    
    public String[] differCont(TrieNode crawl, String word, String []array, int pos, String test, int errors, int max){
        String curr=test;
        int currerrors = errors;
        
        for(int i=0; i<26; i++){
            errors = currerrors;
            if(crawl.children[i] != null){
                
                test = curr + (char)('a'+i);
                
                if(word.charAt(test.length()-1)!=((char)('a'+i))){
                    
                    if(max==1){
                        if(errors==0){
                            errors++;
                        }
                       else{
                            continue;
                        }
                    }
                    else{
                        if(errors<max){
                            errors++;
                        }
                        else{
                            continue;
                        }
                    }
                }
                if((crawl.children[i].isTerminal) && (word.length()==test.length()) && errors>0){
                    array[pos]=test;
                    pos++;
                }
                else{
                    array = differCont(crawl.children[i], word, array, pos, test, errors, max);
                    for(int j=pos; j<array.length; j++){
                        if(array[j]==null){
                            break;
                        }
                    }
                }
            }
        }
        return array;
    }
    
    public String[] differBy(String word, int max){
        TrieNode crawl = root;
        int size = size();
        String[] testarray = new String[size];
        
        if(crawl==null){
            return null;
        }
        
        return differCont(crawl, word, testarray, 0, "", 0, max);
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString(){
        TrieNode search = root;
        String preorder;
        
        preorder = toStringCont(search);
        
        return preorder;
    }
    
    public String toStringCont(TrieNode crawl){
        String pre="";
        int i;
        
        if(crawl==null){
            return "";
        }
        
        for(i=0; i<26; i++){
            if(crawl.children[i] != null){
                pre=pre + " " + (char)('a'+i);
                
                
                if(crawl.children[i].isTerminal){
                  pre=pre + "!";
                }
                pre=pre + toStringCont(crawl.children[i]);
            }
        }
        
        return pre;
    }
    
    public String toDotString(){
        TrieNode search = root;
        String dot;
        boolean flag = true;
        
        dot = dotString(search, flag);
        
        dot = dot + "}\n";
        return dot;
    }
    
    public String dotString(TrieNode crawl, boolean flag){
        int i;
        String dots= "";

        for(i=0; i<26; i++){
            
            if(crawl.children[i] != null){
                
                if(flag){
                    dots = dots + "graph Trie {\n";
                    dots = dots + "\t" + crawl.hashCode() + " [label=\"ROOT\", shape=circle, color=black]\n";
                    dots = dots + "\t" + crawl.hashCode() + " -- " + crawl.children[i].hashCode() +"\n";
                    flag = false;
                }
                else{
                    dots = dots + "\t" + crawl.hashCode() + " [label=\"" + (char)('a'+i) + "\", shape=circle";
                    
                    if(crawl.children[i].isTerminal){
                        dots = dots + ", color=red]\n";
                    }
                    else{
                        dots = dots + ", color=black]\n";
                    }
                    
                    dots = dots + "\t" + crawl.hashCode() + " -- " + crawl.children[i].hashCode() +"\n";
                }
                
                dots = dots + dotString(crawl.children[i], flag);
            }
            
            else{
            }
        }
        
        return dots;
    }
    
    public String [] wordsOfPrefix(String prefix){
        TrieNode crawl = root;
        int pos, size = size();
        String [] words = new String[size];
        
        for(int i=0; i<prefix.length(); i++){
             pos = prefix.charAt(i) - 'a';
            
            if(crawl.children[pos]==null){
                return null;
            }
            crawl=crawl.children[pos];
        }
        
                
        words = search(crawl, words, prefix);
        
        return words;
    }
    
    public String [] search(TrieNode crawl, String [] array, String pref){
        int i;
        
        if(crawl==null){
            return array;
        }
        
        if(crawl.isTerminal){
            for(i=0; i<array.length; i++){
                if(array[i]==null){
                    array[i] = pref;
                    break;
                }
            }
        }
        
        for(i=0; i<26; i++){
            if(crawl.children[i] != null){
                search(crawl.children[i], array, pref + (char)(i+ 'a'));
            }
        }
        
        return array;
    }
    
    //test main
   /* public static void main(String args[]) 
    { 
        // Input keys (use only 'a' through 'z' and lower case) 
        String keys[] = {"body", "boot", "boy", "small", "smaller", 
                         "smallest", "smile", "smooth", "sugar", "sugarcane", 
                         "sugarcanes", "sugarcoat", "sugarcoated", "sugarcoating",
                         "sugarcoats", "sugared", "sugarier", "sugariest", "sugaring",
                         "sugarless", "sugarplum", "sugarplums", "sugars", "sugary"}; 
       
        String output[] = {"Not present in trie", "Present in trie"}; 
       
        
        Trie trie = new Trie(); 
       
        // Construct trie 
        int i; 
        for (i = 0; i < keys.length ; i++) 
            trie.add(keys[i]); 
       
        // Search for different keys 
        if(trie.contains("small") == true) 
            System.out.println("small --- " + output[1]); 
        else System.out.println("small --- " + output[0]); 
          
        if(trie.contains("smalll") == true) 
            System.out.println("smalll --- " + output[1]); 
        else System.out.println("smalll --- " + output[0]); 
          
        if(trie.contains("boot") == true) 
            System.out.println("boot --- " + output[1]); 
        else System.out.println("boot --- " + output[0]); 
          
        if(trie.contains("bawt") == true) 
            System.out.println("bawt --- " + output[1]); 
        else System.out.println("bawt --- " + output[0]); 
         
        int size;
        size = trie.size();
        System.out.println("size:" + size);
        
        String prefix = "sugar";
        String [] temp;
        
        temp = trie.wordsOfPrefix(prefix);
        for (String temp1 : temp) {
            if (temp1 != null) {
                System.out.println(temp1);
            }
        }
        prefix = "smalt";
        System.out.println(trie.toString());
        System.out.println(trie.toDotString());
        //System.out.println(trie.differByOne(prefix));
        
    }*/
    
}
