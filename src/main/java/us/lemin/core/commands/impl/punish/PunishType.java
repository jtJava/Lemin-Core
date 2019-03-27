package us.lemin.core.commands.impl.punish;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PunishType {
	BAN("ban", "banned", "Unfair Advantage"),
	MUTE("mute", "muted", "Misconduct");

	private final String name;
	private final String pastTense;
	private final String defaultMessage;

}
