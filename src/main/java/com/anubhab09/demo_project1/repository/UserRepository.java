package com.anubhab09.demo_project1.repository;

import com.anubhab09.demo_project1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}

//public class UserRepository {
//}
