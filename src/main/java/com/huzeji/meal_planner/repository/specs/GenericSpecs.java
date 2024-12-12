package com.huzeji.meal_planner.repository.specs;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

public interface GenericSpecs<T> {
    default Specification<T> filter(Map<String, Object> filters) {
        return ( root, query, builder ) -> {
            List<Predicate> predicates = filters
                    .entrySet()
                    .stream()
                    .map( filter -> buildPredicate( builder, root, filter ) )
                    .toList();
            return builder.and( predicates.toArray( new Predicate[0] ) );
        };
    }

    private Predicate buildPredicate(CriteriaBuilder builder, Root<T> root, Map.Entry<String, Object> filter) {
        Path<Object> path = null;
        for (String key : filter.getKey().split("\\.")) {
            path = ( path == null ) ? root.get( key ) : path.get(key);
        }
        return builder.equal(path, filter.getValue());
    }
}
