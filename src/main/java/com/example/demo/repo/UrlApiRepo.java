package com.example.demo.repo;

import com.example.demo.entity.UrlApi;
import com.example.demo.entity.UrlApiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UrlApiRepo extends JpaRepository<UrlApi, Integer> {
    @Query(value = "select url_api.path from url_api\n" +
            "join url_api_role uar on url_api.id = uar.url_api_id\n" +
            "join role r on r.id = uar.role_id\n" +
            "where r.role_name IN :roles" , nativeQuery = true)
    List<String> findNameUrlByRoles(Collection<String> roles);
}
