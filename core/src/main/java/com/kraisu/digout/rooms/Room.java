package com.kraisu.digout.rooms;

import com.kraisu.digout.Equimpent.Equipment;
import com.kraisu.digout.Survivor.Survivor;

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
    protected int constructionResources;
    protected int food;
    protected int tools;
    protected List<Equipment> Equipments; //ekwipunek z odkopania
    protected List<Survivor> Survivors; //ocaleńcy z odkopania

    public Room(int gameId, int coordinateX, int coordinateY, int type, boolean discovered, boolean ableToDiscover, int buildUp, boolean ableToBuild, int amountOfSpace, int constructionResources, int food, int tools, List<Equipment> equipments, List<Survivor> survivors ) {
        this.gameId = gameId;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.type = type;
        this.discovered = discovered;
        this.ableToDiscover = ableToDiscover;
        this.buildUp = buildUp;
        this.ableToBuild = ableToBuild;
        this.amountOfSpace = amountOfSpace;
        this.constructionResources = constructionResources;
        this.food = food;
        this.tools = tools;
        this.Equipments = equipments;
        this.Survivors = survivors;
    }
}
