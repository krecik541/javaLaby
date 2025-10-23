package org.example.example.persistance.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<E, K> {
    Optional<E> findById(K id);

    List<E> findAll();

    K create(E e);

    K delete(K k);

    K update(K k, E e);
}
