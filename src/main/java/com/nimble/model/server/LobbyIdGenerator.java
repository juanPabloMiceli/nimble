package com.nimble.model.server;

import com.nimble.exceptions.server.NoAvailableLobbiesException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LobbyIdGenerator {

	private final List<String> availableIds;
	private final Random randomGenerator = new Random();
	private final int lobbyIdLength = 4;

	private static LobbyIdGenerator lobbyIdGeneratorInstance = null;

	public static LobbyIdGenerator getLobbyIdGeneratorInstance() {
		if (lobbyIdGeneratorInstance == null) {
			lobbyIdGeneratorInstance = new LobbyIdGenerator();
		}
		return lobbyIdGeneratorInstance;
	}

	private LobbyIdGenerator() {
		this.availableIds = generateIds();
	}

	private List<String> generateIds() {
		List<String> list = new ArrayList<>();
		recursiveGenerateIds(list, "");
		return list;
	}

	private void recursiveGenerateIds(List<String> list, String prefix) {
		if (prefix.length() >= this.lobbyIdLength) {
			list.add(prefix);
			return;
		}
		for (char c = 'A'; c <= 'B'; c++) {
			String newPrefix = prefix + c;
			recursiveGenerateIds(list, newPrefix);
		}
	}

	public String getId() throws NoAvailableLobbiesException {
		if (this.availableIds.isEmpty()) {
			throw new NoAvailableLobbiesException();
		}
		int index = this.randomGenerator.nextInt(this.availableIds.size());
		return this.availableIds.remove(index);
	}

	public void restore(String id) {
		this.availableIds.add(id);
	}
}
