package com.huzeji.meal_planner.repository;

import com.huzeji.meal_planner.repository.specs.GenericSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Map;

@NoRepositoryBean
public interface GenericRepository<T, I>
    extends JpaRepository<T, I>
            , JpaSpecificationExecutor<T>
            , GenericSpecs<T>
{
    default List<T> getAll(Map<String, Object> filters ) {
        return findAll( filter( filters ) );
    }

    default Page<T> getPage( Map<String, Object> filters, Pageable page ){
        return findAll( filter( filters ), page );
    }

}
