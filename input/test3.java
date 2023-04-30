public class Person {
  private String name;
  private int age;

  public Person(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }
}

public class Employee extends Person {
  private String department;

  public Employee(String name, int age, String department) {
    super(name, age);
    this.department = department;
  }

  public String getDepartment() {
    return department;
  }
}

public class Customer extends Person {
  private int customerID;

  public Customer(String name, int age, int customerID) {
    super(name, age);
    this.customerID = customerID;
  }

  public int getCustomerID() {
    return customerID;
  }
}

public class BankAccount {
  private double balance;

  public BankAccount(double balance) {
    this.balance = balance;
  }

  public double getBalance() {
    return balance;
  }

  public void deposit(double amount) {
    balance += amount;
  }

  public void withdraw(double amount) {
    if (balance >= amount) {
      balance -= amount;
    }
  }
}

public class Main {
  public static void main(String[] args) {
    Employee employee = new Employee("John", 30, "Marketing");
    Customer customer = new Customer("Jane", 25, 1234);
    BankAccount account = new BankAccount(1000.0);

    System.out.println("Employee name: " + employee.getName());
    System.out.println("Employee age: " + employee.getAge());
    System.out.println("Employee department: " + employee.getDepartment());

    System.out.println("Customer name: " + customer.getName());
    System.out.println("Customer age: " + customer.getAge());
    System.out.println("Customer ID: " + customer.getCustomerID());

    System.out.println("Bank account balance: " + account.getBalance());
    account.deposit(500.0);
    System.out.println("Bank account balance after deposit: " + account.getBalance());
    account.withdraw(200.0);
    System.out.println("Bank account balance after withdrawal: " + account.getBalance());
  }
}
