package com.example.jpa.dao;

import com.example.jpa.entities.User;
import javax.persistence.EntityManager;
import java.util.List;

public class UserDao extends GenericDao<User> {

    public UserDao(EntityManager em) {
        super(User.class, em);
    }

    public List<User> findByRoomName(String roomName) {
        return em.createQuery("SELECT u FROM User u WHERE u.room.name = :roomName", User.class)
                .setParameter("roomName", roomName)
                .getResultList();
    }
    public List<User> findByNameContaining(String text) {
        return em.createQuery(
                        "SELECT u FROM User u WHERE LOWER(u.name) LIKE :pattern", User.class)
                .setParameter("pattern", "%" + text.toLowerCase() + "%")
                .getResultList();
    }
}