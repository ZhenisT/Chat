public class test {
    public static void main(String[] args) {
        long t = System.currentTimeMillis()+30000;
        System.out.println(t + " " + System.currentTimeMillis());
        if (t > System.currentTimeMillis()){
            System.out.println(true);
        }
    }
}
