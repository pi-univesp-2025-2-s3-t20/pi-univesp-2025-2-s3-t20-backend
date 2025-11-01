package com.univesp.pi.s3t20.repository;

import com.univesp.pi.s3t20.model.Role;
import com.univesp.pi.s3t20.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
