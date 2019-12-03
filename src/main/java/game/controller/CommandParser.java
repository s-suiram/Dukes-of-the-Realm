package game.controller;

import game.logic.Cardinal;
import game.logic.World;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CommandParser {

    static Scanner c = new Scanner(System.in);

    static Map<String, Consumer<List<String>>> actions = new HashMap<>();

    static {
        actions.put("playerlist", CommandParser::playerList);
        actions.put("playeradd", CommandParser::addPlayer);
        actions.put("castleadd", CommandParser::addCastle);
        actions.put("castlelist", CommandParser::listCastle);
        actions.put("castlelistall", CommandParser::listAllCastle);
        actions.put("step", CommandParser::step);
    }

    public static void main(String[] args_) {
        addPlayer(Arrays.asList("playeradd", "max"));
        addCastle(Arrays.asList("castleadd", "max", "north"));
        do {
            System.out.print("Dukes of the Realm > ");
            String line = c.nextLine();
            List<String> args = Arrays.stream(line.trim().split(" ")).map(String::toLowerCase).collect(Collectors.toList());
            if (args.get(0).equals("exit")) break;
            actions.getOrDefault(args.get(0), CommandParser::notACommand).accept(args);
        } while (true);
    }

    private static void listAllCastle(List<String> args) {
        World.getInstance().getCastles().forEach(System.out::println);
    }

    static void listCastle(List<String> args) {
        boolean bad = args.size() < 2;
        if (bad) {
            invalidArgs(args.get(0));
            return;
        }
        World.getInstance().getPlayer(args.get(1)).ifPresent(player -> player.getCastles().forEach(System.out::println));
    }

    static void step(List<String> args) {
        if (args.size() > 1) {
            for (int i = 0; i < Integer.parseInt(args.get(1)); i++) World.getInstance().step();
            return;
        }
        World.getInstance().step();
    }

    static void addCastle(List<String> args) {
        boolean bad = args.size() < 3 || Arrays.stream(Cardinal.values()).map(Enum::toString).map(String::toLowerCase).noneMatch(s -> s.equals(args.get(2)));
        if (bad) {
            invalidArgs(args.get(0));
            return;
        }
        World.getInstance().getPlayer(args.get(1)).ifPresent(player -> player.addCastle(Cardinal.fromString((args.get(2)))));
    }

    static void addPlayer(List<String> args) {
        boolean bad = args.size() < 2;
        if (bad) {
            invalidArgs(args.get(0));
            return;
        }

        World.getInstance().addPlayer(args.get(1));
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
}
