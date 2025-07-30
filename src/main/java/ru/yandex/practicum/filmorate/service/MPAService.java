package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.MPARepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MPAService {
    private final MPARepository mpaRepository;

    public List<MPA> getAllMPAs() {
        return mpaRepository.findAllRatings();
    }

    public MPA getMPAById(int mpaId) {
        return mpaRepository.findRatingById(mpaId);
    }
}
