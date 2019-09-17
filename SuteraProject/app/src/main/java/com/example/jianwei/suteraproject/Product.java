package com.example.jianwei.suteraproject;
/**
 * @author Jian Wei Hew eeyjwh@nottingham.ac.uk
 * @version 1
 * @since april 2018
 */
/**
 * Getter for ordered item
 */
public class Product {
    String m_name;
    double m_value;

    Product(String name, double value){
        m_name = name;
        m_value = value;
    }

    String getName(){
        return m_name;
    }

    double getValue(){
        return m_value;
    }
}
