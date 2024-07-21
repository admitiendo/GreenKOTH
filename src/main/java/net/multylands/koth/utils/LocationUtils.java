package net.multylands.koth.utils;

import net.multylands.koth.object.LocationPair;
import org.bukkit.Location;

public class LocationUtils {
    public static boolean checkIfIsInBetweenLocations(Location corner1, Location corner2, Location point) {
        double X1 = corner1.getX();
        double Z1 = corner1.getZ();
        double Y1 = corner1.getY();

        double X2 = corner2.getX();
        double Z2 = corner2.getZ();
        double Y2 = corner2.getY();

        double upperX = Math.max(X1, X2);
        double upperZ = Math.max(Z1, Z2);
        double upperY = Math.max(Y1, Y2);

        double lowerX = Math.min(X1, X2);
        double lowerZ = Math.min(Z1, Z2);
        double lowerY = Math.min(Y1, Y2);

        double pX = point.getX();
        double pZ = point.getZ();
        double pY = point.getY();


        return (pX <= upperX && pX >= lowerX && pZ <= upperZ && pZ >= lowerZ && pY <= upperY && pY >= lowerY);
    }

    public static boolean isLocationInside(Location loc, LocationPair pair) {
        Location top, bottom;
        int topX, topY, topZ, botX, botY, botZ, x, y, z;

        if(pair.getLocation1().getY() > pair.getLocation2().getY()) {
            top = pair.getLocation1();
            bottom = pair.getLocation2();
        } else {
            bottom = pair.getLocation1();
            top = pair.getLocation2();
        }

        topX = top.getBlockX();
        topY = top.getBlockY();
        topZ = top.getBlockZ();

        botX = bottom.getBlockX();
        botY = bottom.getBlockY();
        botZ = bottom.getBlockZ();

        x = loc.getBlockX();
        y = loc.getBlockY();
        z = loc.getBlockZ();


        if(botX > topX) { // Bottom X is largest
            if(topZ > botZ) { // Top Z is largest

                if(x >= topX && x <= botX) {
                    if(y >= botY && y <= topY) {
                        if(z >= botZ && z <= topZ) {
                            return true;
                        }
                    }
                }
            }
            else if(botZ > topZ) { // Bottom Z is largest

                if(x >= topX && x <= botX) {
                    if(y >= botY && y <= topY) {
                        if(z >= topZ && z <= botZ) {
                            return true;
                        }
                    }
                }
            }
        }
        else if(topX > botX) { // Top X is largest
            if(topZ > botZ) { // Top Z is largest

                if(x >= botX && x <= topX) {
                    if(y >= botY && y <= topY) {
                        if(z >= botZ && z <= topZ) {
                            return true;
                        }
                    }
                }
            }
            else if(botZ > topZ) { // Bottom Z is largest

                if(x >= botX && x <= topX) {
                    if(y >= botY && y <= topY) {
                        if(z >= topZ && z <= botZ) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

}
