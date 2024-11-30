package com.javaoop.smarthome;

public class UserSingleton {
    private static UserSingleton instance;
    private Users user;

    private UserSingleton() {
        user = new Users(); // Khởi tạo đối tượng Users
    }

    public static UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
