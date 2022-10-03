
package ce326.hw1;

public class TrieNode {
    TrieNode[] children;
    boolean isTerminal;
    
    //constructor of class trienode 
    public TrieNode(){
        children = new TrieNode[26];
        for(int i=0 ; i < 26; i++){
            children[i] = null;
        }
        isTerminal = false;
    }
}
