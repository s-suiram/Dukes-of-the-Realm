package game.logic.utils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * a class that allow a cleaner handling of single-call method in loops.
 */
public class SingleRunHandler implements Serializable {

    private final Set<String> flagSet;
    private final Set<SerializableRunnable> runSet;

    public SingleRunHandler() {
        flagSet = new HashSet<>();
        runSet = new HashSet<>();
    }

    /**
     * executes the runnable if it hasn't been executed before.
            * use this function for method reference or any anonymous runnable
     * @param r the runnable
     * @param name the name of the runnable used to invoke equals.
            * @return true if the method is executed on this call, false if not.
     */
    public boolean doOnce(Runnable r, String name){
        if( !flagSet.contains(name)){
            flagSet.add(name);
            r.run();
            return true;
        }
        return false;
    }

    /**
     * executes the runnable if it hasn't been executed before.
     * use this function for a runnable that have been declared and stored before,
     * as an anonymous runnable cannot be compared
     *
     * @param r the runnable
     * @return true if the method is executed on this call, false if not.
     */
    public boolean doOnce(SerializableRunnable r) {
        if (!runSet.contains(r)) {
            runSet.add(r);
            r.run();
            return true;
        }
        return false;
    }

}
