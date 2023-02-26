import java.util.LinkedList;
import java.util.Arrays;

class TreeNode{
  boolean isLeaf;
  String decisionAttribute;
  int indexDecisionAttribute;
  String tag;

  TreeNode(){
    isLeaf = false;
  }

  public void isLeaf(){
    isLeaf = true;
  }

  public void setDecisionAttribute(String s){
    decisionAttribute = s;
  }

  public void setIndexDecisionAttribute(int n){
    indexDecisionAttribute = n;
  }

  public void setTag(String t){
    tag = t;
  }
}
