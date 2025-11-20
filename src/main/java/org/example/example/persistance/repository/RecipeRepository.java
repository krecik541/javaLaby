package org.example.example.persistance.repository;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.example.persistance.domain.Recipe;

import java.util.*;

@Dependent
public class RecipeRepository implements Repository<Recipe, UUID> {

    private EntityManager em;

    @PersistenceContext(unitName = "foodPU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Recipe> findById(UUID id) {
        Recipe r = em.find(Recipe.class, id);
        return Optional.ofNullable(r);
    }

    @Override
    public List<Recipe> findAll() {
        List<Recipe> l = em.createQuery("SELECT r FROM Recipe r", Recipe.class).getResultList();
        return l;
    }

    @Override
    @Transactional
    public UUID create(Recipe recipe) {
        if(recipe == null)
            return  null;
        em.persist(recipe);

        return recipe.getId();
    }

    @Override
    @Transactional
    public UUID delete(UUID id) {
        Optional<Recipe> r = findById(id);
        if(r.isEmpty())
            return null;

        em.remove(r.get());
        return id;
    }

    @Override
    @Transactional
    public UUID update(UUID id, Recipe recipe) {
        Optional<Recipe> r = findById(id);
        if(r.isEmpty())
            return null;

        em.merge(recipe);
        return id;
    }

    @Transactional
    public void deleteByCategory(UUID id) {
        em.createQuery("delete from Recipe r where r.category.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public List<Recipe> findByAuthor(UUID id) {
        List<Recipe> l = em.createQuery("SELECT r FROM Recipe r where r.author.id = :id", Recipe.class)
                .setParameter("id", id)
                .getResultList();
        return l;
    }

    public List<Recipe> findByAuthorAndCategory(UUID author, UUID category) {
        List<Recipe> l = em.createQuery("SELECT r FROM Recipe r where r.author.id = :author and r.category.id = :category", Recipe.class)
                .setParameter("author", author)
                .setParameter("category", category)
                .getResultList();
        return l;
    }
}
