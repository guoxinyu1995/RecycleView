package com.example.week3_03_01.view;

public interface Iview<E> {
    void requestData(E e);
    void requestFail(E e);
}
