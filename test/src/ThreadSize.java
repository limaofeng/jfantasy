public class ThreadSize {

    public static class TestRunnable implements Runnable{

        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @org.junit.Test
    public void test(){
        for(int i=0;i<10000;i++){
            new Thread(new TestRunnable()).start();
            System.out.print(" thread " + i);
        }
    }

}
