package com.dugq.cache.jpa;

import com.dugq.cache.jpa.model.ImUser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<ImUser,Long> {

    @Cacheable(value = "userById",cacheManager = "myRedisCacheManager")
    ImUser findById(Long id);

    @Cacheable(value = "usersByAccount",key = "#p1",cacheManager = "myRedisCacheManager")
    ImUser findByAccountAndPassword(String account, String password);


    @Cacheable(value = "usersByNickName",keyGenerator = "userNickNameKeyGenerator",cacheManager = "myRedisCacheManager")
    ImUser findByNickNameLike(String nickName);
}
