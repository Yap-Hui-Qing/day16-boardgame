package vttp.batchb.ssf.day16_boardgame.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp.batchb.ssf.day16_boardgame.services.GameService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(path = "/api/boardgame")
public class BoardController {

    @Autowired
    private GameService gameSvc;

    // task 1
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postBoardGame() throws FileNotFoundException {

        File file = new File("src/main/resources/static/game.json");
        FileReader fr = new FileReader(file);
        JsonReader reader = Json.createReader(fr);
        JsonArray result = reader.readArray();

        JsonArrayBuilder responseArray = Json.createArrayBuilder();
        for (int i = 0; i < result.size(); i++) {
            JsonObject gameJson = result.getJsonObject(i);
            String gameKey = gameSvc.insertGame(gameJson);
            int insert_count = gameSvc.incrementCount("insertCount");
            responseArray.add(Json.createObjectBuilder()
                    .add("insert_count", insert_count)
                    .add("id", gameKey)
                    .build());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseArray.build().toString());

    }

    // task 2
    @GetMapping(path = "/{boardgameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getBoardGame(@PathVariable("boardgameId") String gid) {

        Optional<String> opt = gameSvc.getGameById(gid);

        // 404
        if (opt.isEmpty()) {
            JsonObject errorResponse = Json.createObjectBuilder()
                    .add("error", "Board Game is not found")
                    .add("id", gid)
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse.toString());
        } else {
            return ResponseEntity.ok(opt.get());
        }

    }

    // task 3
    @PutMapping(path = "/{boardgameId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateBoardGame(@PathVariable("boardgameId") String gid,
            @RequestParam(defaultValue = "false") Boolean upsert, @RequestBody String payload)
            throws FileNotFoundException {

        // check if the game exists
        Optional<String> opt = gameSvc.getGameById(gid);

        // 404
        // if game does not exist and upsert is false
        if (opt.isEmpty() & !upsert) {
            JsonObject errorResponse = Json.createObjectBuilder()
                    .add("error", "Board Game is not found")
                    .add("id", gid)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse.toString());
        }

        // parse the input
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject gameJson = reader.readObject();

        // insert or update game
        String gameKey = gameSvc.insertGame(gameJson);
        int update_count = gameSvc.incrementCount("updateCount");

        JsonObject result = Json.createObjectBuilder()
                .add("update_count", update_count)
                .add("id", gameKey)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());

    }

}
