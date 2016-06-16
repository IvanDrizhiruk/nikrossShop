package ua.nikross.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.nikross.domain.Photos;
import ua.nikross.repository.PhotosRepository;
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
 * REST controller for managing Photos.
 */
@RestController
@RequestMapping("/api")
public class PhotosResource {

    private final Logger log = LoggerFactory.getLogger(PhotosResource.class);
        
    @Inject
    private PhotosRepository photosRepository;
    
    /**
     * POST  /photos : Create a new photos.
     *
     * @param photos the photos to create
     * @return the ResponseEntity with status 201 (Created) and with body the new photos, or with status 400 (Bad Request) if the photos has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/photos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Photos> createPhotos(@RequestBody Photos photos) throws URISyntaxException {
        log.debug("REST request to save Photos : {}", photos);
        if (photos.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("photos", "idexists", "A new photos cannot already have an ID")).body(null);
        }
        Photos result = photosRepository.save(photos);
        return ResponseEntity.created(new URI("/api/photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("photos", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /photos : Updates an existing photos.
     *
     * @param photos the photos to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated photos,
     * or with status 400 (Bad Request) if the photos is not valid,
     * or with status 500 (Internal Server Error) if the photos couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/photos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Photos> updatePhotos(@RequestBody Photos photos) throws URISyntaxException {
        log.debug("REST request to update Photos : {}", photos);
        if (photos.getId() == null) {
            return createPhotos(photos);
        }
        Photos result = photosRepository.save(photos);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("photos", photos.getId().toString()))
            .body(result);
    }

    /**
     * GET  /photos : get all the photos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of photos in body
     */
    @RequestMapping(value = "/photos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Photos> getAllPhotos() {
        log.debug("REST request to get all Photos");
        List<Photos> photos = photosRepository.findAll();
        return photos;
    }

    /**
     * GET  /photos/:id : get the "id" photos.
     *
     * @param id the id of the photos to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the photos, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/photos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Photos> getPhotos(@PathVariable Long id) {
        log.debug("REST request to get Photos : {}", id);
        Photos photos = photosRepository.findOne(id);
        return Optional.ofNullable(photos)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /photos/:id : delete the "id" photos.
     *
     * @param id the id of the photos to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/photos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePhotos(@PathVariable Long id) {
        log.debug("REST request to delete Photos : {}", id);
        photosRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("photos", id.toString())).build();
    }

}
