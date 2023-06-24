package ru.xikki.plugins.libloader;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.xikki.plugins.libloader.utils.PluginUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class LibLoader extends JavaPlugin {

	private static LibLoader instance;

	private final LibraryManager manager = new LibraryManager();
	private final File libsFolder = new File(this.getDataFolder(), "libs");

	@Override
	public void onLoad() {
		instance = this;
		try {
			PluginUtility.setPluginLibraryLoader(this, new LibraryGroupClassLoader());
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		if (!this.libsFolder.exists()) {
			this.getLogger().warning("Libraries folders not found. Creating new...");
			this.libsFolder.mkdirs();
		}
		File[] files = this.libsFolder.listFiles();
		if (files != null)
			for (File file : files)
				Library.load(file);
	}

	@Override
	public void onEnable() {
		this.getCommand("library").setExecutor(new LibraryCommand());
	}

	@Override
	public void onDisable() {
		List<Library> libraries = new ArrayList<>(this.manager.getLibraries());
		libraries.forEach(Library::unload);
	}

	@NotNull
	public File getLibsFolder() {
		return libsFolder;
	}

	@NotNull
	public LibraryManager getManager() {
		return manager;
	}

	@NotNull
	public static LibLoader getInstance() {
		return instance;
	}

}
