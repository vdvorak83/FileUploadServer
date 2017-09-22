package persistense;

import com.common.Application;
import com.common.domain.FileInfo;
import com.common.persistence.FileInfoRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Kirill Stoianov on 22/09/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class FileInfoRepositoryTest {

    @Autowired
    private FileInfoRepository repository;

    private FileInfo fileInfo;

    @Before
    public void setUp() throws Exception {
        fileInfo = new FileInfo("FileName", "png", "dsfsdafsad", 2321.321);
    }

    @After
    public void tearDown() throws Exception {
        if (repository.findOne(fileInfo.getId())!=null)repository.delete(fileInfo.getId());
    }

    @Test
    public void save(){
        fileInfo = repository.save(fileInfo);
        assertNotNull(fileInfo);
        assertNotNull(fileInfo.getId());
        assertEquals("FileName", fileInfo.getFileName());
        assertEquals("png", fileInfo.getExtension());
    }

    @Test
    public void update(){
        fileInfo = repository.save(fileInfo);
        assertNotNull(fileInfo);
        assertNotNull(fileInfo.getId());
        assertEquals("FileName", fileInfo.getFileName());
        assertEquals("png", fileInfo.getExtension());

        String newName ="New file name";
        fileInfo.setFileName(newName);

        fileInfo = repository.save(fileInfo);
        assertNotNull(fileInfo);
        assertNotNull(fileInfo.getId());
        assertEquals(newName, fileInfo.getFileName());
    }

    @Test
    public void findById(){
        fileInfo = repository.save(fileInfo);
        assertNotNull(fileInfo);
        assertNotNull(fileInfo.getId());
        assertEquals("FileName", fileInfo.getFileName());
        assertEquals("png", fileInfo.getExtension());


        fileInfo = repository.findOne(fileInfo.getId());
        assertNotNull(fileInfo);
    }

    @Test
    public void findAll(){
        fileInfo = repository.save(fileInfo);
        assertNotNull(fileInfo);
        assertNotNull(fileInfo.getId());
        assertEquals("FileName", fileInfo.getFileName());
        assertEquals("png", fileInfo.getExtension());


        final List<FileInfo> all = repository.findAll();
        assertNotNull(all);
        assertEquals(1,all.size());
    }



    @Test
    public void deleteById(){
        fileInfo = repository.save(fileInfo);
        assertNotNull(fileInfo);
        assertNotNull(fileInfo.getId());
        assertEquals("FileName", fileInfo.getFileName());
        assertEquals("png", fileInfo.getExtension());


        repository.delete(fileInfo.getId());

        assertNull(repository.findOne(fileInfo.getId()));
    }

    @Test
    public void deleteAll(){
        fileInfo = repository.save(fileInfo);
        assertNotNull(fileInfo);
        assertNotNull(fileInfo.getId());
        assertEquals("FileName", fileInfo.getFileName());
        assertEquals("png", fileInfo.getExtension());


        repository.deleteAll();

        assertNotNull(repository.findAll());
        assertEquals(0,repository.findAll().size());
    }

}
