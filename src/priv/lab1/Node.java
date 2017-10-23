package priv.lab1;

class Node {
  String data;
  int weight;
  Node next;

  Node(String str) {
    data = str;
    weight = 1;
    next = null;
  }
}