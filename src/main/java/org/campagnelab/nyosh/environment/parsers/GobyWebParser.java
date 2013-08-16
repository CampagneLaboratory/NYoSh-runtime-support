package org.campagnelab.nyosh.environment.parsers;

import org.apache.log4j.Logger;
import org.campagnelab.gobyweb.clustergateway.jobs.simulator.Option;
import org.campagnelab.gobyweb.clustergateway.submission.ClusterGatewaySimulator;
import org.campagnelab.nyosh.environment.NYoShRuntimeEnvironment;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Parser for GobyWeb designTimeDefaults.
 * @author manuele
 */
public class GobyWebParser implements Parser {

    protected static final org.apache.log4j.Logger logger;
    private static Map<String,String> designTimeDefaults = new HashMap<String, String>();

    static {
        //static initialization of variable known to be used by GobyWeb on a cluster node
        //values are used at design time by the editor allowing to load other Environment Sources from the local filesystem
        designTimeDefaults.put("JOB_DIR", System.getProperty("user.home") + "/plugins-SDK-cache");
        designTimeDefaults.put("TMPDIR", System.getProperty("user.home") + "/plugins-SDK-cache");
        designTimeDefaults.put("SGE_O_WORKDIR", System.getProperty("user.home") + "/plugins-SDK-cache");
        Log4JInitializer.init();
        logger= Logger.getLogger(GobyWebParser.class);
    }

    /**
     * Parses the source and populates the {@link org.campagnelab.nyosh.environment.NYoShRuntimeEnvironment}
     *
     * @param source
     */
    @Override
    public void parseAtRunTime(String ... source)  {
       //the designTimeDefaults already exist in the process environment (at least JOB_DIR), here we just populate the nyosh env
       //with them to avoid to rely on a previous loaded JVM Source
        NYoShRuntimeEnvironment environment = NYoShRuntimeEnvironment.getEnvironment();
        for (Map.Entry<String,String> variable : designTimeDefaults.entrySet())
            environment.addVariable(variable.getKey(), System.getenv().get(variable.getKey()));
        if (! System.getenv().containsKey("JOB_DIR")){
            System.out.println("JOB_DIR is not defined in the current environment");
            logger.warn("JOB_DIR is not defined in the current environment");
            return;
        }
        String jobDir = System.getenv().get("JOB_DIR");
        //load constants.sh and auto-options.sh

    }

    /**
     * Parses the source.
     *
     * @param pluginInfo id, type and plugins-root of the plugin.
     * @return the list of variables visible to the plugin.
     */
    @Override
    public SortedSet<ScriptVariable> parseAtDesignTime(String ... pluginInfo) {
        SortedSet<ScriptVariable> returnedVariables = new TreeSet<ScriptVariable>();
        try {
            if (pluginInfo.length !=3) {
                System.err.println("Invalid number of parameters");
                logger.debug(String.format("Invalid number of parameters %d",pluginInfo.length));
                return returnedVariables;
            }
            System.out.println(String.format("Loading variables for script %s, type %s, from root %s",pluginInfo[0],pluginInfo[1],pluginInfo[2]));
            logger.debug(String.format("Loading variables for script %s, type %s, from root %s",pluginInfo[0],pluginInfo[1],pluginInfo[2]));
            //send a request to the plugin-sdk to simulate the job submission
            for (Option option : ClusterGatewaySimulator.process(prepareArguments(pluginInfo), false)) {
                returnedVariables.add(ScriptVariable.fromOption(option));
            }
            for (Map.Entry<String,String> defaultVar : designTimeDefaults.entrySet()) {
                returnedVariables.add(new ScriptVariable(defaultVar.getKey(),defaultVar.getValue(), ScriptVariable.Kind.DIRECTORY));
            }
        } catch (Exception e) {
            System.err.println("Unable to get the list of variables from the plugins-SDK");
            logger.error("Unable to get the list of variables from the plugins-SDK", e);
        }
        return returnedVariables;
    }

    private String[] prepareArguments(String ... pluginInfo) {
        return new String[] {
           "--plugins-dir",
           pluginInfo[2],
           "--action",
           "view-job-env",
           pluginInfo[1].equalsIgnoreCase("resources")?"--resource":"--job",
           pluginInfo[0]
        };
    }

}
