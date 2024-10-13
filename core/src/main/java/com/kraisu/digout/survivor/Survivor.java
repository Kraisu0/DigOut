package com.kraisu.digout.survivor;

import com.badlogic.gdx.graphics.Texture;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.stuff.Equipment;

import java.util.UUID;

public class Survivor {
    private UUID gameId;
    private final UUID id = UUID.randomUUID();
    private String name;
    private int energy;
    private Constants.Survivors profession;
    private Equipment equipment;
    private String profileInformation;
    private int age;
    private Texture img;

    public Survivor(UUID gameId, String name, int energy, Constants.Survivors profession, Equipment equipment, String profileInformation, int age, Texture img) {
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

    public Constants.Survivors getProfession() {
        return profession;
    }

    public void setProfession(Constants.Survivors profession) {
        this.profession = profession;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
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

    public void reduceEnergy(int amount) {
        this.energy = Math.max(this.energy - amount, Constants.SurvivorLimitations.MIN_SURVIVOR_ENERGY);
    }

    public void increaseEnergy(int amount) {
        if(this.energy < Constants.SurvivorLimitations.MAX_SURVIVOR_ENERGY) {
            this.energy = this.energy + amount;
        }else{
            this.energy = Constants.SurvivorLimitations.MAX_SURVIVOR_ENERGY;
        }
    }

    public void giveEquipment(Equipment equipment){
        if(this.equipment == null){
            this.equipment = equipment;
        }else{
            System.out.println("Nie można dodać: " + equipment.getName() + ", ponieważ ocalały ma już ekwipunek: " + this.equipment.getName());
        }
    }

    @Override
    public String toString() {
        return "Survivor{" +
            "gameId=" + gameId +
            ", id=" + id +
            ", name='" + name + '\'' +
            ", energy=" + energy +
            ", profession=" + profession +
            ", equipment=" + equipment +
            ", profileInformation='" + profileInformation + '\'' +
            ", age=" + age +
            ", img=" + img +
            '}';
    }
}
