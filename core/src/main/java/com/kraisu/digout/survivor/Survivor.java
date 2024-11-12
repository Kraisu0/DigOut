package com.kraisu.digout.survivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.stuff.Equipment;

import java.util.UUID;

public class Survivor {
    private UUID gameId;
    private String name;
    private int energy;
    private Constants.Survivors profession;
    private Equipment equipment;
    private String profileInformation;
    private int age;
    private String imgPath;
    private Texture img;
    private Task task;

    public Survivor(UUID gameId, String name, int energy, Constants.Survivors profession, Equipment equipment, String profileInformation, int age, String avatarPath) {
        this.gameId = gameId;
        this.name = name;
        this.energy = energy;
        this.profession = profession;
        this.equipment = equipment;
        this.profileInformation = profileInformation;
        this.age = age;
        this.img = new Texture(avatarPath);
        this.task = null;
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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Texture getImg() {
        return img;
    }

    public void setImg(Texture img) {
        this.img = img;
    }

    public void setImgFromPath(String path){
        this.img = new Texture(path);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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

    public Drawable getIconDrawable() {
        return new TextureRegionDrawable(new TextureRegion(img));
    }

    public Drawable getEnergyIconDrawable() {
        Texture energyTexture = new Texture(Gdx.files.internal("energy/ENERGY_" + energy + ".png"));
        return new TextureRegionDrawable(new TextureRegion(energyTexture));
    }

    public void changeImgForWork(){
        String[] parts = imgPath.split("\\.", 2);
        String path = parts[0] + ".work.png";
        setImgPath(path);
        setImgFromPath(path);
    }

    public void changeImgForNotWork(){
        String[] parts = imgPath.split("\\.", 3);
        String path = parts[0] + ".png";
        setImgPath(path);
        setImgFromPath(path);
    }

    @Override
    public String toString() {
        return "Survivor{" +
            "gameId=" + gameId +
            ", name='" + name + '\'' +
            ", energy=" + energy +
            ", profession=" + profession +
            ", equipment=" + equipment +
            ", profileInformation='" + profileInformation + '\'' +
            ", age=" + age +
            ", img=" + img +
            ", task=" + task +
            '}';
    }
}
