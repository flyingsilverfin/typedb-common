package grakn.common.test.server;

import org.apache.commons.io.FileUtils;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.StartedProcess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class GraknCoreRunner implements GraknRunner {

    private static final String[] ARGS = System.getProperty("sun.java.command").split(" ");
    private static final File DISTRIBUTION_FILE = ARGS.length > 1 ? new File(ARGS[1]) : null;

    private static final String TAR = ".tar.gz";
    private static final String ZIP = ".zip";

    private final File GRAKN_DISTRIBUTION_FILE;
    private final Path GRAKN_TARGET_DIRECTORY;
    private final String GRAKN_DISTRIBUTION_FORMAT;
    private final int port = ThreadLocalRandom.current().nextInt(40000, 60000);
    private final Path tmpDir;

    private ProcessExecutor executor;
    private StartedProcess graknProcess;

    public GraknCoreRunner() throws InterruptedException, TimeoutException, IOException {
        this(DISTRIBUTION_FILE);
    }

    public GraknCoreRunner(File distributionFile) throws InterruptedException, TimeoutException, IOException {
        System.out.println("Constructing a Grakn Core runner");

        if (!distributionFile.exists()) {
            throw new IllegalArgumentException("Grakn distribution file is missing from " + distributionFile.getAbsolutePath());
        }

        checkAndDeleteExistingDistribution(distributionFile);

        GRAKN_DISTRIBUTION_FILE = distributionFile;
        GRAKN_DISTRIBUTION_FORMAT = distributionFormat(distributionFile);
        GRAKN_TARGET_DIRECTORY = distributionTarget(distributionFile);

        tmpDir = Files.createTempDirectory("grakn_core_test");

        this.executor = new ProcessExecutor()
                .directory(Paths.get(".").toAbsolutePath().toFile())
                .redirectOutput(System.out)
                .redirectError(System.err)
                .readOutput(true)
                .destroyOnExit();

        this.unzip();

        System.out.println("Grakn Core runner constructed");
    }

    private static String distributionFormat(File distributionFile) {
        if (distributionFile.toString().endsWith(TAR)) {
            return TAR;
        } else if (distributionFile.toString().endsWith(ZIP)) {
            return ZIP;
        } else {
            fail(String.format("Distribution file format should either be %s or %s", TAR, ZIP));
        }
        return "";
    }

    private static Path distributionTarget(File distributionFile) {
        String format = distributionFormat(distributionFile);
        return Paths.get(distributionFile.toString().replaceAll(
                format.replace(".", "\\."), ""
        ));
    }

    private static void checkAndDeleteExistingDistribution(File distributionFile) throws IOException {
        Path target = distributionTarget(distributionFile);
        System.out.println("Checking for existing Grakn distribution at " + target.toAbsolutePath().toString());
        if (target.toFile().exists()) {
            System.out.println("There exists a Grakn Core distribution and will be deleted");
            FileUtils.deleteDirectory(target.toFile());
            System.out.println("Existing Grakn Core distribution deleted");
        } else {
            System.out.println("There is no existing Grakn Core distribution");
        }
    }

    private void unzip() throws IOException, TimeoutException, InterruptedException {
        System.out.println("Unarchiving Grakn Core distribution");
        if (GRAKN_DISTRIBUTION_FORMAT.equals(TAR)) {
            executor.command("tar", "-xf", GRAKN_DISTRIBUTION_FILE.toString(),
                    "-C", GRAKN_TARGET_DIRECTORY.getParent().toString()).execute();
        } else {
            executor.command("unzip", "-q", GRAKN_DISTRIBUTION_FILE.toString(),
                    "-d", GRAKN_TARGET_DIRECTORY.getParent().toString()).execute();
        }
        executor = executor.directory(GRAKN_TARGET_DIRECTORY.toFile());

        System.out.println("Grakn Core distribution unarchived");
    }

    @Override
    public String host() {
        return "127.0.0.1";
    }

    @Override
    public int port() {
        return 48555;
    }

    @Override
    public String address() {
        return host() + ":" + port();
    }

    public void start() throws InterruptedException, IOException, TimeoutException {
        try {
            System.out.println("Starting Grakn Core database server at " + GRAKN_TARGET_DIRECTORY.toAbsolutePath().toString());
            System.out.println("Database directory will be at " + tmpDir.toAbsolutePath());

            graknProcess = executor.command("./grakn", "server", "start").start();

            Thread.sleep(25000);

            assertTrue("Grakn did not manage to start", isStorageRunning() && isServerRunning());
//            assertTrue("Grakn Core failed to start", graknProcess.getProcess().isAlive());

            System.out.println("Grakn Core database server started");
        } catch (Throwable e) {
            printLogs();
            throw e;
        }
    }

    public void stop() throws InterruptedException, IOException, TimeoutException {
        if (graknProcess != null) {
            try {
                System.out.println("Stopping Grakn Core database server");

                graknProcess.getProcess().destroy();

                System.out.println("Grakn Core database server stopped");
            } catch (Exception e) {
                printLogs();
                throw e;
            }
        }
    }

    private void printLogs() throws InterruptedException, TimeoutException, IOException {
        System.out.println("================");
        System.out.println("Grakn Core Logs:");
        System.out.println("================");
        executor.command("cat", Paths.get(".", "logs", "grakn.log").toString()).execute();
    }

    private boolean isStorageRunning() throws InterruptedException, TimeoutException, IOException {
        System.out.println("Checking if Grakn Storage is running");
        ProcessResult output = executor.command("./grakn", "server", "status").execute();
        assertEquals(0, output.getExitValue());
        String[] lines = output.outputString().split("\n");
        boolean isRunning = lines[lines.length - 2].contains(": RUNNING");

        System.out.println("Grakn Storage is running: " + isRunning);
        return isRunning;
    }

    private boolean isServerRunning() throws InterruptedException, TimeoutException, IOException {
        System.out.println("Checking if Grakn Server is running");
        ProcessResult output = executor.command("./grakn", "server", "status").execute();
        assertEquals(0, output.getExitValue());
        String[] lines = output.outputString().split("\n");
        boolean isRunning = lines[lines.length - 1].contains(": RUNNING");

        System.out.println("Grakn Server is running: " + isRunning);
        return isRunning;
    }


}
