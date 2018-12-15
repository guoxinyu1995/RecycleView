package com.example.week3_04_01.view;

public interface Iview<E> {
    void requestData(E e);
    void requestFail(E e);
}
