package ru.xikki.plugins.libloader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Library {

	protected final String id;
	protected final File file;
	protected final LibraryClassLoader classLoader;
	protected final Map<String, Class<?>> classes;

	private Library(@NotNull File file) {
		try {
			String fileName = file.getName();
			if (!fileName.endsWith(".jar"))
				throw new IllegalStateException(fileName + " is not jar file");
			this.id = fileName.substring(0, fileName.lastIndexOf("."));
			this.file = file;
			this.classLoader = new LibraryClassLoader(this);
			this.classes = new HashMap<>();
		} catch (IOException | NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@NotNull
	public String getId() {
		return id;
	}

	@NotNull
	public File getFile() {
		return file;
	}

	@NotNull
	public LibraryClassLoader getClassLoader() {
		return classLoader;
	}

	@NotNull
	public Map<String, Class<?>> getClasses() {
		return classes;
	}

	public boolean isRegistered() {
		return LibLoader.getInstance().getManager().isRegistered(this);
	}

	protected void load() {
		LibraryManager manager = LibLoader.getInstance().getManager();
		if (manager.isRegistered(this.id))
			throw new IllegalStateException("Library " + this.id + " already registered");
		manager.libraryByIds.put(this.id, this);
		manager.libraryByFiles.put(this.file, this);
	}

	protected void unload() {
		LibraryManager manager = LibLoader.getInstance().getManager();
		if (!manager.isRegistered(this))
			throw new IllegalStateException("Library " + this.id + " is not registered");

		try {
			this.classLoader.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		manager.libraryByIds.remove(this.id);
		manager.libraryByFiles.remove(this.file);
	}

	@Nullable
	public static Library from(@NotNull String id) {
		return LibLoader.getInstance().getManager().getLibrary(id);
	}

	@Nullable
	public static Library from(@NotNull File file) {
		return LibLoader.getInstance().getManager().getLibrary(file);
	}

	@NotNull
	public static Library load(@NotNull File file) {
		Library library = new Library(file);
		library.load();
		return library;
	}

}
