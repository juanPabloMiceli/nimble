package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.requests.KickRequest;
import com.nimble.dtos.responses.KickResponse;
import com.nimble.dtos.responses.LobbyInfoResponse;
import com.nimble.dtos.responses.errors.UnexpectedErrorResponse;
import com.nimble.exceptions.KickException;
import com.nimble.exceptions.game.InvalidPlayerNumberException;
import com.nimble.model.server.Lobby;
import com.nimble.model.server.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class KickHandler extends MethodHandler {

    private WebSocketSession session;

    private KickRequest payload;

    private NimbleRepository nimbleRepository;

    private final Logger logger = LoggerFactory.getLogger(KickHandler.class);

    private Messenger messenger;

    public KickHandler(WebSocketSession session, KickRequest payload, NimbleRepository nimbleRepository, Messenger messenger) {
        this.session = session;
        this.payload = payload;
        this.nimbleRepository = nimbleRepository;
        this.messenger = messenger;
    }

    @Override
    public void run() {
        User user = forceGetUser(nimbleRepository, payload.getSessionId(), messenger, session);
        if (user == null) {
            logger.warn("Alguien que no existe quiere modificar los penalties!");
            return;
        }
        Lobby lobby = forceGetLobby(nimbleRepository, user.getLobbyId(), messenger, session);
        if(lobby == null){
            logger.warn(String.format("%s quiere kickear a alguien en un lobby que no existe!", user.getName()));
            return;
        }
        if(!lobby.isOwner(user.getId())){
            logger.warn(String.format("%s quiere kickear a algien pero no es el host!", user.getName()));
            messenger.send(user.getId(), new KickException(payload.getKicked(),"You are not the lobby owner"));
            return;
        }
        String userIdToKick = null;
        try {
            userIdToKick = lobby.getUserId(payload.getKicked());
        } catch (InvalidPlayerNumberException e) {
            logger.warn(String.format("%s quiere kickear a un usuario que no existe (%d)!", user.getName(), payload.getKicked()));
            messenger.send(user.getId(), new KickException(payload.getKicked(), "Player does not exist"));
            return;
        }
        User userToKick = nimbleRepository.getUser(userIdToKick);
        if(userToKick == null){
            messenger.send(user.getId(), new UnexpectedErrorResponse("User to kick was not found!"));
            throw new RuntimeException("WUT");
        }
        lobby.remove(userIdToKick);
        lobby.restoreColor(userToKick.getColor());
        nimbleRepository.removeUser(userIdToKick);

        messenger.send(userIdToKick, new KickResponse());

        lobby
            .getUsersIds()
            .forEach(userId -> {
                messenger.send(
                        userId,
                        new LobbyInfoResponse(
                                lobby.isOwner(userId),
                                nimbleRepository.usersDtoAtLobby(lobby.getId()),
                                lobby.getId(),
                                lobby.getPenalties()
                        )
                );
            });

        logger.info(String.format("%s fue kickeado del lobby \"%s\"", userToKick.getName(), lobby.getId()));
    }
}
