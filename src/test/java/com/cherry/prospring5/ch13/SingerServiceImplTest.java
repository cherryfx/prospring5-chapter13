package com.cherry.prospring5.ch13;

import com.cherry.prospring5.ch13.config.DataConfig;
import com.cherry.prospring5.ch13.config.ServiceConfig;
import com.cherry.prospring5.ch13.entities.Singer;
import com.cherry.prospring5.ch13.services.SingerService;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceTestConfig.class, ServiceConfig.class,
        DataConfig.class})
@Transactional
@TestExecutionListeners({ServiceTestExecutionListener.class})
@ActiveProfiles("test")
public class SingerServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    SingerService singerService;
    @PersistenceContext
    private EntityManager em;

    @DataSets(setUpDataSet = "/com/cherry/prospring5/ch13/SingerServiceImplTest.xls")
    @Test
    public void testFindAll() throws Exception {
        List<Singer> result = singerService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @DataSets(setUpDataSet = "/com/cherry/prospring5/ch13/SingerServiceImplTest.xls")
    @Test
    public void testFindByFirstNameAndLastName_1() throws Exception {
        Singer result = singerService.findByFirstNameAndLastName("John", "Mayer");
        assertNotNull(result);
    }

    @DataSets(setUpDataSet = "/com/cherry/prospring5/ch13/SingerServiceImplTest.xls")
    @Test
    public void testFindByFirstNameAndLastName_2() throws Exception {
        Singer result = singerService.findByFirstNameAndLastName("BB", "King");
        assertNull(result);
    }

    @Test
    public void testAddSinger() throws Exception {
        deleteFromTables("SINGER");
        Singer singer = new Singer();
        singer.setFirstName("Stevie");
        singer.setLastName("Vaughan ");
        singerService.save(singer);
        em.flush();
        List<Singer> singers = singerService.findAll();
        assertEquals(1, singers.size());
    }

    /**
     * not accomplish need to fixed
     * @throws Exception
     */
    @Test(expected = ConstraintViolationException.class)
    public void testAddSingerWithJSR349Error() throws Exception {
        deleteFromTables("SINGER");
        Singer singer = new Singer();
        singerService.save(singer);
        em.flush();
        List<Singer> singers = singerService.findAll();
        assertEquals(0, singers.size());
    }
}
