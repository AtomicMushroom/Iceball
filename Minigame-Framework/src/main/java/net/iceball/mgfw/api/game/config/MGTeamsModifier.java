package net.iceball.mgfw.api.game.config;

import net.iceball.mgfw.api.arena.MGTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Floris on 10-01-16.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
public interface MGTeamsModifier extends MGTeam {

    /**
     * Disables Suddendeath!
     * The player will not be set in spectator mode after he dies.
     */
    static MGTeamsModifier disableSuddendeath(MGTeamsModifier unactiveMGTeam) {
        unactiveMGTeam.isSuddendeath(false);
        return unactiveMGTeam;
    }
    /**
     * Enables Suddendeath!
     * The player will be set in spectator mode after he dies.
     */
    static MGTeamsModifier enableSuddendeath(MGTeamsModifier unactiveMGTeam) {
        unactiveMGTeam.isSuddendeath(true);
        return unactiveMGTeam;
    }

    /**
     * Enables the mainstartspawn!
     *
     * All players of all teams will spawn at the first set location at the start of the game!
     */
    static MGTeamsModifier enableMainStartSpawn(MGTeamsModifier unactiveMGTeam){
        unactiveMGTeam.isMainStartSpawn(true);
        return unactiveMGTeam;
    }
    /**
     * Disables the mainstartspawn!
     *
     * All players of all teams will spawn at a random location of their own spawns at the start of the game!
     */
    static MGTeamsModifier disableMainStartSpawn(MGTeamsModifier unactiveMGTeam){
        unactiveMGTeam.isMainStartSpawn(false);
        return unactiveMGTeam;
    }

    /**
     * Enables all teams friendlyfire!
     *
     * All your players of all teams will be able to hit each other during the game.
     */
    static MGTeamsModifier disableFriendlyFire(MGTeamsModifier unactiveMGTeam){
        unactiveMGTeam.setAllowFriendlyFire(false);
        return unactiveMGTeam;
    }
    /**
     * Disables all teams friendlyfire!
     *
     * All your players of all teams will unable able to hit each other during the game.
     */
    static MGTeamsModifier enableFriendlyFire(MGTeamsModifier unactiveMGTeam){
        unactiveMGTeam.setAllowFriendlyFire(true);
        return unactiveMGTeam;
    }

    static ListIterator<MGTeamsModifier> getIterator(List<MGTeam> teams) {
        List<MGTeamsModifier> teamsModifier = new ArrayList<>();
        for (MGTeam team : teams){
            MGTeamsModifier T = (MGTeamsModifier) team;
            teamsModifier.add(T);
        }
        return teamsModifier.listIterator();
    }
}
