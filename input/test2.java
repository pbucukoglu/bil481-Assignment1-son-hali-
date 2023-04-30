package com.test;

public class A {
    public static void main(String[] args) {
        B b = new B();
        b.m1();
    }

    public void m1() {
        C c = new C();
        c.m1();
    }
}

class B {
    public void m1() {
        A a = new A();
        a.m1();
        C c = new C();
        c.m2();
    }

    public void m2() {
        C c = new C();
        c.m1();
    }
}

class C {
    public void m1() {
        A a = new A();
        a.m1();
    }

    public void m2() {
        B b = new B();
        b.m2();
    }
}
