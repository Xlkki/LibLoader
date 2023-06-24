package ru.xikki.plugins.libloader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LibraryManager {

	protected final Map<String, Library> libraryByIds = new HashMap<>();
	protected final Map<File, Library> libraryByFiles = new HashMap<>();

	@Nullable
	public Library getLibrary(@NotNull String id) {
		return this.libraryByIds.get(id);
	}

	@Nullable
	public Library getLibrary(@NotNull File file) {
		return this.libraryByFiles.get(file);
	}

	@NotNull
	public Collection<Library> getLibraries() {
		return this.libraryByIds.values();
	}

	public boolean isRegistered(@NotNull String id) {
		return this.libraryByIds.containsKey(id);
	}

	public boolean isRegistered(@NotNull Library library) {
		return this.getLibrary(library.getId()) == library;
	}


}
