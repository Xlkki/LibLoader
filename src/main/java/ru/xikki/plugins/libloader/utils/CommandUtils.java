package ru.xikki.plugins.libloader.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandUtils {

	@NotNull
	public static List<String> filter(@NotNull List<String> variables, @NotNull String[] args) {
		List<String> result = new ArrayList<>();
		String lastArgument = args[args.length - 1];
		for (String variable : variables)
			if (variable.startsWith(lastArgument))
				result.add(variable);
		return result;
	}

}
