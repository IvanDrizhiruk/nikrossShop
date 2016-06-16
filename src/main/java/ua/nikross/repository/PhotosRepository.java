package ua.nikross.repository;

import ua.nikross.domain.Photos;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Photos entity.
 */
@SuppressWarnings("unused")
public interface PhotosRepository extends JpaRepository<Photos,Long> {

}
