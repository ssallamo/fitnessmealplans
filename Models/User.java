package nz.ac.cornell.fitnessmealplans.Models;

/**
 * User data container
 * basic user information schema
 * Created by HJS on 2016-05-14.
 */
public class User {

    private String userID;
    private String userPW;
    private String codeID;
    private int age;
    private double height;
    private String heightCD;
    private double weight;
    private String weightCD;
    private String genderCD;
    private String exerciseCD;
    private int dailyCalorie;

    public User(){

    }
    public User(String userID, String userPW, String codeID){
        this.userID = userID;
        this.userPW = userPW;
        this.codeID = codeID;
        this.age = 0;
        this.height = 0.0;
        this.heightCD = "00";
        this.weight = 0.0;
        this.weightCD = "00";
        this.genderCD = "0";
        this.exerciseCD = "00";
        this.dailyCalorie = 0;
    }

    public User(String userID, String userPW, String codeID, int age, double height, String heightCD, double weight, String weightCD, String genderCD, String exerciseCD, int dailyCalorie) {
        this.userID = userID;
        this.userPW = userPW;
        this.codeID = codeID;
        this.age = age;
        this.height = height;
        this.heightCD = heightCD;
        this.weight = weight;
        this.weightCD = weightCD;
        this.genderCD = genderCD;
        this.exerciseCD = exerciseCD;
        this.dailyCalorie = dailyCalorie;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getUserPW() {
        return userPW;
    }
    public void setUserPW(String userPW) {
        this.userPW = userPW;
    }
    public String getCodeID() {
        return codeID;
    }
    public void setCodeID(String codeID) {
        this.codeID = codeID;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public String getHeightCD() {
        return heightCD;
    }
    public void setHeightCD(String heightCD) {
        this.heightCD = heightCD;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public String getWeightCD() {
        return weightCD;
    }
    public void setWeightCD(String weightCD) {
        this.weightCD = weightCD;
    }
    public String getGenderCD() {
        return genderCD;
    }
    public void setGenderCD(String genderCD) {
        this.genderCD = genderCD;
    }
    public String getExerciseCD() {
        return exerciseCD;
    }
    public void setExerciseCD(String exerciseCD) {
        this.exerciseCD = exerciseCD;
    }
    public int getDailyCalorie() {
        return dailyCalorie;
    }
    public void setDailyCalorie(int calDailyKCal) {
        this.dailyCalorie = dailyCalorie;
    }

}
