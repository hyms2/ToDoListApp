package main.todolist.model;

public class UserInfo {
    private int userId;
    private static String username;

    public UserInfo (String username){
        this.username = username;
    }

    public Integer getUserId(){return userId;}
    public static String getUsername(){return username;}
}
