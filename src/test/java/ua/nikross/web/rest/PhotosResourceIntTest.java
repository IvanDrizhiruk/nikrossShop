package ua.nikross.web.rest;

import ua.nikross.NikrossShopApp;
import ua.nikross.domain.Photos;
import ua.nikross.repository.PhotosRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PhotosResource REST controller.
 *
 * @see PhotosResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NikrossShopApp.class)
@WebAppConfiguration
@IntegrationTest
public class PhotosResourceIntTest {


    private static final byte[] DEFAULT_THUMBNAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";
    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";

    @Inject
    private PhotosRepository photosRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPhotosMockMvc;

    private Photos photos;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhotosResource photosResource = new PhotosResource();
        ReflectionTestUtils.setField(photosResource, "photosRepository", photosRepository);
        this.restPhotosMockMvc = MockMvcBuilders.standaloneSetup(photosResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        photos = new Photos();
        photos.setThumbnail(DEFAULT_THUMBNAIL);
        photos.setThumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        photos.setImage(DEFAULT_IMAGE);
        photos.setImageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        photos.setType(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createPhotos() throws Exception {
        int databaseSizeBeforeCreate = photosRepository.findAll().size();

        // Create the Photos

        restPhotosMockMvc.perform(post("/api/photos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photos)))
                .andExpect(status().isCreated());

        // Validate the Photos in the database
        List<Photos> photos = photosRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeCreate + 1);
        Photos testPhotos = photos.get(photos.size() - 1);
        assertThat(testPhotos.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testPhotos.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testPhotos.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPhotos.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testPhotos.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void getAllPhotos() throws Exception {
        // Initialize the database
        photosRepository.saveAndFlush(photos);

        // Get all the photos
        restPhotosMockMvc.perform(get("/api/photos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(photos.getId().intValue())))
                .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL))))
                .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getPhotos() throws Exception {
        // Initialize the database
        photosRepository.saveAndFlush(photos);

        // Get the photos
        restPhotosMockMvc.perform(get("/api/photos/{id}", photos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(photos.getId().intValue()))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnail").value(Base64Utils.encodeToString(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPhotos() throws Exception {
        // Get the photos
        restPhotosMockMvc.perform(get("/api/photos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhotos() throws Exception {
        // Initialize the database
        photosRepository.saveAndFlush(photos);
        int databaseSizeBeforeUpdate = photosRepository.findAll().size();

        // Update the photos
        Photos updatedPhotos = new Photos();
        updatedPhotos.setId(photos.getId());
        updatedPhotos.setThumbnail(UPDATED_THUMBNAIL);
        updatedPhotos.setThumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE);
        updatedPhotos.setImage(UPDATED_IMAGE);
        updatedPhotos.setImageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        updatedPhotos.setType(UPDATED_TYPE);

        restPhotosMockMvc.perform(put("/api/photos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPhotos)))
                .andExpect(status().isOk());

        // Validate the Photos in the database
        List<Photos> photos = photosRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeUpdate);
        Photos testPhotos = photos.get(photos.size() - 1);
        assertThat(testPhotos.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testPhotos.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testPhotos.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPhotos.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testPhotos.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deletePhotos() throws Exception {
        // Initialize the database
        photosRepository.saveAndFlush(photos);
        int databaseSizeBeforeDelete = photosRepository.findAll().size();

        // Get the photos
        restPhotosMockMvc.perform(delete("/api/photos/{id}", photos.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Photos> photos = photosRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
