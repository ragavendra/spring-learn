package com.stopsnearme.app.ws.ui.model;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface PersonRepository extends PagingAndSortingRepository<Person, Long>, CrudRepository<Person,Long>{

	// List<Person> findByLastName(@Param("name") String name);
	Iterable<Person> findByLastName(@Param("name") String name);

	// Error with Query, may be works only with record model? or without JPA
	// @Query("select * from people cc where cc.userName = :#{authentication.name}")
	// @Query("select * from people cc where cc.userName = :#{authentication.name}")
	Iterable<Person> findAll(Sort sor);
}
