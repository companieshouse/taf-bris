package uk.gov.companieshouse.taf.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jblandford on 30/05/2017.
 * Class to facilitate the loading of config properties that can be used to run the test suite
 * over any environment
 */
public class Env {
    public static final String DEFAULT_RUN_PROFILE = "default";
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
    public static final Env INSTANCE;
    private String env;
    public Config config;

    public static Env env() {
        return INSTANCE;
    }

    /**
     * This method constructs the relevant config based on a JVM argument.  This is required to
     * run the test suite over other environments.
     * @param env The environment under test
     */
    public Env(String env) {
        Optional<String> param = Optional.ofNullable(env);
        if (param.isPresent()) {
            this.env = env;
            log.info("Using environment profile " + env);

        } else {
            this.env = DEFAULT_RUN_PROFILE;
            log.info("No environment passed so using the default profile to execute the tests."
                    + " In order to specify an environment, please provide env profile parameter "
                    + "(-Denv=myProfile)");
        }

        this.config = this.initConfig();
    }

    private Config initConfig() {
        Config systemConfig = ConfigFactory.systemProperties();
        File envConfig = this.searchConfigFileInClasspath(ConfigConstants.ENV_CONF_FILE);
        return systemConfig.withFallback(ConfigFactory.parseFile(envConfig))
                .resolve().getConfig(this.env);
    }

    private File searchConfigFileInClasspath(String filename) {
        List<File> files;

        try (Stream<File> streamFiles = (new ArrayList(FileUtils.listFiles(
                new File(ConfigConstants.PROJECT_DIR),
                new RegexFileFilter(filename), TrueFileFilter.INSTANCE))).stream()
                .filter((Object f) -> {
                    File file = (File)f;
                    return !(file.getAbsolutePath().contains(ConfigConstants.TARGET_DIR));
                })) {
            files = new ArrayList<>();
            streamFiles.forEach(files::add);
        }

        if (files.size() == 0) {
            throw new Error("Config file with name [" + filename + "] could not be "
                    + "found in your classpath.");
        } else {
            if (files.size() > 1) {
                this.log.warn("More than one file found for this environment with name ["
                        + filename + "]");
            }

            if (!((File)files.get(0)).isFile()) {
                throw new Error("The file [" + ((File)files.get(0)).getAbsolutePath() + "] "
                        + "is not a normal file.");
            } else {
                return (File)files.get(0);
            }
        }
    }

    static {
        INSTANCE = new Env(ConfigConstants.ENV_PROPERTY);
    }
}
