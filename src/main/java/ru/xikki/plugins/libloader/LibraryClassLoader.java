package ru.xikki.plugins.libloader;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LibraryClassLoader extends URLClassLoader {

	static {
		ClassLoader.registerAsParallelCapable();
	}

	protected final Library library;
	protected final JarFile jarFile;
	protected final URL url;

	protected LibraryClassLoader(@NotNull Library library) throws IOException, NoSuchFieldException, IllegalAccessException {
		super(new URL[0]);
		this.library = library;
		this.jarFile = new JarFile(this.library.file);
		this.url = this.library.file.toURI().toURL();
		this.addURL(this.url);
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
		Class<?> c = this.library.classes.get(name);
		if (c != null)
			return c;

		String path = name.replace(".", "/").concat(".class");
		JarEntry entry = this.jarFile.getJarEntry(path);

		if (entry != null) {
			byte[] classBytes;
			try (InputStream in = this.jarFile.getInputStream(entry)) {
				classBytes = in.readNBytes(in.available());
			} catch (IOException ex) {
				throw new ClassNotFoundException(name, ex);
			}

			CodeSigner[] signers = entry.getCodeSigners();
			CodeSource source = new CodeSource(this.url, signers);
			try {
				c = super.defineClass(name, classBytes, 0, classBytes.length, source);
			} catch (ClassFormatError ignored) {
			}
		}

		if (c == null)
			c = super.findClass(name);
		this.library.classes.put(name, c);
		return c;
	}

	@Override
	public void close() throws IOException {
		try {
			this.library.classes.clear();
			super.close();
		} finally {
			this.jarFile.close();
		}
	}

}
