package com.example.jianwei.suteraproject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
/**
 * @author Jian Wei Hew eeyjwh@nottingham.ac.uk
 * @version 1
 * @since april 2018
 */
/**
 *Calculate total cart items value,
 * Add new items into cart
 */
public class Cart {

    Map<Product, Integer> lart;
    double m_value = 0;

    Cart(){
        lart = new LinkedHashMap<>();
    }

    /**
     *Calculate total cart items value
     * Add new items into cart
     */
    void addToCart(Product product){
        if (lart.containsKey(product))
            lart.put(product,lart.get(product)+1);
        else
            lart.put(product,1);

        m_value += product.getValue();  //CALCULATE TOTAL CART VALUE

    }

    Set<Product> getProducts()
    {
        return lart.keySet();
    }

    /**
     *Clear the whole cart
     */
    void empty(){ // CLEAR CART STORAGE
        lart.clear();
        m_value=0;

    }
    /**
     *Getters
     */
    double getValue(){
        return m_value;
    }
    int getSize(){
        return lart.size();
    }



}
