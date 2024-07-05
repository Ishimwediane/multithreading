class Task implements Runnable {
    public static double balance = 1000; // Initial balance of 1000
    private boolean deposit;
    private double amount;

    public Task(boolean deposit, double amount) {
        this.deposit = deposit;
        this.amount = amount;
    }

    public static synchronized void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println(Thread.currentThread().getName() + " deposited: " + amount + ", Balance: " + balance);
        }
    }

    public static synchronized void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " withdrew: " + amount + ", Balance: " + balance);
        } else {
            System.out.println(Thread.currentThread().getName() + " attempted to withdraw: " + amount + " (Insufficient funds), Balance: " + balance);
        }
    }

    @Override
    public void run() {
        if (deposit) {
            deposit(amount);
        } else {
            withdraw(amount);
        }
    }
}
public class Main {
    public static void main(String[] args) {
        // Create tasks for deposit and withdrawal
        Task depositTask1 = new Task(true, 200);
        Task withdrawTask1 = new Task(false, 150);
        Task withdrawTask2 = new Task(false, 500);
        Task depositTask2 = new Task(true, 300);
        Task withdrawTask3 = new Task(false, 800); // This will fail due to insufficient funds

        // Create threads for each task
        Thread thread1 = new Thread(depositTask1, "Thread 1");
        Thread thread2 = new Thread(withdrawTask1, "Thread 2");
        Thread thread3 = new Thread(withdrawTask2, "Thread 3");
        Thread thread4 = new Thread(depositTask2, "Thread 4");
        Thread thread5 = new Thread(withdrawTask3, "Thread 5");

        // Start threads
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

        // Join threads to ensure the main thread waits for their completion
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All transactions are completed. Final balance: " + Task.balance);
    }
}
