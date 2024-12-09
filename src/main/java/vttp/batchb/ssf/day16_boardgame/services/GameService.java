package vttp.batchb.ssf.day16_boardgame.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batchb.ssf.day16_boardgame.repository.GameRepository;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepo;
    
    private static final Logger logger = Logger.getLogger(GameService.class.getName());

    public String insertGame(JsonObject game) throws FileNotFoundException{

        String gameKey = Integer.toString(game.getInt("gid"));
        gameRepo.insertGame(gameKey, game.toString());
        return gameKey;

    }

    public Optional<String> getGameById(String key){
        return gameRepo.getGameById(key);
    }

    public int incrementCount(String key){
        return gameRepo.incrementCount(key);
    }


}
