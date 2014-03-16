package ch.rotscher.sqlfilerunner;

import junit.framework.Assert;
import org.flywaydb.core.util.ClassUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rotscher on 16.03.14.
 */
public class SqlfileRunnerTest {

    private static String path;

    @BeforeClass
    public static void beforeClass() {
        path = ClassUtils.getLocationOnDisk(SqlfileRunnerTest.class);
        path = path.substring(0, path.lastIndexOf("/"));
        System.out.println(path);
    }

    @Test
    public void testInstanceCreation() {
        try {
            new SqlfileRunner(new File(path + "/sqlfiles"), "jdbc:h2:mem:flyway_test;DB_CLOSE_DELAY=-1", "sa", "", "create table x (a varchar(50), b varchar(50), c int(11))");
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testInstanceCreationExecuteNoDirectory() {
        try {
            SqlfileRunner runner = new SqlfileRunner(new File(path + "/nonexistingdirectory"), "jdbc:h2:mem:flyway_test;DB_CLOSE_DELAY=-1", "sa", "", "create table x (a varchar(50), b varchar(50), c int(11))");
            Assert.fail("this test should throw an IOException as");
        } catch (IOException e) {

        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCollect() {
        try {
            File baseDir = new File(path + "/sqlfiles");
            List<File> sqlFiles = new ArrayList<File>();

            SqlfileRunner runner = new SqlfileRunner(baseDir, "jdbc:h2:mem:flyway_test1;DB_CLOSE_DELAY=-1", "sa", "", "create table x (a varchar(50), b varchar(50), c int(11))");
            runner.collectSqlFiles(baseDir, sqlFiles);

            Assert.assertTrue(sqlFiles.size() == 5);

        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testExecute() {
        try {
            File baseDir = new File(path + "/sqlfiles");
            SqlfileRunner runner = new SqlfileRunner(baseDir, "jdbc:h2:mem:flyway_test2;DB_CLOSE_DELAY=-1", "sa", "", "create table x (a varchar(50), b varchar(50), c int(11))");
            runner.execute();

        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testExecute1() {
        try {
            File baseDir = new File(path + "/sqlfiles2");
            SqlfileRunner runner = new SqlfileRunner(baseDir, "jdbc:h2:mem:flyway_test;DB_CLOSE_DELAY=-1", "sa", "");
            runner.execute();

        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

}
