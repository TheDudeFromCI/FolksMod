package me.ci.folks.ai.commands;

public class UnknownCommandException extends Exception {
    public UnknownCommandException(String name) {
        super("Unknown command: " + name + "!");
    }
}
