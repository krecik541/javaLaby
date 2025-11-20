package org.example.example.persistance.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.example.persistance.domain.Category;
import org.example.example.persistance.domain.User;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@Dependent
public class UserRepository implements Repository<User, UUID> {

    private EntityManager em;

    @PersistenceContext(unitName = "foodPU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    @Transactional
    public UUID create(User user) {
        if(user == null)
            return  null;
        em.persist(user);

        return user.getId();
    }

    @Override
    @Transactional
    public UUID delete(UUID id) {
        Optional<User> u = findById(id);
        if(u.isEmpty())
            return null;

        em.remove(u.get());
        return id;
    }

    @Override
    @Transactional
    public UUID update(UUID id, User user) {
        Optional<User> u = findById(id);
        if(u.isEmpty())
            return null;

        em.merge(user);
        return id;
    }


    public Path getAvatar(UUID id) {
        User user = findById(id).orElseThrow(IllegalArgumentException::new);
        return user.getAvatar();
    }

    public UUID setAvatar(UUID id, Path path) throws IOException {
        User user = findById(id).orElseThrow(IllegalArgumentException::new);
        user.setAvatar(path);
        return id;
    }

    public UUID deleteAvatar(UUID id) {
        User user = findById(id).orElseThrow(IllegalArgumentException::new);
        user.setAvatar(null);
        return id;
    }

    public Optional<User> findByLogin(String s) {
        List<User> list = em.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                .setParameter("login", s)
                .getResultList();
        if (list.isEmpty()) return Optional.empty();
        return Optional.of(list.get(0));
    }
}
