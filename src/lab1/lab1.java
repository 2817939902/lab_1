package lab1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

class node {
  String data;
  int weight;
  node next;

  node(String str) {
    data = str;weight=1;next = null;
  }
}

public class lab1 {

  Vector G = new Vector();
  Vector V = new Vector();
  int P[][];

  private int vertexIndex(String word)
  {
    for(int i=0; i<V.size(); i++)
    {
      if (((String)V.elementAt(i)).equals(word))
        return i;
    }
    return -1;
  }

  public void creatGraph(String filename)
  {
    try{
      FileReader fr = new FileReader(filename);
      BufferedReader br =  new BufferedReader(fr);

      String s,str1,str2;
      node t;
      int front,rear,i;
      try{
        while((s = br.readLine()) != null)
        {
          s = s.toLowerCase();
          front = rear = 0;
          str1 = str2 = "";

          while(front < s.length())
          {
            str1 = str2;
            if(s.charAt(front)>=97 && s.charAt(front) <= 122)
            {
              rear = front;
              while(rear<s.length() && s.charAt(rear)>=97 && s.charAt(rear) <= 122)
                rear++;
              str2 = s.substring(front,rear);
              if(vertexIndex(str2)<0)
                V.addElement(str2);
              if(str1 != str2 && str1 != "" && str2!= "")
              {
                for(i=0; i<G.size(); i++)
                {
                  t = (node)G.elementAt(i);
                  if (t.data.equals(str1))
                  {
                    while(t.next!=null)
                    {
                      if(t.next.data.equals(str2))
                      {
                        t.next.weight++;
                        break;
                      }
                      else 
                        t=t.next;
                    }
                    if(t.next==null)
                      t.next = new node(str2);
                    break;
                  }
                }
                if(i==G.size())
                {
                  t = new node(str1);
                  t.next = new node(str2);
                  G.addElement(t);						
                }			
              }
              front = rear;
            }
            else
              front++;
          }
          fr.close();
        }}catch(IOException e){};
    }catch(FileNotFoundException f){};
  }

  public void showDirectedGraph()
  {

    for(int i=0; i<G.size();i++)
    {
      node t = (node)G.elementAt(i);
      System.out.print(t.data+": ");
      while(t.next!=null)
      {
        System.out.print(t.next.data+"("+t.next.weight+") ");
        t=t.next;
      }
      System.out.println();
    }
  }

  public String queryBridgeWords(String word1, String word2)
  {
    Vector B1=new Vector();
    Vector B2=new Vector();
    Vector B3=new Vector();
    node t;

    boolean if1,if2;
    int i,j;
    String result;
    if1=if2=false;
    for(i=0; i<G.size(); i++)
    {
      t=(node)G.elementAt(i);
      if(t.data.equals(word2))
        if2 = true;
      if(t.data.equals(word1))
      {
        if1 = true;
        while(t.next!=null)
        {
          if(t.next.data.equals(word2))
            if2=true;
          B1.addElement(t.next.data);
          t=t.next;
        }
        continue;
      }
      while(t.next!=null)
      {
        if(t.next.data.equals(word1))
          if1=true;
        if(t.next.data.equals(word2))
        {
          if2=true;
          B2.addElement(((node)G.elementAt(i)).data);
        }
        t=t.next;
      }
    }
    if(if1==false && if2==true)
      result = "No \"" + word1+ "\" in the graph!";
    else if(if1==true && if2==false)
      result = "No \"" + word2+ "\" in the graph!";
    else if(if1==false && if2==false)
      result = "No \"" + word1 + "\" and \"" + word2+ "\" in the graph!";
    else
    {
      for(i=0; i<B2.size(); i++)
        for(j=0; j<B1.size(); j++)
        {
          if(((String)B1.elementAt(j)).equals(B2.elementAt(i)))
            B3.addElement(B2.elementAt(i));
        }
      if(B3.isEmpty())
        result =  "No bridge words from \""+word1+"\" to \""+word2+"\"!";
      else
      {
        result = "The bridge words from \""+word1+"\" to \""+word2+"\" are: ";
        i=0;
        while(i!=B3.size()-1)
        {
          result = result + (String)B3.elementAt(i)+ ", ";
          i++;
        }
        if(i>0)
          result = result + "and ";
        result = result + (String)B3.elementAt(i) + '.';
      }}
    B1.clear();
    B2.clear();
    B3.clear();
    return result;

  }

