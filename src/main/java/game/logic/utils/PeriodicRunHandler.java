package game.logic.utils;

import java.util.HashMap;


/**
 * a class tha encapsulates the periodic execution of methods in a loop
 * (the method that does not execute every loop and have a cooldown time)
 */
public class PeriodicRunHandler {

    private HashMap<String, PeriodicRunner> periodicRunnerMap;

    public PeriodicRunHandler() {
        periodicRunnerMap = new HashMap<>();
    }

    /**
     * add a function
     * @param r the runnable
     * @param coolDown the cooldown in number of loop
     * @param name the name of the runnable to identify it
     */
    public void add(Runnable r, int coolDown, String name){
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


    private static class PeriodicRunner {
        int cooldown;
        int counter;
        Runnable r;

        PeriodicRunner(int skipRate, Runnable r) {
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
