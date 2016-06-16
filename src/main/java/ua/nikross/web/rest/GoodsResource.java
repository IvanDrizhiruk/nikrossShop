package ua.nikross.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.nikross.domain.Goods;
import ua.nikross.repository.GoodsRepository;
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
 * REST controller for managing Goods.
 */
@RestController
@RequestMapping("/api")
public class GoodsResource {

    private final Logger log = LoggerFactory.getLogger(GoodsResource.class);
        
    @Inject
    private GoodsRepository goodsRepository;
    
    /**
     * POST  /goods : Create a new goods.
     *
     * @param goods the goods to create
     * @return the ResponseEntity with status 201 (Created) and with body the new goods, or with status 400 (Bad Request) if the goods has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/goods",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Goods> createGoods(@RequestBody Goods goods) throws URISyntaxException {
        log.debug("REST request to save Goods : {}", goods);
        if (goods.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("goods", "idexists", "A new goods cannot already have an ID")).body(null);
        }
        Goods result = goodsRepository.save(goods);
        return ResponseEntity.created(new URI("/api/goods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("goods", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /goods : Updates an existing goods.
     *
     * @param goods the goods to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated goods,
     * or with status 400 (Bad Request) if the goods is not valid,
     * or with status 500 (Internal Server Error) if the goods couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/goods",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Goods> updateGoods(@RequestBody Goods goods) throws URISyntaxException {
        log.debug("REST request to update Goods : {}", goods);
        if (goods.getId() == null) {
            return createGoods(goods);
        }
        Goods result = goodsRepository.save(goods);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("goods", goods.getId().toString()))
            .body(result);
    }

    /**
     * GET  /goods : get all the goods.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of goods in body
     */
    @RequestMapping(value = "/goods",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Goods> getAllGoods() {
        log.debug("REST request to get all Goods");
        List<Goods> goods = goodsRepository.findAllWithEagerRelationships();
        return goods;
    }

    /**
     * GET  /goods/:id : get the "id" goods.
     *
     * @param id the id of the goods to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the goods, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/goods/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Goods> getGoods(@PathVariable Long id) {
        log.debug("REST request to get Goods : {}", id);
        Goods goods = goodsRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(goods)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /goods/:id : delete the "id" goods.
     *
     * @param id the id of the goods to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/goods/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGoods(@PathVariable Long id) {
        log.debug("REST request to delete Goods : {}", id);
        goodsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("goods", id.toString())).build();
    }

}
