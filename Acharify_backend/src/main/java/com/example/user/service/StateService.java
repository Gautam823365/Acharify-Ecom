package com.example.user.service;

import com.example.user.entity.State;
import com.example.user.repo.StateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StateService {

    @Autowired
    private StateRepository stateRepository;

    public List<State> getAllStates() {
        return stateRepository.findAll();
    }

    public State getStateById(Long id) {
        Optional<State> state = stateRepository.findById(id);
        return state.orElse(null);
    }

    public List<State> saveAllStates(List<State> states) {
        return stateRepository.saveAll(states);
    }

}
