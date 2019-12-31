package game;

import game.logic.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FirstNameDico {
    private static Scanner file;
    private static int size;

    static {
        reset();
        size = 0;
        while (file.hasNextLine()) {
            file.nextLine();
            size++;
        }
        reset();
    }

    public static void reset() {
        try {
            file = new Scanner(new File("resources/name-dico.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getName(int index) {
        for (int i = 0; i < index; i++)
            file.nextLine();
        String line = file.nextLine();
        reset();
        return line;
    }

    public static String getRandName() {
        return getName(World.rand(0, size));
    }
}
