package ch.rotscher.sqlfilerunner;

import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.dbsupport.DbSupport;
import org.flywaydb.core.dbsupport.DbSupportFactory;
import org.flywaydb.core.dbsupport.SqlScript;
import org.flywaydb.core.util.*;
import org.flywaydb.core.util.jdbc.DriverDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by rotscher on 16.03.14.
 */
public class SqlfileRunner {

    private final File baseDirectory;
    private final DbSupport support;

    public SqlfileRunner(File baseDirectory, String url, String user, String password, String... initSql) throws IOException, SQLException {
        this.baseDirectory = baseDirectory;

        if (this.baseDirectory == null || !this.baseDirectory.exists() || !this.baseDirectory.isDirectory()) {
            throw new IOException(String.format("directory %s should exists and be a directory", this.baseDirectory));
        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        DataSource ds = new DriverDataSource(classLoader, null, url, user, password, initSql);
        support = DbSupportFactory.createDbSupport(ds.getConnection(), true);

    }

    public int execute() {
        int filesExecuted = 0;
        List<File> sqlFiles = new ArrayList<File>();
        collectSqlFiles(baseDirectory, sqlFiles);


        for (File sqlFile : sqlFiles) {
            Resource resource = new FileSystemResource(sqlFile.getAbsolutePath());
            SqlScript script = new SqlScript(resource.loadAsString("UTF-8"), support);
            System.out.format("execute sql file: %s%n", sqlFile.getAbsolutePath());
            script.execute(support.getJdbcTemplate());
            filesExecuted++;
        }

        return filesExecuted;
    }

    void collectSqlFiles(File directory, List<File> collectedFiles) {

        for (String fileName : directory.list()) {
            File file = new File(directory, fileName);
            if (file.isDirectory()) {
                collectSqlFiles(file, collectedFiles);
            } else {
                if (fileName.endsWith(".sql")) {
                    collectedFiles.add(file);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, SQLException {

        System.out.println(args);
        String environment = args[0];
        String path = ClassUtils.getLocationOnDisk(SqlfileRunner.class);
        path = path.substring(0, path.lastIndexOf("/")) + "/..";

        Properties props = new Properties();
        props.load(new FileInputStream(new File(path + "/conf/sqlfilerunner-" + environment + ".properties")));

        File baseDir = new File(path + "/" + props.getProperty("sqlfiles.dir"));
        SqlfileRunner runner = new SqlfileRunner(baseDir, props.getProperty("jdbc.url"),
                props.getProperty("jdbc.user"), props.getProperty("jdbc.password"), props.getProperty("jdbc.initSql"));
        int filesExecuted = runner.execute();
        System.out.format("executed %d files%n", filesExecuted);
    }
}
