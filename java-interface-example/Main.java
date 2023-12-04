// INFO: 
// This is an example of a Java interface. An interface is a way to describe behavior. For example,
// this interface defines an "add" method which returns nothing and takes in exactly one integer. In
// order for a class to implement the List interface correctly, it must provide a concrete 
// implementation of the add method which has the exact same signature (returns nothing, takes
// exactly one integer as an argument). Also note that the List interface does NOT provide any 
// implementation details of this method--that's up to the class which implements this interface
// to do. 
//
// NOTE:
// The add method in this interface can be implemented in many different ways. It need not be 
// the same across classes which implement it. For example, the add implementation for an
// ArrayList needs to append the data to the end of an internal contiguous sequence of integers. On
// the other hand, the add implementation for a LinkedList needs to create a new node and link the
// newly created node with the end of the list.

//
interface List {
  public void add(int data);

  public String toString();

  // NOTE: The following methods are commented out. Feel free to implement them as
  // an exercise.

  // public void addAll(int[] data);
  //
  // public void appendList(List list);
  //
  // public int remove();
  //
  // public int length();
}

// NOTE: The ArrayList class is a concrete implementation of the List interface.
// The syntax for
// implementing an interface in Java is `class <class-name> implements
// <interface-name>`.
class ArrayList implements List {
  // An ArrayList contains an internal contiguous sequence of integers (an array
  // of integers) and
  // the total number of integers currently in the array.
  private int[] data;
  private int length;

  public ArrayList() {
    this.data = new int[50];
    this.length = 0;
  }

  public ArrayList(int[] data) {
    this.data = data;
    this.length = data.length;
  }

  // The add implementation for an ArrayList appends an integer to the end of the
  // contiguous
  // sequence. If there's no space, then the existing items are reallocated to a
  // larger array
  // and then the data is appended.
  @Override
  public void add(int item) {
    // If there's not enough space
    if (length == data.length) {
      // Reallocate the data
      int[] newData = new int[(int) (data.length * 1.5)];
      System.arraycopy(data, 0, newData, 0, data.length);
      data = newData;
    }
    // Append the item to the end
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

// NOTE: The LinkedList class is a concrete implementation of the List
// interface.
class LinkedList implements List {

  // Just an internal node class for use in the LinkedList
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

  // The add method for a LinkedList constructs a new ListNode and links it to the
  // end of the
  // list. If head is null, head is set to the new node, otherwise, the list is
  // iterated through
  // until the final element is reached, at which point the new node is linked to
  // that one.
  //
  // NOTE: The implementation of this add method compared with the add method of
  // the ArrayList
  // are very different! However, the method signatures are the exact same, and
  // both methods
  // satisfy the List interface, which only requires that the type signature of
  // the method matches
  // the type signature of the add method defined in the List interface.
  @Override
  public void add(int data) {
    // Construct a new node
    ListNode newNode = new ListNode(data);
    if (head == null) {
      head = newNode;
    } else {
      // iterate through the list
      ListNode iter = head;
      while (iter.next != null) {
        iter = iter.next;
      }
      // ...and link the new node to the end
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

  // This method takes any concrete object which implements the List interface.
  public static void takesList(List list) {
    // Call the add method (any object which implements the List interface must
    // define the
    // add method).
    list.add(25);
    System.out.println(list);
  }
}
