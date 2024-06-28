package com.taskmanager.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class JpaConfig {

    /**
     * Configures the entity manager factory bean for JPA.
     *
     * @param dataSource The DataSource to be used by the entity manager factory.
     * @return A configured LocalContainerEntityManagerFactoryBean.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        // Set the DataSource that the entity manager factory should use.
        em.setDataSource(dataSource);

        // Specify the package(s) to scan for JPA entities.
        em.setPackagesToScan("com.taskmanager.model");

        // Set the JPA vendor adapter. Here we are using Hibernate as the JPA implementation.
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Set the persistence unit name. This is useful if you have multiple persistence units.
        em.setPersistenceUnitName("default");

        return em;
    }
}
