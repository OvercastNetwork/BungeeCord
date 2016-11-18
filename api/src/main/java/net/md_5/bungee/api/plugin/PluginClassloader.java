package net.md_5.bungee.api.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class PluginClassloader extends URLClassLoader
{
    static
    {
        ClassLoader.registerAsParallelCapable();
    }

    private final List<PluginClassloader> dependencies;

    public PluginClassloader(List<PluginClassloader> dependencies, URL[] urls)
    {
        super( urls );
        this.dependencies = dependencies;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        synchronized(getClassLoadingLock(name)) {
            try {
                return loadLocalClass(name, resolve);
            } catch(ClassNotFoundException e1) {
                try {
                    return loadDependencyClass(name, resolve);
                } catch(ClassNotFoundException e2) {
                    return super.loadClass(name, resolve);
                }
            }
        }
    }

    private Class<?> loadLocalClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> cls = findLoadedClass(name);
        if(cls == null) cls = findClass(name);
        if(resolve) resolveClass(cls);
        return cls;
    }

    private Class<?> loadDependencyClass(String name, boolean resolve) throws ClassNotFoundException {
        for(PluginClassloader loader : dependencies) {
            try {
                return loader.loadLocalClass(name, resolve);
            } catch(ClassNotFoundException e) {
                // continue
            }
        }
        throw new ClassNotFoundException(name);
    }
}
