package com.ilopezluna;

import com.ilopezluna.config.Constants;
import com.ilopezluna.domain.Item;
import com.ilopezluna.service.ItemService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import com.google.common.base.Joiner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ComponentScan
@EnableAutoConfiguration(exclude = {MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class})
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Inject
    private Environment env;

    @Inject
    private ItemService itemService;

    /**
     * Initializes jhipster.
     * <p/>
     * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
     * <p/>
     */
    @PostConstruct
    public void initApplication() throws IOException {
        if (env.getActiveProfiles().length == 0) {
            log.warn("No Spring profile configured, running with default configuration");
        }
        else if (env.getActiveProfiles().length == 1 && env.getActiveProfiles()[0].equals(Constants.SPRING_PROFILE_DEVELOPMENT)) {

            log.info("Adding initial items");
            for (int i=0; i < 5; i++) {
                List<Item> items = new ArrayList<>();
                for (int u=0; u < 2000; u++) {
                    Item item = buildItem( (i + 1) * u );
                    items.add(item);
                }

                itemService.save(items);
            }
            log.info("Done");
        }
        else {
            log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
        }
    }

    private Item    buildItem(int i) {
        Item item = new Item();
        item.setCreatedAt(new DateTime());
        item.setDescription("Description: " + i);
        item.setLatitude("41." + i);
        item.setLongitude("2." + i);
        item.setName("Name: " + i);
        item.setPrice(new BigDecimal(i));
        item.setStatus(1);
        return item;
    }

    /**
     * Main method, used to run the application.
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(Application.class);
        app.setShowBanner(false);

        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);

        // Check if the selected profile has been set as argument.
        // if not the development profile will be added
        addDefaultProfile(app, source);
        addLiquibaseScanPackages();
        Environment env = app.run(args).getEnvironment();
        log.info("Access URLs:\n----------------------------------------------------------\n\t" +
            "Local: \t\thttp://127.0.0.1:{}\n\t" +
            "External: \thttp://{}:{}\n----------------------------------------------------------",
            env.getProperty("server.port"),
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"));

    }

    /**
     * Set a default profile if it has not been set
     */
    private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
        if (!source.containsProperty("spring.profiles.active")) {
            app.setAdditionalProfiles(Constants.SPRING_PROFILE_DEVELOPMENT);
        }
    }

    /**
     * Set the liquibases.scan.packages to avoid an exception from ServiceLocator.
     */
    private static void addLiquibaseScanPackages() {
        System.setProperty("liquibase.scan.packages", Joiner.on(",").join(
            "liquibase.change", "liquibase.database", "liquibase.parser",
            "liquibase.precondition", "liquibase.datatype",
            "liquibase.serializer", "liquibase.sqlgenerator", "liquibase.executor",
            "liquibase.snapshot", "liquibase.logging", "liquibase.diff",
            "liquibase.structure", "liquibase.structurecompare", "liquibase.lockservice",
            "liquibase.ext", "liquibase.changelog"));
    }
}
