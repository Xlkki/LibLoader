package ru.xikki.plugins.libloader;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class LibraryGroupClassLoader extends ClassLoader {

	static {
		ClassLoader.registerAsParallelCapable();
	}

	@NotNull
	@Override
	public URL getResource(@NotNull String name) {
		return this.findResource(name);
	}

	@NotNull
	@Override
	public Enumeration<URL> getResources(@NotNull String name) throws IOException {
		return this.findResources(name);
	}

	@NotNull
	@Override
	public Class<?> loadClass(@NotNull String name) throws ClassNotFoundException {
		return super.loadClass(name, false);
	}

	@NotNull
	@Override
	protected Class<?> findClass(@NotNull String name) throws ClassNotFoundException {
		for (Library library : LibLoader.getInstance().getManager().libraryByIds.values()) {
			try {
				return Class.forName(name, true, library.classLoader);
			} catch (ClassNotFoundException e) {
			}
		}
		return super.findClass(name);
	}

}
