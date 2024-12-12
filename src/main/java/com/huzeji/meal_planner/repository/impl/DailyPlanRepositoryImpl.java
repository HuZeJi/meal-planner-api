package com.huzeji.meal_planner.repository.impl;


import com.huzeji.meal_planner.model.DailyPlanEntity;
import com.huzeji.meal_planner.model.UserEntity;
import com.huzeji.meal_planner.model.WeeklyPlanEntity;
import com.huzeji.meal_planner.repository.DailyPlanCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailyPlanRepositoryImpl implements DailyPlanCustomRepository {
    @PersistenceContext private EntityManager entityManager;
    @Override
    public List<DailyPlanEntity> findOnDate(LocalDate date, UserEntity user ) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DailyPlanEntity> cq = cb.createQuery( DailyPlanEntity.class );
        Root<DailyPlanEntity> dailyPlan = cq.from( DailyPlanEntity.class );
        Join<DailyPlanEntity, WeeklyPlanEntity> weeklyPlanJoin = dailyPlan.join( "weeklyPlan" );

        Predicate userMatch = cb.equal( weeklyPlanJoin.get( "user" ), user );
        Predicate dateMatch = cb.equal( dailyPlan.get( "date" ), date );

        cq.where( cb.and( dateMatch, userMatch ) );
        return entityManager.createQuery( cq ).getResultList();
    }
}
