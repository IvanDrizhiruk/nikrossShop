package ua.nikross.web.rest;

import ua.nikross.NikrossShopApp;
import ua.nikross.domain.Categories;
import ua.nikross.repository.CategoriesRepository;

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
 * Test class for the CategoriesResource REST controller.
 *
 * @see CategoriesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NikrossShopApp.class)
@WebAppConfiguration
@IntegrationTest
public class CategoriesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private CategoriesRepository categoriesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCategoriesMockMvc;

    private Categories categories;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CategoriesResource categoriesResource = new CategoriesResource();
        ReflectionTestUtils.setField(categoriesResource, "categoriesRepository", categoriesRepository);
        this.restCategoriesMockMvc = MockMvcBuilders.standaloneSetup(categoriesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        categories = new Categories();
        categories.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCategories() throws Exception {
        int databaseSizeBeforeCreate = categoriesRepository.findAll().size();

        // Create the Categories

        restCategoriesMockMvc.perform(post("/api/categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categories)))
                .andExpect(status().isCreated());

        // Validate the Categories in the database
        List<Categories> categories = categoriesRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeCreate + 1);
        Categories testCategories = categories.get(categories.size() - 1);
        assertThat(testCategories.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categories
        restCategoriesMockMvc.perform(get("/api/categories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(categories.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get the categories
        restCategoriesMockMvc.perform(get("/api/categories/{id}", categories.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(categories.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCategories() throws Exception {
        // Get the categories
        restCategoriesMockMvc.perform(get("/api/categories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);
        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();

        // Update the categories
        Categories updatedCategories = new Categories();
        updatedCategories.setId(categories.getId());
        updatedCategories.setName(UPDATED_NAME);

        restCategoriesMockMvc.perform(put("/api/categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCategories)))
                .andExpect(status().isOk());

        // Validate the Categories in the database
        List<Categories> categories = categoriesRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeUpdate);
        Categories testCategories = categories.get(categories.size() - 1);
        assertThat(testCategories.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);
        int databaseSizeBeforeDelete = categoriesRepository.findAll().size();

        // Get the categories
        restCategoriesMockMvc.perform(delete("/api/categories/{id}", categories.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Categories> categories = categoriesRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeDelete - 1);
    }
}
