import java.util.*;
import java.io.*;

class DT{
  public static String delimiter = ",";
  public static LinkedList<String> label;
  public static int nColumns;
  public static MyTree tree;
  public static LinkedList<LinkedList<String>> atributes;

  public static void read(String csvFile, LinkedList<String[]> l, LinkedList<String> label){ //FUNCIONA BEM
    try{
      File file = new File(csvFile);
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);
      String line = "";
      String[] temp;
      line = br.readLine();
      temp = line.split(delimiter);
      for(String s : temp) label.add(s);
      while((line = br.readLine()) != null){
        temp = line.split(delimiter);
        l.add(temp);
      }
      br.close();
    } catch (IOException ioe){
      ioe.printStackTrace();
    }
  }

  public static void read(String csvFile, LinkedList<String[]> l){
    try{
      File file = new File(csvFile);
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);
      String line = "";
      String[] temp;
      temp = line.split(delimiter);
      while((line = br.readLine()) != null){
        temp = line.split(delimiter);
        l.add(temp);
      }
      br.close();
    } catch (IOException ioe){
      ioe.printStackTrace();
    }
  }

  public static double log2(double n){
    double logi = Math.log(n) / Math.log(2);
    return logi;
  }

  public static String[] getClassArray(LinkedList<String[]> l){
    String[] s = new String[l.size()];
    int c = 0;
    for(String[] p : l){
      s[c] = p[(l.get(0).length)-1];
      c++;
    }
    return s;
  }

  public static String[] getColumnArray(LinkedList<String[]> l, int n){ // FUNCIONA BEM
    String[] s = new String[l.size()];
    int c = 0;
    for(String[] p : l){
      s[c] = p[n];
      c++;
    }
    return s;
  }

  static int tMInsert(TreeMap<String, Integer> m, String v){
    if(!m.containsKey(v)){
      m.put(v,1);
      return 1;
    }else{
      m.put(v, m.get(v)+1);
      return 0;
    }
  }

  public static double entropyForColumn(String[] col, String[] classe){
    LinkedList<String> l = new LinkedList<>(); // linkedlist to save different atributes in column
    TreeMap<String, Integer> tm = new TreeMap<>();
    double entropy = 0;

    for(String s : col){
      if(!l.contains(s)) l.add(s);
    }

    for(String s : l){
      int countString = 0;
      int diffClass = 0;
      for(int i=0; i<col.length; i++){ //contruct treemap to keep (Class-Nº appearences)/atribute
        if(col[i].equals(s)){
          countString++;
          diffClass += tMInsert(tm, classe[i]);
        }
      }
      double entropyTemp = 0;
      if(diffClass != 1){
        for(Map.Entry<String, Integer> entry : tm.entrySet()){
          entropyTemp += (entry.getValue() / (double) countString) * log2((entry.getValue() / (double) countString));
        }
        entropyTemp *= countString / (double) col.length;
        entropy += entropyTemp;
      }
      tm.clear();
    }
    return -entropy;
  }

  public static int leastEntropy(LinkedList<String[]> table, String[] classe, LinkedList<Integer> used) {
    double least = 1.0;
    int index = 0;
    for(int i=1; i<nColumns-1; i++) {
      if(!used.contains(i)){
        String[] s = getColumnArray(table, i);
        if(entropyForColumn(s, classe) < least){
          least = entropyForColumn(s, classe);
          index = i;
        }
      }
    }
    return index;
  }

  public static boolean allClassSame(String[] s) {
    String s1 = s[0];
    for(String s2 : s){
      if(!s1.equals(s2)) return false;
    }
    return true;
  }

  public static LinkedList<String> getDiffValues(MyTree tree, int n){
    LinkedList<String> l = new LinkedList<>();
    String[] s = getColumnArray(tree.getExamples(), n);
    for(String s1 : s){
      if(!l.contains(s1)) l.add(s1);
    }
    return l;
  }

  public static String mostCommonInClass(String[] array){
		Map<String,Integer> hshmap = new HashMap<String, Integer>();
		for (String str : array){
			if (hshmap.keySet().contains(str)) hshmap.put(str, hshmap.get(str) + 1);
			else hshmap.put(str, 1);
		}
		String maxStr = "";
    int maxVal = 0;
		for (Map.Entry<String,Integer> entry : hshmap.entrySet()){
			String key = entry.getKey();
			Integer count = entry.getValue();
			if (count > maxVal){
				maxVal = count;
				maxStr = key;
			}
			else if (count == maxVal){
				if (key.length() < maxStr.length()) maxStr = key;
			}
		}
    return maxStr;
	}

  public static LinkedList<LinkedList<String>> diffAtributes(LinkedList<String[]> l){
    LinkedList<LinkedList<String>> atrbts = new LinkedList<>();
    for(int i=0; i<nColumns; i++){
      LinkedList<String> nova = new LinkedList<>();
      atrbts.add(nova);

        for(String s[] : l){
          if(!nova.contains(s[i])) nova.add(s[i]);
        }

    }
    return atrbts;
  }

  public static boolean isNumeric(String strNum) {
    if (strNum == null) {
      return false;
    }
    try {
      double d = Double.parseDouble(strNum);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  public static boolean worthDiscretizing(String[] s){
    ArrayList<String> apps = new ArrayList<>();
    for(String s1 : s){
      if(!apps.contains(s1)) apps.add(s1);
      if(apps.size() > 6) return true;
    }
    return false;
  }

  public static double getMedian(double[] numArray){
    Arrays.sort(numArray);
    double median;
    if (numArray.length % 2 == 0)
      median = ((double) numArray[numArray.length/2] + (double)numArray[numArray.length/2 - 1])/2;
    else
      median = (double) numArray[numArray.length/2];
    return median;
  }

  public static void discretize(String[] s, LinkedList<String[]> table, int index){
    double[] v = new double[s.length];
    for(int i=0; i<s.length; i++){
      double d = Double.parseDouble(s[i]);
      v[i] = d;
    }
    double[] v2 = v.clone();
    double median = getMedian(v);
    String less = "<" + median;
    String eqgreater = ">=" + median;
    for(int i=0; i<table.size(); i++){
       if(v2[i] < median){
         table.get(i)[index] = less;
       }else{
         table.get(i)[index] = eqgreater;
       }
    }
  }

  public static void discretize(LinkedList<String[]> table){
    for(int index=1; index<nColumns-1; index++){
      String[] s = getColumnArray(table, index);
      if(isNumeric(s[0])){
        if(worthDiscretizing(s)){
          discretize(s, table, index);
        }
      }
    }
  }

  public static void iD3(LinkedList<String[]> table, MyTree tree){
    tree.setExamples(table);
    iD3(tree);
  }

  public static void iD3(MyTree tree){
    TreeNode root = new TreeNode();
    tree.setRoot(root);
    String[] classe = getClassArray(tree.getExamples());
    if(allClassSame(classe)){
      root.setTag(classe[0]);
      root.isLeaf();
    }
    if(tree.numUsed() == nColumns - 3){
      root.setTag(mostCommonInClass(classe));
      root.isLeaf();
    }else{
      int bestAttribute = leastEntropy(tree.getExamples(), classe, tree.getUsed());
      root.setDecisionAttribute(label.get(bestAttribute));
      root.setIndexDecisionAttribute(bestAttribute);
      String[] col = getColumnArray(tree.getExamples(), bestAttribute);
      for(String s : atributes.get(bestAttribute)){
        LinkedList<String[]> newRows = new LinkedList<>();
        for(int i=0; i<col.length; i++){
          if(col[i].equals(s)){
            newRows.add(tree.getExamples().get(i));
          }
        }
        MyTree subtree = new MyTree();
        tree.addSubTree(subtree);
        subtree.setParent(tree);
        subtree.setBranch(s);
        subtree.setDepth(tree.depth+1);
        subtree.setUsed(tree.getUsed(), bestAttribute);
        if(newRows.isEmpty()){
          TreeNode node = new TreeNode();
          subtree.setRoot(node);
          node.isLeaf();
          node.setTag(mostCommonInClass(classe));
        }else{
          subtree.setExamples(newRows);
          iD3(subtree);
        }
        newRows.clear();
      }
    }
  }

  public static String getDecision(String[] s, MyTree tree){
    int initial = tree.root.indexDecisionAttribute;
    String s1 = s[initial];
    while(true){
      for(MyTree mt : tree.subtrees){
        if(mt.branch.equals(s1)){
          if(mt.root.isLeaf){
            return mt.root.tag;
          }else{
          initial = mt.root.indexDecisionAttribute;
          s1 = s[initial];
          tree = mt;
          }
          break;
        }
      }
    }
  }

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    String csvFile = "";
    String inputFile = "";

    System.out.println("Please type the desired .csv file for the decision tree:");
    csvFile = in.next();
    System.out.println("Do you wish to input a file with examples?");
    System.out.println("0) No    1) Yes");
    int inp = in.nextInt();
    if(inp == 1){
      System.out.println("Please type the desired .csv file for the input test examples:");
      inputFile = in.next();
    }
    LinkedList<String[]> table = new LinkedList<>();
    label = new LinkedList<>();
    LinkedList<String[]> testExamples = new LinkedList<>();
    read(csvFile, table, label);
    if(inp == 1){
      read(inputFile, testExamples);
    }

    nColumns = table.get(0).length;
    discretize(table);
    atributes = diffAtributes(table);
    tree = new MyTree();
    iD3(table , tree);
    System.out.println();
    System.out.println("Decision Tree for " + csvFile + ":");
    tree.printTree();
    System.out.println();
    if(inp == 1){
      System.out.println("Proper class for examples in " + inputFile + ":");
      for(String[] s : testExamples){
        System.out.println(s[0] + ":  " + getDecision(s, tree));
      }
    }

    System.out.println();
  }
}
