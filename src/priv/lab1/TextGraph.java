package priv.lab1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.Vector;
/* 主类*/
public class TextGraph {
  Vector graph = new Vector();
  Vector vertexs = new Vector();
  int[][] p;

  /*find the vertex of node*/
  private int vertexIndex(String word) {
    for (int i = 0; i < vertexs.size(); i++) {
      if (((String)vertexs.elementAt(i)).equals(word)) {
        return i;
      }
    }
    return -1;
  }

  /*
   Create a oriented graph corrding to file
   */ 
  public void creatGraph(String filename) {
    try {
      FileReader fr = new FileReader(filename);
      BufferedReader br =  new BufferedReader(fr);
      String s;
      String str1;
      String str2;
      Node t;
      int front;
      int rear;
      int i;
      try {

        while ((s = br.readLine()) != null) {
          s = s.toLowerCase();
          front = rear = 0;
          str1 = str2 = "";

          while (front < s.length()) {
            str1 = str2;
            if (s.charAt(front) >= 97 && s.charAt(front) <= 122) {
              rear = front;
              while (rear < s.length() && s.charAt(rear) >= 97 && s.charAt(rear) <= 122) {
                rear++;
              }
              str2 = s.substring(front,rear);
              if (vertexIndex(str2) < 0) {
                vertexs.addElement(str2);
              }
              if (str1 != str2 && str1 != "" && str2 != "") {
                for (i = 0; i < graph.size(); i++) {
                  t = (Node)graph.elementAt(i);
                  if (t.data.equals(str1)) {
                    while (t.next != null) {
                      if (t.next.data.equals(str2)) {
                        t.next.weight++;
                        break;
                      } else {
                        t = t.next;
                      }
                    }
                    if (t.next == null) {
                      t.next = new Node(str2);
                    }
                    break;
                  }
                }
                if (i == graph.size()) {
                  t = new Node(str1);
                  t.next = new Node(str2);
                  graph.addElement(t);
                }
              }
              front = rear;
            } else {
              front++;
            }
          }
          fr.close();

        }
      } catch (IOException e) {
      }
    } catch (FileNotFoundException f) {
    }
  }

  public void showDirectedGraph() {

    for (int i = 0; i < graph.size(); i++) {
      Node t = (Node)graph.elementAt(i);
      System.out.print(t.data + ": ");
      while (t.next != null) {
        System.out.print(t.next.data + "(" + t.next.weight + ") ");
        t = t.next;
      }
      System.out.println();
    }
  }

  public String queryBridgeWords(String word1, String word2) {
    final Vector b1 = new Vector();
    final Vector b2 = new Vector();
    final Vector b3 = new Vector();
    Node t;

    boolean if1 = false;
    boolean if2 = false;
    int i;
    int j;
    String result;
    for (i = 0; i < graph.size(); i++) {
      t = (Node)graph.elementAt(i);
      if (t.data.equals(word2)) {
        if2 = true;
      }
      if (t.data.equals(word1)) {
        if1 = true;
        while (t.next != null) {
          if (t.next.data.equals(word2)) {
            if2 = true;
          }
          b1.addElement(t.next.data);
          t = t.next;
        }
        continue;
      }
      while (t.next != null) {
        if (t.next.data.equals(word1)) {
          if1 = true;
        }
        if (t.next.data.equals(word2)) {
          if2 = true;
          b2.addElement(((Node)graph.elementAt(i)).data);
        }
        t = t.next;
      }
    }
    if ( !if1 && if2) {
      result = "No \"" + word1 + "\" in the graph!";
    } else if (if1 && !if2) {
      result = "No \"" + word2 + "\" in the graph!";
    } else if (!if1 && !if2) {
      result = "No \"" + word1 + "\" and \"" + word2 + "\" in the graph!";
    } else {
      for (i = 0; i < b2.size(); i++) {
        for (j = 0; j < b1.size(); j++) {
          if (((String)b1.elementAt(j)).equals(b2.elementAt(i))) {
            b3.addElement(b2.elementAt(i));
          }
        }
      }
      if (b3.isEmpty()) {
        result =  "No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!";
      } else {
        result = "The bridge words from \"" + word1 + "\" to \"" + word2 + "\" are: ";
        i = 0;
        while (i != b3.size() - 1) {
          result = result + (String)b3.elementAt(i) + ", ";
          i++;
        }
        if (i > 0) {
          result = result + "and ";
        }
        result = result + (String)b3.elementAt(i) + '.';
      } 
    }
    b1.clear();
    b2.clear();
    b3.clear();
    return result;
  }

