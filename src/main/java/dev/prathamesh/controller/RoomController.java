package dev.prathamesh.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.prathamesh.model.RoomModel;
import dev.prathamesh.service.RoomService;
import dev.prathamesh.types.CreateRoomRequest;
import dev.prathamesh.types.RoomSearchRequest;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> greet() {

        return ResponseEntity.ok("Hello From Rooms");
    }

    @PostMapping
    public ResponseEntity<RoomModel> createRoom(
            @RequestBody CreateRoomRequest room) {

        RoomModel createdRoom = roomService.createRoom(room);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdRoom);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomModel> getRoomById(
            @PathVariable Long id) {

        RoomModel room = roomService.getRoomById(id);

        return ResponseEntity.ok(room);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<RoomModel>> searchRooms(
            @ModelAttribute RoomSearchRequest request,
            Pageable pageable) {

        Page<RoomModel> rooms =
                roomService.searchRooms(request, pageable);

        return ResponseEntity.ok(rooms);
    }
}