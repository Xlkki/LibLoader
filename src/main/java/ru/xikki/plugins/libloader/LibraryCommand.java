package ru.xikki.plugins.libloader;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.xikki.plugins.libloader.utils.CommandUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LibraryCommand implements TabExecutor {

	private final List<String> completions = new ArrayList<>();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (args.length < 1) {
			sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
					"§cIncorrect command format"
			));
			sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
					"§cCommand format: /library <load/reload/unload/list> <...>"
			));
			return true;
		}
		if (args[0].equals("load")) {
			if (!sender.hasPermission("library.load")) {
				sender.sendMessage(Bukkit.permissionMessage());
				return true;
			}
			if (args.length < 2) {
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§cIncorrect command format"
				));
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§cCommand format: /library load <file_name>"
				));
				return true;
			}
			StringBuilder fileNameBuilder = new StringBuilder(args[1]);
			for (int i = 2; i < args.length; i++)
				fileNameBuilder.append(" ")
						.append(args[i]);
			if (!fileNameBuilder.toString().endsWith(".jar"))
				fileNameBuilder.append(".jar");
			String fileName = fileNameBuilder.toString();
			File file = new File(LibLoader.getInstance().getLibsFolder(), fileName);
			if (!file.exists()) {
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§cFile " + fileName + " does not exists"
				));
				return true;
			}
			if (!file.getName().endsWith(".jar")) {
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§cFile " + fileName + " is not a jar file"
				));
				return true;
			}
			if (Library.from(file) != null) {
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§cLibrary " + fileName + " already loaded"
				));
				return true;
			}
			Library.load(file);
			sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
					"§aLibrary " + fileName + " loaded"
			));
			return true;
		} else if (args[0].equals("unload")) {
			if (!sender.hasPermission("library.unload")) {
				sender.sendMessage(Bukkit.permissionMessage());
				return true;
			}
			if (args.length < 2) {
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§cIncorrect command format"
				));
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§cCommand format: /library unload <library_id>"
				));
				return true;
			}
			StringBuilder libraryIdBuilder = new StringBuilder(args[1]);
			for (int i = 2; i < args.length; i++)
				libraryIdBuilder.append(" ")
						.append(args[i]);
			String libraryId = libraryIdBuilder.toString();
			Library library = Library.from(libraryId);
			if (library == null) {
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§cLibrary " + libraryId + " is not loaded"
				));
				return true;
			}
			library.unload();
			sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
					"§aLibrary " + libraryId + " unloaded"
			));
			return true;
		} else if (args[0].equals("reload")) {
			if (!sender.hasPermission("library.reload")) {
				sender.sendMessage(Bukkit.permissionMessage());
				return true;
			}
			if (args.length < 2) {
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§cIncorrect command format"
				));
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§cCommand format: /library reload <library_id>"
				));
				return true;
			}
			StringBuilder libraryIdBuilder = new StringBuilder(args[1]);
			for (int i = 2; i < args.length; i++)
				libraryIdBuilder.append(" ")
						.append(args[i]);
			String libraryId = libraryIdBuilder.toString();
			Library library = Library.from(libraryId);
			if (library == null) {
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§cLibrary " + libraryId + " is not loaded"
				));
				return true;
			}
			sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
					"§eLibrary " + libraryId + " reloading has started..."
			));
			library.unload();
			Library.load(library.getFile());
			sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
					"§aLibrary " + libraryId + " reloaded"
			));
			return true;
		} else if (args[0].equals("list")) {
			if (!sender.hasPermission("library.list")) {
				sender.sendMessage(Bukkit.permissionMessage());
				return true;
			}
			if (args.length != 1) {
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§cIncorrect command format"
				));
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§cCommand format: /library list"
				));
				return true;
			}
			Collection<Library> libraries = LibLoader.getInstance().getManager().getLibraries();
			if (!libraries.isEmpty()) {
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§eLibraries [" + libraries.size() + "]:"
				));
				for (Library library : libraries)
					sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
							"\t§a - " + library.getId()
					));
			} else
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§cThere are no loaded libraries"
				));
			File[] files = LibLoader.getInstance().getLibsFolder().listFiles();
			if (files != null) {
				List<File> availableLibraries = new ArrayList<>();
				for (File file : files) {
					if (Library.from(file) != null)
						continue;
					availableLibraries.add(file);
				}
				if (availableLibraries.size() == 0)
					return true;
				sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
						"§eAvailable libraries [" + availableLibraries.size() + "]:"
				));
				for (File file : availableLibraries) {
					sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
							"\t§e - " + file.getName()
					));
				}
			}
			return true;
		} else {
			sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
					"§cIncorrect command format"
			));
			sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
					"§cCommand format: /library <load/reload/unload/list> <...>"
			));
			return true;
		}
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		this.completions.clear();
		if (args.length == 1) {
			if (sender.hasPermission("library.load"))
				this.completions.add("load");
			if (sender.hasPermission("library.unload"))
				this.completions.add("unload");
			if (sender.hasPermission("library.reload"))
				this.completions.add("reload");
			if (sender.hasPermission("library.list"))
				this.completions.add("list");
		} else if (args.length >= 2) {
			if (args[0].equals("load") && sender.hasPermission("library.load")) {
				File[] files = LibLoader.getInstance().getLibsFolder().listFiles();
				if (files != null)
					for (File file : files) {
						String fileName = file.getName();
						String[] words = fileName.split(" ");
						StringBuilder builder = new StringBuilder();
						for (int i = args.length - 2; i < words.length; i++)
							builder.append(words[i]).append(" ");
						if (builder.isEmpty())
							continue;
						builder.setLength(builder.length() - 1);
						this.completions.add(builder.toString());
					}
			} else if (args[0].equals("unload") && sender.hasPermission("library.unload")) {
				for (Library library : LibLoader.getInstance().getManager().getLibraries()) {
					String libraryId = library.getId();
					String[] words = libraryId.split(" ");
					StringBuilder builder = new StringBuilder();
					for (int i = args.length - 2; i < words.length; i++)
						builder.append(words[i]).append(" ");
					if (builder.isEmpty())
						continue;
					builder.setLength(builder.length() - 1);
					this.completions.add(builder.toString());
				}
			} else if (args[0].equals("reload") && sender.hasPermission("library.reload")) {
				for (Library library : LibLoader.getInstance().getManager().getLibraries()) {
					String libraryId = library.getId();
					String[] words = libraryId.split(" ");
					StringBuilder builder = new StringBuilder();
					for (int i = args.length - 2; i < words.length; i++)
						builder.append(words[i]).append(" ");
					if (builder.isEmpty())
						continue;
					builder.setLength(builder.length() - 1);
					this.completions.add(builder.toString());
				}
			}
		}
		return CommandUtils.filter(this.completions, args);
	}

}
