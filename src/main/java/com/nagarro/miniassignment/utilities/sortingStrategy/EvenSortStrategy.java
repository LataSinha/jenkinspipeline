package com.nagarro.miniassignment.utilities.sortingStrategy;

import java.util.Comparator;

import com.nagarro.miniassignment.entity.User;

public class EvenSortStrategy implements SortStrategy {

    private final String sortType;

    public EvenSortStrategy(String sortType) {
        this.sortType = sortType;
    }

    @Override
    public Comparator<User> getComparator() {
        // Applying even sorting based on sortType
        switch (sortType.toLowerCase()) {
            case "name":
            	return Comparator.<User, Boolean>comparing(user -> user.getName().length() % 2 == 0, Comparator.reverseOrder())
            	        .thenComparing(Comparator.comparing(User::getName));

            case "age":
            	return Comparator.<User, Boolean>comparing(user -> user.getAge() % 2 == 0, Comparator.reverseOrder())
            	        .thenComparing(User::getAge); 	
            default:
                throw new IllegalArgumentException("Unsupported sortType: " + sortType);
        }
    }
}