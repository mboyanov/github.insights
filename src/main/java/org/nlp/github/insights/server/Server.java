package org.nlp.github.insights.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.plugins.server.netty.RestEasyHttpResponseEncoder;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.reflections.Reflections;

public class Server {


    public void start(String host, int port) {

        ResteasyDeployment red = new ResteasyDeployment();
        red.setActualResourceClasses(getRestEasyResources());
        red.setActualProviderClasses(getRestEasyProviders());
        NettyJaxrsServer netty = new NettyJaxrsServer();

        netty.setExecutorThreadCount(99);
        netty.setDeployment(red);
        netty.setHostname(host);
        netty.setPort(port);
        netty.setRootResourcePath("");
        netty.setSecurityDomain(null);

        netty.start();
        System.out.println("started server");
    }

    private static List<Class> getRestEasyResources() {
        Reflections reflections = new Reflections("org.nlp.github.insights.api");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Path.class);
        Set<Class> resourceClasses = new HashSet<Class>();
        for (Class clazz : annotated) {
            Set<Class<?>> impls = reflections.getSubTypesOf(clazz);
            for (Class impl : impls) {
                resourceClasses.add(impl);
            }
        }
        return new ArrayList<Class>(resourceClasses);
    }

    private static List<Class> getRestEasyProviders() {
        List<Class> providerClasses = new ArrayList<>();
        providerClasses.add(ResteasyJackson2Provider.class);
        providerClasses.add(RestEasyHttpResponseEncoder.class);
        return providerClasses;
    }

}
