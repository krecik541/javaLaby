package org.example.example.persistance.repository;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.example.example.persistance.domain.Recipe;
import org.example.example.persistance.domain.User;

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
//        List<Recipe> l = em.createQuery("SELECT r FROM Recipe r", Recipe.class).getResultList();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Recipe> cq = cb.createQuery(Recipe.class);
        Root<Recipe> root = cq.from(Recipe.class);
        cq.select(root);
        return em.createQuery(cq).getResultList();
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
        System.out.println("deleting " + id);
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
//        em.createQuery("delete from Recipe r where r.category.id = :id")
//                .setParameter("id", id)
//                .executeUpdate();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<Recipe> cq = cb.createCriteriaDelete(Recipe.class);
        Root<Recipe> root = cq.from(Recipe.class);
        cq.where(cb.equal(root.get("category").get("id"), id));
        em.createQuery(cq).executeUpdate();
    }

    public List<Recipe> findByAuthor(UUID id) {
//        List<Recipe> l = em.createQuery("SELECT r FROM Recipe r where r.author.id = :id", Recipe.class)
//                .setParameter("id", id)
//                .getResultList();
//        return l;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Recipe> cq = cb.createQuery(Recipe.class);
        Root<Recipe> root = cq.from(Recipe.class);
        cq.select(root).where(cb.equal(root.get("author").get("id"), id));
        return em.createQuery(cq).getResultList();
    }

    public List<Recipe> findByCategory(UUID id) {
//        List<Recipe> l = em.createQuery("SELECT r FROM Recipe r where r.author.id = :id", Recipe.class)
//                .setParameter("id", id)
//                .getResultList();
//        return l;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Recipe> cq = cb.createQuery(Recipe.class);
        Root<Recipe> root = cq.from(Recipe.class);
        cq.select(root).where(cb.equal(root.get("category").get("id"), id));
        return em.createQuery(cq).getResultList();
    }

    public List<Recipe> findByAuthorAndCategory(UUID author, UUID category) {
//        List<Recipe> l = em.createQuery("SELECT r FROM Recipe r where r.author.id = :author and r.category.id = :category", Recipe.class)
//                .setParameter("author", author)
//                .setParameter("category", category)
//                .getResultList();
//        return l;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Recipe> cq = cb.createQuery(Recipe.class);
        Root<Recipe> root = cq.from(Recipe.class);
        Predicate p1 = cb.equal(root.get("category").get("id"), category);
        Predicate p2 = cb.equal(root.get("author").get("id"), author);
        cq.select(root).where(cb.and(p1, p2));
        return em.createQuery(cq).getResultList();
    }

    public List<Recipe> findByFilters(String title, String description, Integer preparationTime, String authorName, UUID categoryId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Recipe> cq = cb.createQuery(Recipe.class);
        Root<Recipe> root = cq.from(Recipe.class);

        List<Predicate> predicates = new ArrayList<>();

        // Filtrowanie po tytule - zawiera fragment (case-insensitive)
        if (title != null && !title.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        // Filtrowanie po opisie - zawiera fragment (case-insensitive)
        if (description != null && !description.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
        }

        // Filtrowanie po czasie przygotowania - dokładna wartość
        if (preparationTime != null) {
            predicates.add(cb.equal(root.get("preparationTime"), preparationTime));
        }

        // Filtrowanie po autorze (po nazwie)
        if (authorName != null && !authorName.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("author").get("name")), "%" + authorName.toLowerCase() + "%"));
        }

        // Filtrowanie po kategorii
        if (categoryId != null) {
            predicates.add(cb.equal(root.get("category").get("id"), categoryId));
        }

        // Łączenie wszystkich warunków operatorem AND
        if (!predicates.isEmpty()) {
            cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
        } else {
            cq.select(root);
        }

        return em.createQuery(cq).getResultList();
    }
}
