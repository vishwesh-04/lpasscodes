package Mock_I;

import java.util.concurrent.Semaphore;

class BarberShop {
    private final Semaphore customers = new Semaphore(0);
    private final Semaphore barber = new Semaphore(0);
    private final Semaphore mutex = new Semaphore(1);
    private int waitingCustomers = 0;
    private final int maxChairs;

    public BarberShop(int maxChairs) {
        this.maxChairs = maxChairs;
    }

    public void getHaircut(int customerId) throws InterruptedException {
        mutex.acquire();
        if (waitingCustomers < maxChairs) {
            waitingCustomers++;
            System.out.println("Customer " + customerId + " is waiting.");
            mutex.release();
            customers.release(); 
            barber.acquire(); 
            getHaircutAction(customerId);
        } else {
            System.out.println("Customer " + customerId + " leaves (no chairs available).");
            mutex.release();
        }
    }

    public void getHaircutAction(int customerId) {
        System.out.println("Customer " + customerId + " is getting a haircut.");
        try {
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Customer " + customerId + " is done with the haircut.");
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        waitingCustomers--;
        if (waitingCustomers > 0) {
            customers.release(); 
        }
        barber.release(); 
        mutex.release();
    }

    public void barberWork() throws InterruptedException {
        while (true) {
            customers.acquire(); 
            mutex.acquire();
            waitingCustomers--;
            System.out.println("Barber is cutting hair.");
            mutex.release();
            barber.release(); 
            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Barber is done cutting hair.");
        }
    }
}

class Customer extends Thread {
    private final BarberShop shop;
    private final int id;

    public Customer(BarberShop shop, int id) {
        this.shop = shop;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            shop.getHaircut(id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class SleepingBarber {
    public static void main(String[] args) {
        final int MAX_CHAIRS = 3;
        final int NUM_CUSTOMERS = 10;

        BarberShop shop = new BarberShop(MAX_CHAIRS);
        
        Thread barberThread = new Thread(() -> {
            try {
                shop.barberWork();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        barberThread.start();

        for (int i = 1; i <= NUM_CUSTOMERS; i++) {
            new Customer(shop, i).start();
            try {
                Thread.sleep((int)(Math.random() * 2000)); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
