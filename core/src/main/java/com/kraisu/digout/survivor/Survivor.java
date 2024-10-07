package com.kraisu.digout.survivor;

import com.badlogic.gdx.graphics.Texture;

import java.util.UUID;

public class Survivor {
    private UUID gameId;
    private final UUID id = UUID.randomUUID();
    private String name;
    private int energy;
    private int profession;
    private int equipment;
    private String profileInformation;
    private int age;
    private Texture img;


    public Survivor(UUID gameId, String name, int energy, int profession, int equipment, String profileInformation, int age, Texture img) {
        this.gameId = gameId;
        this.name = name;
        this.energy = energy;
        this.profession = profession;
        this.equipment = equipment;
        this.profileInformation = profileInformation;
        this.age = age;
        this.img = img;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getProfession() {
        return profession;
    }

    public void setProfession(int profession) {
        this.profession = profession;
    }

    public int getEquipment() {
        return equipment;
    }

    public void setEquipment(int equipment) {
        this.equipment = equipment;
    }

    public String getProfileInformation() {
        return profileInformation;
    }

    public void setProfileInformation(String profileInformation) {
        this.profileInformation = profileInformation;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Texture getImg() {
        return img;
    }

    public void setImg(Texture img) {
        this.img = img;
    }
}
