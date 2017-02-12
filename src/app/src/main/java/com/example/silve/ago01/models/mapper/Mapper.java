package com.example.silve.ago01.models.mapper;

/**
 * Created by silve on 2017/02/12.
 */

public interface Mapper<From,To> {
    To map(From from);
}
