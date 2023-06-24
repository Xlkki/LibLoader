package ru.xikki.plugins.libloader.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Map;

public class PluginUtility {

	@NotNull
	public static PluginClassLoader getPluginClassLoader(@NotNull JavaPlugin plugin) throws NoSuchFieldException, IllegalAccessException {
		Field field = JavaPlugin.class.getDeclaredField("classLoader");
		field.setAccessible(true);
		return (PluginClassLoader) field.get(plugin);
	}

	@NotNull
	public static ClassLoader getPluginLibraryLoader(@NotNull JavaPlugin plugin) throws NoSuchFieldException, IllegalAccessException {
		PluginClassLoader loader = PluginUtility.getPluginClassLoader(plugin);
		Field field = PluginClassLoader.class.getDeclaredField("libraryLoader");
		field.setAccessible(true);
		return (ClassLoader) field.get(loader);
	}

	public static void setPluginLibraryLoader(@NotNull JavaPlugin plugin, @NotNull ClassLoader libraryLoader) throws NoSuchFieldException, IllegalAccessException {
		PluginClassLoader loader = PluginUtility.getPluginClassLoader(plugin);
		Field field = PluginClassLoader.class.getDeclaredField("libraryLoader");
		field.setAccessible(true);
		field.set(loader, libraryLoader);
	}

	@NotNull
	public static Map<String, Class<?>> getPluginClasses(@NotNull JavaPlugin plugin) throws NoSuchFieldException, IllegalAccessException {
		PluginClassLoader loader = PluginUtility.getPluginClassLoader(plugin);
		Field field = PluginClassLoader.class.getDeclaredField("classes");
		field.setAccessible(true);
		return (Map<String, Class<?>>) field.get(loader);
	}

}
