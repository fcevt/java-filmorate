package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.dao.DirectorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorRepository directorRepository;

    public List<Director> getAllDirectors() {
        return directorRepository.findAll();
    }

    public Director getDirectorById(int id) {
        return directorRepository.findById(id);
    }

    public Director createDirector(Director director) {
        return directorRepository.create(director);
    }

    public Director updateDirector(Director director) {
        return directorRepository.update(director);
    }

    public void deleteDirector(int id) {
        if (!directorRepository.delete(id)) {
            throw new NullPointerException("Режиссер с id: " + id + " не найден");
        }
    }
}