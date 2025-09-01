package ru.gallery.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

public class EntityManagers {

    @Nonnull
    public static EntityManager em(@Nonnull String jdbcUrl) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(
                StringUtils.substringAfter(jdbcUrl, "37038/")
        );
        
        return emf.createEntityManager();
    }
}
