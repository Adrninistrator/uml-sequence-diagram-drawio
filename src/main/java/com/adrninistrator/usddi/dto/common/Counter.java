package com.adrninistrator.usddi.dto.common;

/**
 * @author adrninistrator
 * @date 2024/10/2
 * @description:
 */
public class Counter {

    private int count;

    public Counter() {
        this(0);
    }

    public Counter(int count) {
        this.count = count;
    }

    public int get() {
        return count;
    }

    public int add(int value) {
        count += value;
        return count;
    }

    public void set(int value) {
        count = value;
    }

    public void trySetMax(int value) {
        if (value > count) {
            count = value;
        }
    }

    public void setZero() {
        set(0);
    }

    @Override
    public String toString() {
        return "count=" + count;
    }
}
