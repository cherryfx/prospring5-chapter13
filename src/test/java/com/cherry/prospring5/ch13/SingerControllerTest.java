package com.cherry.prospring5.ch13;

import com.cherry.prospring5.ch13.controller.SingerController;
import com.cherry.prospring5.ch13.entities.Singer;
import com.cherry.prospring5.ch13.services.SingerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SingerControllerTest {
    private final List<Singer> singers = new ArrayList<>();
    @Before
    public void initSingers() {
        Singer singer = new Singer();
        singer.setId(1l);
        singer.setFirstName("John");
        singer.setLastName("Mayer");
        singers.add(singer);
    }
    @Test
    public void testList() throws Exception {
        SingerService singerService = mock(SingerService.class);
        when(singerService.findAll()).thenReturn(singers);
        SingerController singerController = new SingerController();
        ReflectionTestUtils.setField(singerController,
                "singerService", singerService);
        ExtendedModelMap uiModel = new ExtendedModelMap();
        uiModel.addAttribute("singers", singerController.listData());
        Singers modelSingers = new Singers((List<Singer>) uiModel.get("singers"));
        assertEquals(1, modelSingers.getSingers().size());
    }

    @Test
    public void testCreate() {
        final Singer newSinger = new Singer();
        newSinger.setId(999l);
        newSinger.setFirstName("BB");
        newSinger.setLastName("King");
        SingerService singerService = mock(SingerService.class);
        when(singerService.save(newSinger)).thenAnswer(new Answer<Singer>() {
            public Singer answer(InvocationOnMock invocation) throws Throwable {
                singers.add(newSinger);
                return newSinger;
            }
        });
        SingerController singerController = new SingerController();
        ReflectionTestUtils.setField(singerController, "singerService",
                singerService);
        Singer singer = singerController.create(newSinger);
        assertEquals(Long.valueOf(999l), singer.getId());
        assertEquals("BB", singer.getFirstName());
        assertEquals("King", singer.getLastName());
        assertEquals(2, singers.size());
    }

//    @DataSets(setUpDataSet= "/com/cherry/prospring5/ch13/SingerServiceImplTest.xls")
//    @Test
//    public void testFindAll() throws Exception {
//        List<Singer> result = singerService.findAll();0
//    }
}
