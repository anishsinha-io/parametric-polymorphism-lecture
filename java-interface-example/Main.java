interface List {
  public void add(int data);

  // public void addAll(int[] data);
  //
  // public void appendList(List list);
  //
  // public int remove();
  //
  // public int length();
}

class ArrayList implements List {
  int[] data;
  int length;

  public ArrayList() {
    this.data = new int[50];
    this.length = 0;
  }

  public ArrayList(int[] data) {
    this.data = data;
    this.length = data.length;
  }

  @Override
  public void add(int item) {
    if (length == data.length) {
      int[] newData = new int[(int) (data.length * 1.5)];
      System.arraycopy(data, 0, newData, 0, data.length);
      data = newData;
    }
    data[length++] = item;
  }

  @Override
  public String toString() {
    String out = "ArrayList [ ";
    for (int i = 0; i < length; i++) {
      out = out + data[i] + " ";
    }
    out += "]";
    return out;
  }

}

class LinkedList implements List {

  private class ListNode {
    int data;
    ListNode prev;
    ListNode next;

    public ListNode(int data) {
      this.data = data;
      this.next = null;
      this.prev = null;
    }
  }

  private ListNode head;

  public LinkedList() {
    head = null;
  }

  public LinkedList(int[] data) {
    for (int item : data) {
      add(item);
    }
  }

  @Override
  public void add(int data) {
    ListNode newNode = new ListNode(data);
    if (head == null) {
      head = newNode;
    } else {
      ListNode iter = head;
      while (iter.next != null) {
        iter = iter.next;
      }
      iter.next = newNode;
      newNode.prev = iter;
    }
  }

  public String toString() {
    String out = "LinkedList [ ";
    ListNode iter = head;
    while (iter != null) {
      out = out + iter.data + " ";
      iter = iter.next;
    }
    out += "]";
    return out;
  }

}

public class Main {
  public static void main(String[] args) {
    // NOTE:
    // Both l1 and l2 are objects that satisfy the List interface,
    // although their implementations for the add method differ!
    // This is what polymorphism is. One interface may have any
    // number of concrete implementations. Both the ArrayList and
    // LinkedList classes are examples of concrete implementations
    // of the List interface;
    List l1 = new ArrayList(new int[] { 1, 2, 3, 4, 5 });
    List l2 = new LinkedList(new int[] { 6, 7, 8, 9, 10 });

    takesList(l1);
    takesList(l2);
  }

  public static void takesList(List list) {
    list.add(25);
    System.out.println(list);
  }
}
