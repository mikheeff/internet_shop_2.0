package com.internetshop.service.api;

import com.internetshop.Exceptions.NoSuchCategoryException;
import com.internetshop.Exceptions.NoSuchRulesException;
import com.internetshop.entities.GoodsEntity;
import com.internetshop.model.Category;
import com.internetshop.model.Goods;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public interface GoodsService {
    List<Goods> getAllGoods();
    List<Goods> getAllGoods(int firstId,int maxResults);
    List<Goods> getAllGoodsByCategoryName(int firstId, int maxResults, String categoryName);
    void addGoods(Goods goods) throws NoSuchCategoryException, NoSuchRulesException;
    void deleteGoodsById(int id);
    Goods getGoodsById(int id);
    void updateGoods(Goods goods);
    GoodsEntity convertGoodsToDAO(Goods goods) throws NoSuchCategoryException, NoSuchRulesException;
    Goods convertGoodsToDTO(GoodsEntity goodsEntity);
    int getRandomGoodsId();
    long getAmountOfGoods();
    long getAmountOfGoodsByCategoryName(String categoryName);
    List<Category> getAllCategories();
    void addCategory(Category category);
}
