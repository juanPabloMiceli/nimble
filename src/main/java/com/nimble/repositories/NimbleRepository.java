package com.nimble.repositories;

import com.nimble.model.Lobby;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NimbleRepository {

	private Lobby lobby;

}
