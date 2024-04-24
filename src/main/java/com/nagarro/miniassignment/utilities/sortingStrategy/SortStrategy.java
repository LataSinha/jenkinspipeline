package com.nagarro.miniassignment.utilities.sortingStrategy;

import java.util.Comparator;

import com.nagarro.miniassignment.entity.User;

public interface SortStrategy {
    Comparator<User> getComparator();
}