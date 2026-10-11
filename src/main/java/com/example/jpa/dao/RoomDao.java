package com.example.jpa.dao;

import com.example.jpa.entities.Room;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class RoomDao extends GenericDao<Room> {

    public RoomDao(EntityManager em) {
        super(Room.class, em);
    }

    public List<Room> findByMinCapacity(int minCapacity) {
        return em.createQuery("SELECT r FROM Room r WHERE r.capacity >= :minCapacity", Room.class)
                .setParameter("minCapacity", minCapacity)
                .getResultList();
    }


    public List<Room> findByCapacityGreaterThan(int minCapacity) {
        TypedQuery<Room> query = em.createQuery(
                "SELECT r FROM Room r WHERE r.capacity > :minCapacity", Room.class);
        query.setParameter("minCapacity", minCapacity);
        return query.getResultList();
    }
}