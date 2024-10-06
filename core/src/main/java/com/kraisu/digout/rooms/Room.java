package com.kraisu.digout.rooms;

import java.util.List;

public abstract class Room {
    protected int gameId; //ID gry w jakiej znajduje się pokój
    protected int coordinateX; //pozycja poziomia na mapie
    protected int coordinateY; //pozycja pionowa na mapie
    protected int type; //baza, kamienie, skały, wyjście
    protected boolean discovered; //czy pokój został odkopany
    protected boolean ableToDiscover; //czy pokój moze być odkopany
    protected int buildUp; //nic, baza, kuchnia, restroom, winda, warsztat, elektrownia, pompa powietrza, majsterkowania
    protected boolean ableToBuild; //czy pokój można zabudować
    protected int amountOfSpace; //ile jest miejsca w pokoju
    protected List<int[]> Resources; //zasoby z odkopania
    protected List<int[]> Equipment; //ekwipunek z odkopania
    protected List<int[]> Survivors; //ocaleńcy z odkopania


    public Room(int gameId, int coordinateX, int coordinateY, int type, boolean discovered, boolean ableToDiscover, int buildUp, boolean ableToBuild, int amountOfSpace, List<int[]> resources, List<int[]> equipment, List<int[]> survivors) {
        this.gameId = gameId;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.type = type;
        this.discovered = discovered;
        this.ableToDiscover = ableToDiscover;
        this.buildUp = buildUp;
        this.ableToBuild = ableToBuild;
        this.amountOfSpace = amountOfSpace;
        this.Resources = resources;
        this.Equipment = equipment;
        this.Survivors = survivors;
    }


}
