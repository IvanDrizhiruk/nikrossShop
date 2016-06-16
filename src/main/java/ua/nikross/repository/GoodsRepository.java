package ua.nikross.repository;

import ua.nikross.domain.Goods;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Goods entity.
 */
@SuppressWarnings("unused")
public interface GoodsRepository extends JpaRepository<Goods,Long> {

    @Query("select distinct goods from Goods goods left join fetch goods.categories")
    List<Goods> findAllWithEagerRelationships();

    @Query("select goods from Goods goods left join fetch goods.categories where goods.id =:id")
    Goods findOneWithEagerRelationships(@Param("id") Long id);

}
