package game.logic.utils;

import java.io.Serializable;
import java.util.HashMap;


/**
 * a class tha encapsulates the periodic execution of methods in a loop
 * (the method that does not execute every loop and have a cooldown time)
 */
public class PeriodicRunHandler implements Serializable {

    private HashMap<String, PeriodicRunner> periodicRunnerMap;

    public PeriodicRunHandler() {
        periodicRunnerMap = new HashMap<>();
    }

    /**
     * add a function
     *
     * @param r        the runnable
     * @param coolDown the cooldown in number of loop
     * @param name     the name of the runnable to identify it
     */
    public void add(SerializableRunnable r, int coolDown, String name) {
        periodicRunnerMap.put(name, new PeriodicRunner(coolDown, r));
    }


    /**
     * executes periodically ( after each cooldown ) the
     * runnable associated with the name in parameter
     * @param name the runnable name
     */
    public void doPeriodically(String name) {
        periodicRunnerMap.get(name).doPeriodically();
    }


    private static class PeriodicRunner implements Serializable {
        int cooldown;
        int counter;
        SerializableRunnable r;

        PeriodicRunner(int skipRate, SerializableRunnable r) {
            this.cooldown = skipRate;
            this.counter = 1;
            this.r = r;
        }

        void doPeriodically(){
            if(counter++ > cooldown){
                r.run();
                counter = 1;
            }
        }
    }
}
