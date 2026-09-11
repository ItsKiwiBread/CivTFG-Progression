package net.itskiwibread.civtfg_progression.progression;

public record ItemRule(
        int goal,
        boolean blockUse,
        boolean dropOnBlockedUse
) {
}