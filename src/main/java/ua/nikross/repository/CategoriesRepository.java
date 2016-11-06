package ua.nikross.repository;

import ua.nikross.domain.Categories;

import org.springframework.data.jpa.repository.*;
import ua.nikross.domain.Goods;

import java.util.List;

/**
 * Spring Data JPA repository for the Categories entity.
 */
@SuppressWarnings("unused")
public interface CategoriesRepository extends JpaRepository<Categories,Long> {

    @Query("select distinct categories from Categories categories left join fetch categories.goods")
    List<Categories> findAllWithEagerRelationships();
}
