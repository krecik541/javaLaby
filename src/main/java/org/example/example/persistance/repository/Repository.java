package org.example.example.persistance.repository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface Repository<E, K> {
    Optional<E> findById(K id);

    List<E> findAll();

    @Transactional
    K create(E e);

    @Transactional
    K delete(K k);

    @Transactional
    K update(K k, E e);
}
