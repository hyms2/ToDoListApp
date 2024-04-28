package main.todolist.model;

public class UserInfo {
    private int userId;
    private static String username;
//    private String password;

    public UserInfo (String username){
        this.username = username;
//        this.password = password;
    }

    public Integer getUserId(){return userId;}
    public static String getUsername(){return username;}
//    public String getPassword(){return password;}

//    public void setUserId(Integer userId){this.userId = userId;}
//    public void setUsername(String username){this.username = username;}
//    public void setPassword(String password){this.password = password;}
}
