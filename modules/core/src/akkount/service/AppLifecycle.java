package akkount.service;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.events.AppContextStoppedEvent;
import com.haulmont.cuba.core.sys.persistence.DbmsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.SQLException;

@Component("akk_AppLifecycle")
public class AppLifecycle {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    protected Persistence persistence;

    @EventListener(AppContextStoppedEvent.class)
    public void applicationStopped() {
        if ("hsql".equals(DbmsType.getType()) && Boolean.valueOf(AppContext.getProperty("akk.shutdownDatabaseOnExit"))) {
            log.info("Shutting down the HSQL database");
            QueryRunner queryRunner = new QueryRunner(persistence.getDataSource());
            try {
                queryRunner.update("shutdown");
            } catch (SQLException e) {
                log.warn("Unable to shutdown the database", e);
            }
        }
    }
}
