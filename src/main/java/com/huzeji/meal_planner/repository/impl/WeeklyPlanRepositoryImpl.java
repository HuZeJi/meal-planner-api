package com.huzeji.meal_planner.repository.impl;


import com.huzeji.meal_planner.model.UserEntity;
import com.huzeji.meal_planner.model.WeeklyPlanEntity;
import com.huzeji.meal_planner.repository.WeeklyPlanCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WeeklyPlanRepositoryImpl implements WeeklyPlanCustomRepository {
    @PersistenceContext private EntityManager entityManager;
    @Override
    public List<WeeklyPlanEntity> findBetweenDates(LocalDate date, UserEntity user ) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<WeeklyPlanEntity> cq = cb.createQuery( WeeklyPlanEntity.class );
        Root<WeeklyPlanEntity> root = cq.from( WeeklyPlanEntity.class );

        Predicate dateBetween = cb.between(
                cb.literal( date ),
                root.get( "startDate" ),
                root.get( "endDate" )
        );
        Predicate userMatch = cb.equal( root.get( "user" ), user );

        cq.where( cb.and( dateBetween, userMatch ) );
        return entityManager.createQuery( cq ).getResultList();
    }
}
