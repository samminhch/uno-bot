package com.marshie.util;

public class Chain <K,V>{
    private K key;
    private V val;

    public Chain(K k, V v) {
        key = k;
        val = v;
    }

    public K getKey(){
        return key;
    }

    public V getVal(){
        return val;
    }

    public void setKey(K k) {
        key = k;
    }

    public void setVal(V v) {
        val = v;
    }
}
