import java.util.*;

class MyTree{
  String branch;
  TreeNode root;
  MyTree parent;
  LinkedList<String[]> examples;
  LinkedList<Integer> used;
  LinkedList<MyTree> subtrees;
  int depth;


  MyTree(){
    parent = null;
    root = null;
    examples = new LinkedList<>();
    subtrees = new LinkedList<>();
    //leaves = new LinkedList<>();
    used = new LinkedList<>();
    depth = 0;
  }

  MyTree(MyTree tree){
    parent = tree;
    root = null;
    examples = new LinkedList<>();
    subtrees = new LinkedList<>();
  }

  public void setDepth(int d){
    depth = d;
  }


  public void setRoot(TreeNode tn){
    root = tn;
  }

  public void setBranch(String b){
    branch = b;
  }

  public void addSubTree(MyTree mt){
    subtrees.add(mt);
  }

  public void setExamples(LinkedList<String[]> l){
    examples = (LinkedList) l.clone();
  }

  public LinkedList<String[]> getExamples(){
    return examples;
  }

  public void setUsed(LinkedList<Integer> l, int i){
    used = (LinkedList) l.clone();
    used.add(i);
  }

  public LinkedList<Integer> getUsed(){
    return used;
  }

  public int numUsed(){
    return used.size();
  }

  public void setParent(MyTree mt){
    parent = mt;
  }

  public void printTree(){
    LinkedList<MyTree> stack = new LinkedList<>();
    System.out.println(" <" + root.decisionAttribute + ">");
    for(MyTree mt : subtrees){
      stack.add(mt);
    }
    while(!stack.isEmpty()){
      MyTree cur = stack.poll();
      String space = " ";
      for(int i=0; i<cur.depth; i++){
        space += "   ";
      }
      if(cur.root.isLeaf){
        System.out.println(space + cur.branch + ": " + cur.root.tag + " (" + cur.getExamples().size() + ")");
      }else{
        System.out.println(space + cur.branch + ":");
        System.out.println(space + " <" + cur.root.decisionAttribute + ">");
        for(MyTree mt : cur.subtrees){
          stack.addFirst(mt);
        }
      }
    }
  }
}
