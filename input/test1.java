public class A {
    public static void main(String[] args) {
        B b = new B();
        b.foo();
    }

    public void bar() {
        System.out.println("A.bar() called");
    }
}

class B {
    public void foo() {
        A a = new A();
        a.bar();
    }
}

