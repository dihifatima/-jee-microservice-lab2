package com.example.jpa.service;

import com.example.jpa.dao.RoomDao;
import com.example.jpa.dao.UserDao;
import com.example.jpa.entities.Room;
import com.example.jpa.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ManagementService {

    private EntityManagerFactory emf;
    private EntityManager em;
    private RoomDao roomDao;
    private UserDao userDao;

    public ManagementService() {
        emf = Persistence.createEntityManagerFactory("managementPU");
        em = emf.createEntityManager();
        roomDao = new RoomDao(em);
        userDao = new UserDao(em);
    }

    public void close() {
        em.close();
        emf.close();
    }

    public void addRoom(Room room) {
        roomDao.create(room);
    }

    public void addUser(User user) {
        userDao.create(user);
    }

    public List<Room> listRooms() {
        return roomDao.findAll();
    }

    public List<User> listUsers() {
        return userDao.findAll();
    }

    public List<User> usersByRoom(String roomName) {
        return userDao.findByRoomName(roomName);
    }
    public List<Room> roomsLargerThan(int minCapacity) {
        return roomDao.findByCapacityGreaterThan(minCapacity);
    }

    public List<User> searchUsersByName(String text) {
        return userDao.findByNameContaining(text);
    }
}