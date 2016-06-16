package ua.nikross.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.nikross.domain.Categories;
import ua.nikross.repository.CategoriesRepository;
import ua.nikross.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Categories.
 */
@RestController
@RequestMapping("/api")
public class CategoriesResource {

    private final Logger log = LoggerFactory.getLogger(CategoriesResource.class);
        
    @Inject
    private CategoriesRepository categoriesRepository;
    
    /**
     * POST  /categories : Create a new categories.
     *
     * @param categories the categories to create
     * @return the ResponseEntity with status 201 (Created) and with body the new categories, or with status 400 (Bad Request) if the categories has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/categories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categories> createCategories(@RequestBody Categories categories) throws URISyntaxException {
        log.debug("REST request to save Categories : {}", categories);
        if (categories.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("categories", "idexists", "A new categories cannot already have an ID")).body(null);
        }
        Categories result = categoriesRepository.save(categories);
        return ResponseEntity.created(new URI("/api/categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("categories", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /categories : Updates an existing categories.
     *
     * @param categories the categories to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated categories,
     * or with status 400 (Bad Request) if the categories is not valid,
     * or with status 500 (Internal Server Error) if the categories couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/categories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categories> updateCategories(@RequestBody Categories categories) throws URISyntaxException {
        log.debug("REST request to update Categories : {}", categories);
        if (categories.getId() == null) {
            return createCategories(categories);
        }
        Categories result = categoriesRepository.save(categories);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("categories", categories.getId().toString()))
            .body(result);
    }

    /**
     * GET  /categories : get all the categories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of categories in body
     */
    @RequestMapping(value = "/categories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Categories> getAllCategories() {
        log.debug("REST request to get all Categories");
        List<Categories> categories = categoriesRepository.findAll();
        return categories;
    }

    /**
     * GET  /categories/:id : get the "id" categories.
     *
     * @param id the id of the categories to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the categories, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/categories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categories> getCategories(@PathVariable Long id) {
        log.debug("REST request to get Categories : {}", id);
        Categories categories = categoriesRepository.findOne(id);
        return Optional.ofNullable(categories)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /categories/:id : delete the "id" categories.
     *
     * @param id the id of the categories to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/categories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCategories(@PathVariable Long id) {
        log.debug("REST request to delete Categories : {}", id);
        categoriesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("categories", id.toString())).build();
    }

}
