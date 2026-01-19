package com.example.user.controller;

import com.example.user.entity.State;
import com.example.user.service.StateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/states")
public class StateController {

    @Autowired
    private StateService stateService;

    // Get all states
    @GetMapping
    public ResponseEntity<List<State>> getAllStates() {
        List<State> states = stateService.getAllStates();
        return ResponseEntity.ok(states);
    }

    // Get state by ID
    @GetMapping("/{id}")
    public ResponseEntity<State> getStateById(@PathVariable Long id) {
        State state = stateService.getStateById(id);
        if (state != null) {
            return ResponseEntity.ok(state);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new state
    @PostMapping("/batch")
    public ResponseEntity<List<State>> createStates(@RequestBody List<State> states) {
        List<State> createdStates = stateService.saveAllStates(states);
        return ResponseEntity.ok(createdStates);
    }

}
