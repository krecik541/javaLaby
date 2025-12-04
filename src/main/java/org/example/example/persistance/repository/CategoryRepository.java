package org.example.example.persistance.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.example.persistance.domain.Category;
import org.example.example.persistance.domain.CategoryType;
import org.example.example.persistance.domain.Recipe;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.*;

@Dependent
public class CategoryRepository implements Repository<Category, UUID> {

    private EntityManager em;

    @PersistenceContext(unitName = "foodPU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return Optional.ofNullable(em.find(Category.class, id));
    }

    @Override
    public List<Category> findAll() {
//        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Category> cq = cb.createQuery(Category.class);
        Root<Category> root = cq.from(Category.class);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }

    @Override
    @Transactional
    public UUID create(Category category) {
        if(category == null)
            return  null;
        em.persist(category);

        return category.getId();
    }

    @Override
    @Transactional
    public UUID delete(UUID id) {
        Optional<Category> c = findById(id);
        if(c.isEmpty())
            return null;

        em.remove(c.get());
        return id;
    }

    @Override
    @Transactional
    public UUID update(UUID id, Category category) {
        Optional<Category> c = findById(id);
        if(c.isEmpty())
            return null;

        em.merge(category);
        return id;
    }
}
