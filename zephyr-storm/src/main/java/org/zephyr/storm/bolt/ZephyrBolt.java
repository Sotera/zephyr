package org.zephyr.storm.bolt;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseBasicBolt;

/**
 * ZephyrBolt is our contract for communication between the steps in a ZephyrProcess;
 * type T describes what sort of component our ZephyrBolt encapsulates - a ParserFactory, a Schema
 * definition, an Enricher implementation or an Outputter implementation.
 * <p/>
 * This class requires two Strings in its constructor; these strings are for a spring configuration file
 * and a springBeanId that is the bean ID in our configuration file for the component we are encapsulating
 *
 * @param <T>
 */
public abstract class ZephyrBolt<T> extends BaseBasicBolt {

    private static final long serialVersionUID = 1147917214093623902L;

    /**
     * The component this class encapsulates; generally defined (concretely) by the implementing subclass
     */
    protected T component;
    /**
     * The Spring configuration file we expect to use, on the classpath, to provide the bean definition for our component
     */
    protected String springConfig;
    /**
     * The bean ID in our Spring configuration file
     */
    protected String springBeanId;

    /**
     * Required constructor, to be called by all implementing subclasses if they want to override the constructor behavior
     *
     * @param springConfig
     * @param springBeanId
     */
    public ZephyrBolt(String springConfig, String springBeanId) {
        this.springConfig = springConfig;
        this.springBeanId = springBeanId;
        this.component = null;
    }

    /**
     * When this object is finally ready to begin work, we will use Spring to instantiate the component we will use
     * This bypasses our need for making every Zephyr component Kryo serializable - though it is admittedly
     * very un-Storm-like
     */
    @SuppressWarnings({"unchecked", "resource", "rawtypes"})
    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        ApplicationContext springContext = new ClassPathXmlApplicationContext(springConfig);
        this.component = (T) springContext.getBean(springBeanId);
    }


}
