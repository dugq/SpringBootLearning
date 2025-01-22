package com.dugq.cache.jpa;

import com.dugq.cache.jpa.model.ImSession;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.Repository;

public interface SessionRepository extends Repository<ImSession,Long> {

    @Cacheable(cacheNames = "session")
    ImSession findById(Long id);

}
