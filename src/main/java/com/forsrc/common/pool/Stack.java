package com.forsrc.common.pool;

import java.util.LinkedList;
import java.util.ListIterator;

public class Stack<T> {

  private LinkedList<T> list = new LinkedList<>();

  public void push(T v) {
    list.addLast(v);
  }

  public T peek() {
    return list.peekLast();
  }

  public T pop() {
    return list.pollLast();
  }

  public boolean isEmpty() {
    return list.isEmpty();
  }

  public ListIterator<T> getIterator() {
    return list.listIterator(list.size());
  }

  public ListIterator<T> getIteratorFirst() {
    return list.listIterator();
  }

  private void loop() {
    int size = list.size();
    ListIterator<T> listIterator = list.listIterator(size);
    while (listIterator.hasPrevious()) {
      T t = listIterator.previous();
    }
  }

  private void loopFirst() {
    ListIterator<T> listIterator = list.listIterator();
    while (listIterator.hasNext()) {
      T t = listIterator.next();
    }
  }

}