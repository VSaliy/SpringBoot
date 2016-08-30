package com.olegshan.service;

import com.olegshan.AbstractTest;
import com.olegshan.model.Greeting;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.Collection;

/**
 * Created by olegshan on 30.08.2016.
 */
@Transactional
public class GreetingServiceTest extends AbstractTest {

    @Autowired
    GreetingService service;

    @Before
    public void setUp() {
        service.evictCache();
    }

    @After
    public void tearDown() {
        //clean up after each test method
    }

    @Test
    public void testFindAll() {
        Collection<Greeting> list = service.findAll();

        Assert.assertNotNull("failure - expected not null", list);
        Assert.assertEquals("failure - expected size", 2, list.size());
    }

    @Test
    public void testFindOne() {
        Long id = new Long(1);
        Greeting entity = service.findOne(id);
        Assert.assertNotNull("failure - expected not null", entity);
        Assert.assertEquals("failure - expected id attribute match", id, entity.getId());
    }

    @Test
    public void testFindOneNotFound() {
        Long id = Long.MAX_VALUE;
        Greeting entity = service.findOne(id);
        Assert.assertNull("failure - expected null", entity);
    }

    @Test
    public void testCreate() {
        Greeting entity = new Greeting();
        entity.setText("test");

        Greeting createdEntity = service.create(entity);
        Assert.assertNotNull("failure - expected not null", createdEntity);
        Assert.assertNotNull("failure - expected id attribute not null", createdEntity.getId());
        Assert.assertEquals("failure - expected text attribute match", "test", createdEntity.getText());

        Collection<Greeting> list = service.findAll();
        Assert.assertEquals("failure - expected size", 3, list.size());
    }

    @Test
    public void testCreateWithId() {

        Exception exception = null;

        Greeting entity = new Greeting();
        entity.setId(Long.MAX_VALUE);
        entity.setText("test");

        try {
            service.create(entity);
        } catch (EntityExistsException e) {
            exception = e;
        }

        Assert.assertNotNull("failure - expected exception", exception);
        Assert.assertTrue("failure - expected EntityExistsException",
                exception instanceof EntityExistsException);

    }

    @Test
    public void testUpdate() {
        Long id = new Long(1);

        Greeting entity = service.findOne(id);
        Assert.assertNotNull("failure - expected not null", entity);

        String updatedText = entity.getText() + "test";
        entity.setText(updatedText);

        Greeting updatedEntity = service.update(entity);

        Assert.assertNotNull("failure - expected updated entity not null", updatedEntity);
        Assert.assertEquals("failure - expected updated entity id attribute unchanged", id, updatedEntity.getId());
        Assert.assertEquals("failure - expected updated entity text attribute match",
                updatedText, updatedEntity.getText());
    }

    @Test
    public void testUpdateNotFound() {
        Exception e = null;

        Greeting entity = new Greeting();
        entity.setId(Long.MAX_VALUE);
        entity.setText("test");

        try {
            service.update(entity);
        } catch (NoResultException nre) {
            e = nre;
        }
        Assert.assertNotNull("failure - expected exception", e);
        Assert.assertTrue("failure - expected NoResultException", e instanceof NoResultException);
    }

    @Test
    public void testDelete() {
        Long id = new Long(1);

        Greeting entity = service.findOne(id);
        Assert.assertNotNull("failure - expected not null", entity);

        service.delete(id);
        Collection<Greeting> list = service.findAll();
        Assert.assertEquals("failure - expected size", 1, list.size());

        Greeting deleteEntity = service.findOne(id);

        Assert.assertNull("failure - expected entity to be deleted", deleteEntity);
    }


}
