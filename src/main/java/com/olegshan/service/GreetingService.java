package com.olegshan.service;

import com.olegshan.model.Greeting;

import java.util.Collection;

/**
 * Created by olegshan on 29.08.2016.
 */
public interface GreetingService {

    Collection<Greeting> findAll();

    Greeting findOne(Long id);

    Greeting create(Greeting greeting);

    Greeting update(Greeting greeting);

    void delete(Long id);

    void evictCache();
}