  public String generateNewText(String inputText) {
    String s = inputText.toLowerCase();
    String result;
    int front;
    int rear;
    int front1;
    int rear1;
    int random;
    int label;
    String str1;
    String str2;
    String bridge;
    Vector b = new Vector();
    front = rear = label = 0 ;
    str1 = str2 = bridge = result = "";
    while (front < s.length()) {
      str1 = str2;
      if (s.charAt(front) >= 97 && s.charAt(front) <= 122) {
        rear = front;
        while (rear < s.length() && s.charAt(rear) >= 97 && s.charAt(rear) <= 122) {
          rear++;
        }
        str2 = s.substring(front,rear);
        if (!str1.equals(str2) && !str1.equals("") && !str2.equals("")) {
          bridge = queryBridgeWords(str1, str2);
          if (bridge.indexOf("The bridge words from") >= 0) {
            front1 = rear1 = bridge.indexOf(": ") + 2;
            while (rear1 >= 0) {
              rear1 = bridge.indexOf(',',front1);
              if (rear1 >= 0) {
                b.addElement(bridge.substring(front1, rear1));
              } else {
                if (label > 0) {
                  b.addElement(bridge.substring(front1 + 4, bridge.length() - 1));
                } else {
                  b.addElement(bridge.substring(front1, bridge.length() - 1));
                }
              }
              front1 = rear1 + 2;
            }
            label = 0;
            random = (int)(Math.random() * b.size());
            result = result + (String)b.elementAt(random) + ' ';
          }

        }
        result += inputText.substring(front,rear);
        front = rear;
      } else {
        result += inputText.charAt(front);
        front++;
      }
    }
    return result;
  }

  public void floyd() {
    int n = vertexs.size();
    p = new int[n][n];
    int[][] c = new int[n][n];
    int i;
    int j;
    int k;
    Node t;
    for (i = 0; i < n; i++) {
      for (j = 0; j < n; j++) {
        c[i][j] = Integer.MAX_VALUE / 2 - 1;
        p[i][j] = -2;
      }
    }

    for (i = 0; i < graph.size(); i++) {
      t = (Node)graph.elementAt(i);
      j = vertexIndex(t.data);
      while (t.next != null) {
        k = vertexIndex(t.next.data);
        c[j][k] = t.next.weight;
        p[j][k] = -1;
        t = t.next;
      }
    }

    for (k = 0; k < n; k++) {
      for (i = 0; i < n; i++) {
        for (j = 0; j < n; j++) {
          if (c[i][k] + c[k][j] < c[i][j]) {
            c[i][j] = c[i][k] + c[k][j];
            p[i][j] = k;
          }
        }
      }
    }

  }

  public String calcShortestPath(String word1, String word2) {
    int i;
    int j;
    i = vertexIndex(word1);
    j = vertexIndex(word2);
    if (i < 0 || j < 0) {
      return "There is no path from \"" + word1 + "\" to ";
    }
    int k = p[i][j];
    if (k == -1) {
      return word1 + " -> ";
    } else if (k == -2) {
      return "There is no path from \"" + word1 + "\" to ";
    } else {
      return calcShortestPath(word1,(String)vertexs.elementAt(k))
          + calcShortestPath((String)vertexs.elementAt(k),word2);
    }

  }

  public String randomWalk() {

    try {
      final RandomAccessFile fr = 
          new RandomAccessFile("E:\\workspace\\lab1\\src\\priv\\lab1\\randomWalk.txt","rw");
      fr.seek(fr.length());
      final Vector visited = new Vector();
      final Vector temp = new Vector();
      int random;
      int i;
      Node t;
      random = (int)(Math.random() * graph.size());
      t = (Node)graph.elementAt(random);

      fr.writeBytes(t.data + " ");
      System.out.print(t.data + " ");
      while (true) {
        while (t.next != null) {
          temp.addElement(t.next);
          t = t.next;
        }
        random = (int)(Math.random() * temp.size());
        t = (Node)temp.elementAt(random);
        if (visited.contains(t)) {
          break;
        }
        visited.addElement(t);
        fr.writeBytes(t.data + " ");
        System.out.print(t.data + " ");
        for (i = 0; i < graph.size(); i++) {
          if (((Node)graph.elementAt(i)).data.equals(t.data)) {
            t = (Node)graph.elementAt(i);
            break;
          }
        }
        if (i >= graph.size()) {
          break;
        }
        temp.clear();
      }
      System.out.println();
      fr.writeBytes("\r\n");
      fr.close();
    } catch (IOException e) {
    }

    return "";
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    Scanner sc = new Scanner(System.in);
    String n;
    TextGraph test = new TextGraph();
    n = sc.nextLine();
    test.creatGraph("E:\\workspace\\lab1\\src\\priv\\lab1\\test.txt");
    n = sc.nextLine();
    test.floyd();
    System.out.println("展示有向图（邻接表）：");
    n = sc.nextLine();
    test.showDirectedGraph();
    System.out.println();
    System.out.println("查询桥接词：");
    n = sc.nextLine();
    System.out.println(test.queryBridgeWords("most","usually"));
    System.out.println(test.queryBridgeWords("most","by"));
    System.out.println(test.queryBridgeWords("ship","train"));
    System.out.println();
    System.out.println("根据桥接词生成新文本：");
    n = sc.nextLine();
    System.out.println(test.generateNewText("most usually by ship train"));
    System.out.println(test.generateNewText("power of ago"));
    System.out.println(test.generateNewText("steam who engine"));
    System.out.println();
    System.out.println("计算最短路径：");
    n = sc.nextLine();
    System.out.println(test.calcShortestPath("and", "steam") + "handsome.");
    System.out.println(test.calcShortestPath("of", "of") + "of.");
    System.out.println(test.calcShortestPath("by", "most") + "most.");
    System.out.println();
    System.out.println("随机游走：");
    n = sc.nextLine();
    test.randomWalk();
    test.randomWalk();
    test.randomWalk();
    n = sc.nextLine();

  }

}
