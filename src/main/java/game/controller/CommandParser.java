package game.controller;

import game.logic.Cardinal;
import game.logic.World;
import game.logic.troop.TroopType;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CommandParser {

    static Scanner c = new Scanner(System.in);

    static Map<String, Consumer<List<String>>> actions = new HashMap<>();

    static {
        actions.put("playerlist", CommandParser::playerList);
        actions.put("fighteradd", CommandParser::addFighting);
        actions.put("neutraladd", CommandParser::addNeutral);
        actions.put("castleadd", CommandParser::addCastle);
        actions.put("castlelist", CommandParser::listCastle);
        actions.put("castlelistall", CommandParser::listAllCastle);
        actions.put("step", CommandParser::step);
        actions.put("help", CommandParser::help);
        actions.put("troopform", CommandParser::formTroop);
        actions.put("levelup", CommandParser::levelUp);
    }

    public static void main(String[] args_) {
        do {
            System.out.print("Dukes of the Realm > ");
            String line = c.nextLine();
            List<String> args = Arrays.stream(line.trim().split(" ")).map(String::toLowerCase).collect(Collectors.toList());
            if (args.get(0).equals("exit")) break;
            actions.getOrDefault(args.get(0), CommandParser::notACommand).accept(args);
        } while (true);
    }

    private static void levelUp(List<String> args) {
        try {
            if (!World.getInstance().castleById(Integer.parseInt(args.get(1))).get().startLevelUp())
                System.out.println("Not enough money to level up");
        } catch (IndexOutOfBoundsException e) {
            invalidArgs(args.get(0));
        } catch (NumberFormatException e) {
            invalidArgs(args.get(0), e.getMessage());
        } catch (NoSuchElementException e) {
            invalidArgs(args.get(0), "Invalid castle id");
        }
    }

    // troopform castleid troopname nbtroop
    private static void formTroop(List<String> args) {
        try {
            World.getInstance().castleById(Integer.parseInt(args.get(1))).get().produce(TroopType.fromString(args.get(2)));
        } catch (IndexOutOfBoundsException e) {
            invalidArgs(args.get(0));
        } catch (NumberFormatException e) {
            invalidArgs(args.get(0), e.getMessage());
        } catch (NoSuchElementException e) {
            invalidArgs(args.get(0), "Invalid castle id");
        }
    }

    private static void listAllCastle(List<String> args) {
        World.getInstance().getCastles().forEach(System.out::println);
    }

    static void listCastle(List<String> args) {
        try {
            World.getInstance().getPlayer(args.get(1))
                    .orElseThrow(() -> new IllegalArgumentException("The player " + args.get(1) + " doesn't exist"))
                    .getCastles().forEach(System.out::println);
        } catch (IndexOutOfBoundsException e) {
            invalidArgs(args.get(0));
        } catch (IllegalArgumentException e) {
            invalidArgs(args.get(0), e.getMessage());
        }
    }

    static void step(List<String> args) {
        if (args.size() > 1) {
            try {
                for (int i = 0; i < Integer.parseInt(args.get(1)); i++) World.getInstance().castleStep();
            } catch (NumberFormatException e) {
                invalidArgs(args.get(0), "First arg is not a number");
            }
            return;
        }
        World.getInstance().castleStep();
    }

    static void help(List<String> args) {
        actions.keySet().forEach(System.out::println);
    }

    static void addCastle(List<String> args) {
        try {
            World.getInstance().getPlayer(args.get(1)).ifPresent(player -> player.addCastle(Cardinal.fromString((args.get(2)))));
        } catch (IndexOutOfBoundsException e) {
            invalidArgs(args.get(0));
        } catch (IllegalArgumentException e) {
            invalidArgs(args.get(0), e.getMessage());
        }
    }

    static void addFighting(List<String> args) {
        try {
            World.getInstance().addFightingDukes(args.get(1));
        } catch (IndexOutOfBoundsException e) {
            invalidArgs(args.get(0));
        }
    }

    static void addNeutral(List<String> args) {
        try {
            World.getInstance().addNeutralDukes(args.get(1));
        } catch (IndexOutOfBoundsException e) {
            invalidArgs(args.get(0));
        }
    }

    static void playerList(List<String> args) {
        World.getInstance().getPlayers().forEach(System.out::println);
    }

    static void notACommand(List<String> args) {
        System.out.println(args.get(0) + " is not a valid command");
    }

    static void invalidArgs(String name) {
        System.out.println("Invalid args for " + name);
    }

    static void invalidArgs(String name, String desc) {
        System.out.println("Invalid args for " + name);
        System.out.println(desc);
    }
}
