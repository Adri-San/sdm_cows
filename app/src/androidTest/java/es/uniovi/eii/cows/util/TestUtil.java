package es.uniovi.eii.cows.util;

public class TestUtil {

    /**
     * Auxiliary method that waits {millis} milliseconds.
     *
     * @param millis milliseconds that main thread will be waiting
     */
    public static void wait(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}