package controller;

import com.common.Application;
import com.common.Config.AppConfig;
import com.common.Config.WebConfig;
import com.common.controller.FileUploadController;
import com.common.persistence.FileInfoRepository;
import com.common.service.impl.FileInfoService;
import com.common.service.impl.StorageService;
import org.json.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;

import com.common.exception.FileAlreadyExistsException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Created by Kirill Stoianov on 22/09/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class, FileInfoRepository.class})
@ContextConfiguration(classes = {AppConfig.class, WebConfig.class})
@WebAppConfiguration
public class FileUploadControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private StorageService storageService;

    @Autowired
    private FileInfoService infoService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testSingle() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/image-store/delete/all"))
                .andExpect(status().isOk());

        //prepare test image file
        final URL firFileURL = FileUploadControllerTest.class.getClassLoader().getResource("Ff6a9mL.jpg");
        java.nio.file.Path resPath = java.nio.file.Paths.get(firFileURL.toURI());
        final byte[] firstImageBytes = Files.readAllBytes(resPath);

        MockMultipartFile firstFile = new MockMultipartFile("file", "imgName.jpg", "image/jpg", firstImageBytes);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image-store/upload/single")
                .file(firstFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]", notNullValue()));
    }

    @Test
    public void testSingleWithException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/image-store/delete/all"))
                .andExpect(status().isOk());


        //prepare test image file
        final URL firFileURL = FileUploadControllerTest.class.getClassLoader().getResource("Ff6a9mL.jpg");
        java.nio.file.Path resPath = java.nio.file.Paths.get(firFileURL.toURI());
        final byte[] firstImageBytes = Files.readAllBytes(resPath);

        MockMultipartFile firstFile = new MockMultipartFile("file", "imgName.jpg", "image/jpg", firstImageBytes);


        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image-store/upload/single")
                .file(firstFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]", notNullValue()));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image-store/upload/single")
                .file(firstFile))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testMultiple() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/image-store/delete/all"))
                .andExpect(status().isOk());

        //prepare test image file
        final URL firFileURL = FileUploadControllerTest.class.getClassLoader().getResource("Ff6a9mL.jpg");
        java.nio.file.Path resPath = java.nio.file.Paths.get(firFileURL.toURI());
        final byte[] firstImageBytes = Files.readAllBytes(resPath);

        final URL secondFileURL = FileUploadControllerTest.class.getClassLoader().getResource("h_1401934118_3057436_6e965a2f1e.jpeg");
        resPath = Paths.get(secondFileURL.toURI());
        final byte[] secondFIleBytes = Files.readAllBytes(resPath);

        MockMultipartFile firstFile = new MockMultipartFile("file", "imgName.jpg", "image/jpg", firstImageBytes);
        MockMultipartFile secondFile = new MockMultipartFile("file", "h_1401934118_3057436_6e965a2f1e.jpeg", "image/jpeg", secondFIleBytes);

        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image-store/upload/multiple")
                .file(firstFile).file(secondFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]", notNullValue()))
                .andExpect(jsonPath("$.[1]", notNullValue()));
    }


    @Test
    public void testFindByIdAndSize() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/image-store/delete/all"))
                .andExpect(status().isOk());

        //prepare test image file
        final URL firFileURL = FileUploadControllerTest.class.getClassLoader().getResource("Ff6a9mL.jpg");
        java.nio.file.Path resPath = java.nio.file.Paths.get(firFileURL.toURI());
        final byte[] firstImageBytes = Files.readAllBytes(resPath);

        MockMultipartFile firstFile = new MockMultipartFile("file", "imgName.jpg", "image/jpg", firstImageBytes);

        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image-store/upload/single")
                .file(firstFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]", notNullValue()));

        JSONArray ids = new JSONArray(resultActions.andReturn().getResponse().getContentAsString());
        mockMvc.perform(MockMvcRequestBuilders.get("/image-store/"+ids.getString(0)+"/small"))
                .andExpect(status().isOk());
    }


    @Test
    public void testDeleteById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/image-store/delete/all"))
                .andExpect(status().isOk());

        //prepare test image file
        final URL firFileURL = FileUploadControllerTest.class.getClassLoader().getResource("Ff6a9mL.jpg");
        java.nio.file.Path resPath = java.nio.file.Paths.get(firFileURL.toURI());
        final byte[] firstImageBytes = Files.readAllBytes(resPath);

        MockMultipartFile firstFile = new MockMultipartFile("file", "imgName.jpg", "image/jpg", firstImageBytes);

        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image-store/upload/single")
                .file(firstFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]", notNullValue()));

        JSONArray ids = new JSONArray(resultActions.andReturn().getResponse().getContentAsString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/image-store/delete/"+ids.getString(0)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/image-store/"+ids.getString(0)+"/small"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testDeleteAll() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/image-store/delete/all"))
                .andExpect(status().isOk());

        //prepare test image file
        final URL firFileURL = FileUploadControllerTest.class.getClassLoader().getResource("Ff6a9mL.jpg");
        java.nio.file.Path resPath = java.nio.file.Paths.get(firFileURL.toURI());
        final byte[] firstImageBytes = Files.readAllBytes(resPath);

        MockMultipartFile firstFile = new MockMultipartFile("file", "imgName.jpg", "image/jpg", firstImageBytes);

        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image-store/upload/single")
                .file(firstFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]", notNullValue()));

        JSONArray ids = new JSONArray(resultActions.andReturn().getResponse().getContentAsString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/image-store/delete/all"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/image-store/"+ids.getString(0)+"/small"))
                .andExpect(status().isBadRequest());
    }

}
