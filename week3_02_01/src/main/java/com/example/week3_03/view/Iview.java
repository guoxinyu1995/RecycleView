package com.example.week3_03.view;

public interface Iview<E> {
    void requestData(E e);
    void requestFail(E e);
}
