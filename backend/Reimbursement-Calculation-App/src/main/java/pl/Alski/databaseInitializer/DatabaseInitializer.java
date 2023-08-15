package pl.Alski.databaseInitializer;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor
public class DatabaseInitializer {
    private final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    public void initialize() {
        logger.info("--------------------------");
        logger.info("DatabaseInitializer starting its job.");
        UserInitializer userInitializer = new UserInitializer();
        userInitializer.initialize();

        LimitsInitializer limitInitializer = new LimitsInitializer();
        limitInitializer.initialize();

        ClaimInitializer claimInitializer = new ClaimInitializer();
        claimInitializer.initialize();

        logger.info("DatabaseInitializer finished its job.");
        logger.info("--------------------------");
    }
}
