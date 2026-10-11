package com.example.jpa.test;

import com.example.jpa.entities.Room;
import com.example.jpa.entities.User;
import com.example.jpa.service.ManagementService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class ManagementServiceTest {

    private ManagementService service;

    @Before
    public void setUp() {
        service = new ManagementService();
    }

    @After
    public void tearDown() {
        service.close();
    }

    // Unique values avoid conflicts with the unique constraints (room name, user email)
    private String unique(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private User findUserByEmail(List<User> users, String email) {
        return users.stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }

    @Test
    public void testAddRoomGeneratesId() {
        Room room = new Room(unique("Conference"), 50);
        service.addRoom(room);

        assertNotNull("The room id should be generated after saving", room.getId());
    }

    @Test
    public void testAddedRoomAppearsInList() {
        String roomName = unique("Meeting");
        service.addRoom(new Room(roomName, 12));

        List<Room> rooms = service.listRooms();

        boolean found = rooms.stream().anyMatch(r -> roomName.equals(r.getName()));
        assertTrue("The new room should be in the list", found);
    }

    @Test
    public void testUserIsLinkedToRoom() {
        Room room = new Room(unique("Lab"), 25);
        service.addRoom(room);

        String email = unique("david") + "@example.com";
        service.addUser(new User("David", email, room));

        User saved = findUserByEmail(service.listUsers(), email);
        assertNotNull("The user should be saved", saved);
        assertEquals("David", saved.getName());
        assertNotNull(saved.getRoom());
        assertEquals(room.getName(), saved.getRoom().getName());
    }
    @Test
    public void testRoomsLargerThanIsStrict() {
        Room big = new Room(unique("Big"), 21);
        Room exact = new Room(unique("Exact"), 20);
        service.addRoom(big);
        service.addRoom(exact);

        List<Room> result = service.roomsLargerThan(20);

        assertTrue(result.stream().anyMatch(r -> r.getName().equals(big.getName())));
        assertFalse(result.stream().anyMatch(r -> r.getName().equals(exact.getName())));
    }

    @Test
    public void testSearchUsersByNameIsCaseInsensitive() {
        String token = unique("zz");
        service.addUser(new User("Jean" + token, unique("jean") + "@example.com", null));
        service.addUser(new User("Marie", unique("marie") + "@example.com", null));

        List<User> result = service.searchUsersByName(token.toUpperCase());

        assertEquals(1, result.size());
        assertEquals("Jean" + token, result.get(0).getName());
    }

    @Test
    public void testUserWithoutRoom() {
        String email = unique("emma") + "@example.com";
        service.addUser(new User("Emma", email, null));

        User saved = findUserByEmail(service.listUsers(), email);
        assertNotNull(saved);
        assertNull("A user can exist without a room", saved.getRoom());
    }

    @Test
    public void testUsersByRoomReturnsOnlyMatchingUsers() {
        Room roomA = new Room(unique("Library"), 40);
        Room roomB = new Room(unique("Cafeteria"), 60);
        service.addRoom(roomA);
        service.addRoom(roomB);

        service.addUser(new User("Frank", unique("frank") + "@example.com", roomA));
        service.addUser(new User("Grace", unique("grace") + "@example.com", roomA));
        service.addUser(new User("Henry", unique("henry") + "@example.com", roomB));

        List<User> inA = service.usersByRoom(roomA.getName());
        List<User> inB = service.usersByRoom(roomB.getName());

        assertEquals(2, inA.size());
        assertEquals(1, inB.size());
        assertEquals("Henry", inB.get(0).getName());
    }

    @Test
    public void testUsersByUnknownRoomReturnsEmptyList() {
        List<User> users = service.usersByRoom(unique("DoesNotExist"));

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }
}