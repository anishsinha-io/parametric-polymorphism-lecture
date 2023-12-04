# Parametric Polymorphism

Before going over _parametric_ polymorphism, let's first discuss _polymorphism_ (with LOTS of examples!!). The dictionary definition will be something along the lines of:

- Polymorphism is the ability to create a property, function, or object that has more than one implementation, or actualization.
- Polymorphism is an ability to substitute things that have common functionality in the sense that they have the same methods and data.

Let's look at a real world example:

If you can drive one car, you can drive all of them. All cars expose the same _interface_:

- A car can steer
- A car can reverse
- A car can drive forward

(etc.)

However, the implementation of steering, reversing, and driving forward may differ across different car brands, makes, and models. For example, a Ferrari will probably have a much more fine tuned implementation for steering than a Chrysler. Although they expose the same _interface_, their _implementation details_ differ.

Let's look at this in the context of a real programming language. Here is (a simplified version of) the List interface in Java which works with integers.

```java
public interface List {
  void add(int);
  void addAll(int[]);
  void appendList(List);
  int remove();
  int length();
}
```

This interface contains a set of method headers. If a class wants to implement the interface List, it must implement each of these methods. Let's look at two different classes which both implement the List interface: `ArrayList` and `LinkedList`.

```java
public class ArrayList implements List {
  int[] data;
  int length;

  public void add(int item) {
    if(length == data.length) {
      int[] newData = new int[data.length * 1.5];
      System.arraycopy(arr1, 0, new_data, 0, arr1.length);
      data = newData;
    }
  }
  // assume the other methods are implemented below...
}

public class LinkedList implements List {
  private class ListNode {
    int data;
    ListNode next;
    ListNode prev;

    public ListNode(int data) {
      this.data = data;
      this.next = null;
      this.prev = null;
    }
  }

  private ListNode head;
  private int length;

  public void add(int item) {
    ListNode newNode = new ListNode(item);
    if(length == 0) {
      head = newNode;
    } else {
      ListNode iter = head;
      while(iter != null) {
        iter = iter.next;
      }
      iter.next = newNode;
      newNode.prev = iter;
    }
  }
  // assume the other methods are implemented below
}
```

Observe how, even though both the `ArrayList` class and the `LinkedList` class expose an `add` method, that the implementations are entirely different. However, both satisfy the `List` interface, so both `ArrayList` and `LinkedList` can be used when an argument of type `List` is required.

For example:

```java
public class Main {
  public static void main(String[] args) {
    ArrayList l1 = new ArrayList();
    LinkedList l2 = new LinkedList();

    takesList(l1);
    takesList(l2);
  }

  public static void takesList(List list) {
    // do something with list
  }
}
```

The `takesList` method can take anything that implements the `List` interface, so passing either a `LinkedList` or an `ArrayList` both work.

This is the core principle behind polymorphism. The `List` interface can have _any number_ of concrete implementations. The _functionality_ is what matters, and any concrete class which implements `List` can be substituted for any other class which also implements `List`.

Now lets discuss _parametric_ polymorphism. Parametric polymorphism allows a single piece of code to take one or more _type parameters_ which allow for specialization of that code based on the type passed in.

This definition is not particularly easy to understand, so let's look into an example of where this might be useful. What better place to start than a language _without_ parametric polymorphism: C. Let's look at the following implementation of a stack of integers.

```C
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>

typedef struct int_stack_frame {
  struct stack_frame* next;
  int data;
} stack_frame;

typedef struct int_stack {
  int num_frames;
  stack_frame* top;
} stack;

int_stack_frame* new_frame(int data) {
  int_stack_frame* sf = malloc(sizeof(stack_frame));
  sf->data = data;
  sf->next = NULL;
  return sf;
}

int_stack* new_stack() {
  int_stack* s = malloc(sizeof(stack));
  s->num_frames = 0;
  s->top = NULL;
  return s;
}

void push(int_stack* s, int data) {
  int_stack_frame* sf = new_frame(dat);
  sf->next = s->top;
  s->top = sf;
}

int pop(int_stack* s, int* buf) {
  if(s->num_frames < 1) {
    return -1
  }

  data = s->top->data;
  s->top = s->top->next;
  s->num_frames--;
  *buf = data;
  return 0;
}

```

This example implements a stack of integers in C with push and pop functions. Now consider the case where we need a stack of doubles (not integers). What do we do? We can't exactly reuse this code, can we, because it is specialized for integers! There are two possible solutions in C: duplicate the code (write an implementation for a `double_stack` which essentially just copies int_stack but replaces instances of `int` with instances of `double`), or use void pointers (at the expense of type safety). Neither of these are good options. Duplicating the code violates the DRY (Don't Repeat Yourself) principle which is a code smell, and using void pointers means you must cast back to the pointer type you desire and then dereference it, which leads to a lot of opportunities for bugs. Wouldn't it be better if we could just specify a type parameter which allows us to specialize a piece of code as needed? Consider the following code in C++ (only using templates--not using ANY other advanced features of C++).

```c++

template<typename T>
struct stack_frame {
  struct stack_frame* next;
  T data; // notice this line!! We are saying that the type of data is based on a type *parameter* that
          // is passed in!

  stack_frame(T data) {
    this.data = data;
  }
};

template<typename T>
struct stack {
  struct stack_frame<T>* top;
  int num_frames;

  stack() {
    this.num_frames = 0;
    this.top = nullptr;
  }

  void push(T data) {
    stack_frame frame = new stack_frame(data);
    frame->next = top;
    num_frames++;
  }

  int pop(T* buf) {
    if (num_frames == 0) {
      return -1;
    }
    *buf = *top->data;
    top = top->next;
    num_frames--;
    return 0;
  }
};

```

This stack now has a _type parameter_ and we can specialize a stack with a _type_. For example, we can now do the following:

```c++
int main() {
  stack<int> s1 = stack();
  stack<float> s2 = stack();
  stack<double> s3 = stack();
  return 0;
}
```

We do not need to duplicate code! And we also get the full advantages of type safety. The way this works under the hood, is that the C++ compiler, whenever it comes across a specific implementation (like `stack<int>`), will _generate_ an implementation for you. For example, when it comes across a `stack<int>` it will generate the necessary code for the stack to work with integers.

This process (of generating specialized code) is called _monomorphization_.
