package com.internetshop.repository.impl;

import com.internetshop.entities.CategoryEntity;
import com.internetshop.entities.ClientEntity;
import com.internetshop.entities.GoodsEntity;
import com.internetshop.repository.api.GoodsRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;
import java.util.Queue;
import static java.lang.Math.toIntExact;

@Repository
public class GoodsRepositoryImpl implements GoodsRepository {

    @PersistenceUnit(unitName = "item-manager-pu")
    private EntityManagerFactory emf= Persistence.createEntityManagerFactory("item-manager-pu");



    public List<GoodsEntity> getAll() {
        EntityManager em = this.emf.createEntityManager();
        return em.createQuery("select goods from GoodsEntity goods", GoodsEntity.class).getResultList();
    }

    public List<GoodsEntity> getAll(int firstId, int maxResults) {
        EntityManager em = this.emf.createEntityManager();
        return em.createQuery("select goods from GoodsEntity goods", GoodsEntity.class).setFirstResult(firstId).setMaxResults(maxResults).getResultList();
    }


    @Override
    public List<GoodsEntity> getAllGoodsByCategoryName(int firstId, int maxResults, String categoryName) {
        EntityManager em = this.emf.createEntityManager();
        return em
                .createQuery("select goods from GoodsEntity goods where category.name = :category", GoodsEntity.class)
                .setParameter("category", categoryName).setFirstResult(firstId).setMaxResults(maxResults).getResultList();
    }


    @Override
    public void addGoods(GoodsEntity goodsEntity) {
        EntityManager em = this.emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(goodsEntity);
        em.getTransaction().commit();
    }

    @Override
    public void deleteGoodsById(int id) {
        EntityManager em = this.emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(em.find(GoodsEntity.class, id));
        em.getTransaction().commit();
    }

    @Override
    public GoodsEntity getGoodsById(int id) {
        EntityManager em = this.emf.createEntityManager();
        return em.find(GoodsEntity.class, id);
    }

    @Override
    public void updateGoods(GoodsEntity goodsEntity) {
        EntityManager em = this.emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(goodsEntity);
        em.getTransaction().commit();
    }

    @Override
    public long getAmountOfGoods() {
        EntityManager em = this.emf.createEntityManager();
        return em.createQuery("select count(*) FROM GoodsEntity goods",Long.class).getSingleResult();
    }

    @Override
    public long getAmountOfGoodsByCategoryName(String categoryName) {
        EntityManager em = this.emf.createEntityManager();
        return em.createQuery("select count(*) from GoodsEntity goods where category.name = :category",Long.class).setParameter("category", categoryName).getSingleResult();
    }

    @Override
    public GoodsEntity getRandomGoods() {
        EntityManager em = this.emf.createEntityManager();
        GoodsEntity goodsEntity =  em.createQuery("select goods from GoodsEntity goods order by rand()",GoodsEntity.class).setMaxResults(1).getSingleResult();
        return goodsEntity;
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        EntityManager em = this.emf.createEntityManager();
        return em.createQuery("select categories from CategoryEntity categories", CategoryEntity.class).getResultList();

    }
}
