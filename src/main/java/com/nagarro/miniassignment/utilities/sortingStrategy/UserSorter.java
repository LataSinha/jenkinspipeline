package com.nagarro.miniassignment.utilities.sortingStrategy;

import java.util.List;

import com.nagarro.miniassignment.entity.User;

public class UserSorter {

    private final SortStrategy sortStrategy;

    public UserSorter(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public List<User> sortUsers(List<User> users) {
        users.sort(sortStrategy.getComparator());
        System.out.println("Sorted List: " + users);
        return users;
    }
}