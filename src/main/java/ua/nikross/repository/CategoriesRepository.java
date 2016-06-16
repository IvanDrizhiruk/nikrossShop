package ua.nikross.repository;

import ua.nikross.domain.Categories;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Categories entity.
 */
@SuppressWarnings("unused")
public interface CategoriesRepository extends JpaRepository<Categories,Long> {

}