  public String generateNewText(String inputText)
  {
    String s = inputText.toLowerCase();
    String result;
    int front,rear,front1,rear1,random,label;
    String str1,str2,Bridge;
    Vector B = new Vector();
    front = rear = label=0 ;
    str1 = str2 = Bridge= result ="";
    while(front < s.length())
    {
      str1 = str2;
      if(s.charAt(front)>=97 && s.charAt(front) <= 122 )
      {
        rear = front;
        while(rear<s.length() && s.charAt(rear)>=97 && s.charAt(rear) <= 122)
          rear++;
        str2 = s.substring(front,rear);
        if(str1 != str2 && str1 != "" && str2!= "")
        {
          Bridge = queryBridgeWords(str1, str2);
          if(Bridge.indexOf("The bridge words from")>=0)
          {
            front1 = rear1 = Bridge.indexOf(": ")+2;
            while(rear1 >= 0)
            {
              rear1 = Bridge.indexOf(',',front1);
              if(rear1 >= 0)
                B.addElement(Bridge.substring(front1, rear1));	
              else
              {
                if(label > 0)
                  B.addElement(Bridge.substring(front1+4, Bridge.length()-1));
                else
                  B.addElement(Bridge.substring(front1, Bridge.length()-1));
              }
              front1 = rear1+2;
            }
            label = 0;
            random = (int)(Math.random()*B.size());
            result = result +(String)B.elementAt(random)+' ';
          }

        }
        result += inputText.substring(front,rear);
        front = rear;
      }
      else
      {
        result += inputText.charAt(front);
        front++;
      }
    }
    return result;
  }

  public void Floyd()
  {
    int n = V.size();
    P = new int[n][n];
    int C[][] = new int[n][n];
    int i,j,k;
    node t;
    for(i=0; i<n; i++)
      for(j=0; j<n; j++)
      {

        C[i][j] = Integer.MAX_VALUE/2-1;
        P[i][j] = -2;
      }

    for(i=0; i<G.size(); i++)
    {
      t = (node)G.elementAt(i);
      j = vertexIndex(t.data);
      while(t.next!=null)
      {
        k = vertexIndex(t.next.data);
        C[j][k] = t.next.weight;
        P[j][k] = -1;
        t = t.next;
      }
    }

    for(k=0; k<n; k++)
      for(i=0; i<n; i++)
        for(j=0; j<n; j++)
          if(C[i][k] + C[k][j] < C[i][j])
          {
            C[i][j] = C[i][k] + C[k][j];
            P[i][j] = k;
          }

  }
  public String calcShortestPath(String word1, String word2)
  {
    int i,j;
    i = vertexIndex(word1);
    j = vertexIndex(word2);
    if(i<0 || j<0)
      return "There is no path from \"" + word1 + "\" to ";
    int k = P[i][j];
    if(k == -1)
      return word1 + " -> ";
    else if(k == -2)
      return "There is no path from \"" + word1 + "\" to ";
    else
      return calcShortestPath(word1,(String)V.elementAt(k)) +
          calcShortestPath((String)V.elementAt(k),word2);

  }

  public String randomWalk()
  {

    try{
      RandomAccessFile fr = new RandomAccessFile("E:\\workspace\\lab1\\src\\lab1\\randomWalk.txt","rw");
      fr.seek(fr.length());
      Vector visited = new Vector();
      Vector temp = new Vector();
      int random,i;
      node t;
      random = (int)(Math.random()*G.size());
      t = (node)G.elementAt(random);

      fr.writeBytes(t.data+" ");
      System.out.print(t.data+" ");
      while(true)
      {
        while(t.next!= null)
        {
          temp.addElement(t.next);
          t = t.next;
        }
        random = (int)(Math.random()*temp.size());
        t = (node)temp.elementAt(random);
        if(visited.contains(t))
          break;
        visited.addElement(t);
        fr.writeBytes(t.data+" ");
        System.out.print(t.data+" ");
        for(i=0; i<G.size(); i++)
        {
          if(((node)G.elementAt(i)).data.equals(t.data))
          {
            t = (node)G.elementAt(i);
            break;
          }
        }
        if(i >= G.size())
          break;
        temp.clear();
      }
      System.out.println();
      fr.writeBytes("\r\n");
      fr.close();
    }catch(IOException e){};

    return "";
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    lab1 T = new lab1();
    T.creatGraph("E:\\workspace\\lab1\\src\\lab1\\test.txt");
    T.Floyd();
    System.out.println("展示有向图（邻接表）：");
    T.showDirectedGraph();
    System.out.println();
    System.out.println("查询桥接词：");
    System.out.println(T.queryBridgeWords("most","usually"));
    System.out.println(T.queryBridgeWords("most","by"));
    System.out.println(T.queryBridgeWords("ship","train"));
    System.out.println();
    System.out.println("根据桥接词生成新文本：");
    System.out.println(T.generateNewText("most usually by ship train"));
    System.out.println(T.generateNewText("power of ago"));
    System.out.println(T.generateNewText("steam who engine"));
    System.out.println();
    System.out.println("计算最短路径：");
    System.out.println(T.calcShortestPath("and", "steam")+"handsome.");
    System.out.println(T.calcShortestPath("of", "of")+"of.");
    System.out.println(T.calcShortestPath("by", "most")+"most.");
    System.out.println();
    System.out.println("随机游走：");
    T.randomWalk();
    T.randomWalk();
    T.randomWalk();

  }

}
