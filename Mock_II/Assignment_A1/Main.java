package Mock_I.Assignment_A1;


public class Main {
    public static void main(String[] args) {
        PassI passI = new PassI();
        passI.create_req_tables();
        passI.perform();
    }
}