package ua.nikross.web.rest;

import ua.nikross.NikrossShopApp;
import ua.nikross.domain.Goods;
import ua.nikross.repository.GoodsRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the GoodsResource REST controller.
 *
 * @see GoodsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NikrossShopApp.class)
@WebAppConfiguration
@IntegrationTest
public class GoodsResourceIntTest {

    private static final String DEFAULT_ARTICLE = "AAAAA";
    private static final String UPDATED_ARTICLE = "BBBBB";
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private GoodsRepository goodsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGoodsMockMvc;

    private Goods goods;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GoodsResource goodsResource = new GoodsResource();
        ReflectionTestUtils.setField(goodsResource, "goodsRepository", goodsRepository);
        this.restGoodsMockMvc = MockMvcBuilders.standaloneSetup(goodsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        goods = new Goods();
        goods.setArticle(DEFAULT_ARTICLE);
        goods.setTitle(DEFAULT_TITLE);
        goods.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createGoods() throws Exception {
        int databaseSizeBeforeCreate = goodsRepository.findAll().size();

        // Create the Goods

        restGoodsMockMvc.perform(post("/api/goods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(goods)))
                .andExpect(status().isCreated());

        // Validate the Goods in the database
        List<Goods> goods = goodsRepository.findAll();
        assertThat(goods).hasSize(databaseSizeBeforeCreate + 1);
        Goods testGoods = goods.get(goods.size() - 1);
        assertThat(testGoods.getArticle()).isEqualTo(DEFAULT_ARTICLE);
        assertThat(testGoods.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testGoods.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllGoods() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

        // Get all the goods
        restGoodsMockMvc.perform(get("/api/goods?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(goods.getId().intValue())))
                .andExpect(jsonPath("$.[*].article").value(hasItem(DEFAULT_ARTICLE.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getGoods() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);

        // Get the goods
        restGoodsMockMvc.perform(get("/api/goods/{id}", goods.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(goods.getId().intValue()))
            .andExpect(jsonPath("$.article").value(DEFAULT_ARTICLE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGoods() throws Exception {
        // Get the goods
        restGoodsMockMvc.perform(get("/api/goods/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoods() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);
        int databaseSizeBeforeUpdate = goodsRepository.findAll().size();

        // Update the goods
        Goods updatedGoods = new Goods();
        updatedGoods.setId(goods.getId());
        updatedGoods.setArticle(UPDATED_ARTICLE);
        updatedGoods.setTitle(UPDATED_TITLE);
        updatedGoods.setDescription(UPDATED_DESCRIPTION);

        restGoodsMockMvc.perform(put("/api/goods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGoods)))
                .andExpect(status().isOk());

        // Validate the Goods in the database
        List<Goods> goods = goodsRepository.findAll();
        assertThat(goods).hasSize(databaseSizeBeforeUpdate);
        Goods testGoods = goods.get(goods.size() - 1);
        assertThat(testGoods.getArticle()).isEqualTo(UPDATED_ARTICLE);
        assertThat(testGoods.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testGoods.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteGoods() throws Exception {
        // Initialize the database
        goodsRepository.saveAndFlush(goods);
        int databaseSizeBeforeDelete = goodsRepository.findAll().size();

        // Get the goods
        restGoodsMockMvc.perform(delete("/api/goods/{id}", goods.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Goods> goods = goodsRepository.findAll();
        assertThat(goods).hasSize(databaseSizeBeforeDelete - 1);
    }
}
