package com.example.silve.ago01.models.repository;


import com.example.silve.ago01.models.specification.Specification;

import java.util.List;

/**
 * Created by silve on 2017/02/12.
 */

public interface Repository<T> {

    void add(T item);

    void add(Iterable<T> items);

    void update(T item);

    void remove(T item);

    void remove(Specification specification);

    List<T> query(Specification specification);

}
