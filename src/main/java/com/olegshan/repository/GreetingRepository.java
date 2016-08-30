package com.olegshan.repository;

import com.olegshan.model.Greeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by olegshan on 29.08.2016.
 */
@Repository
public interface GreetingRepository extends JpaRepository<Greeting, Long> {
}
