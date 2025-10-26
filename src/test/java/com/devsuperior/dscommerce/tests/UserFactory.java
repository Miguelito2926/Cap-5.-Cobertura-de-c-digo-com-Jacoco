package com.devsuperior.dscommerce.tests;

import com.devsuperior.dscommerce.entities.Role;
import com.devsuperior.dscommerce.entities.User;

import java.time.LocalDate;

public class UserFactory {

    public static User createClientUser() {

        User user = new User(
                1L,
                "Maria Brown",
                "maria@gmail.com",
                "988888888",
                 LocalDate.of(2001, 7, 25),
                "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO"
        );

        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createAdmintUser() {

        User user = new User(
                2L,
                "Alex Brown",
                "alex@gmail.com",
                "987777777",
                LocalDate.of(1999, 10, 20),
                "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO"
        );

        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static User createCustomAdmintUser(Long id, String username) {

        User user = new User(
                 id,
                 username,
                "alex@gmail.com",
                "987777777",
                 LocalDate.of(1999, 10, 20),
                "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO"
        );

        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static User createCustomClientUser(Long id, String username) {

        User user = new User(
                 id,
                 username,
                "maria@gmail.com",
                "988888888",
                 LocalDate.of(2001, 7, 25),
                "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO"
        );

        user.addRole(new Role(id, "ROLE_CLIENT"));
        return user;
    }
}
