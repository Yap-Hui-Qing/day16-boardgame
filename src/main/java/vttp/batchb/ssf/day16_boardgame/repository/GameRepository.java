package vttp.batchb.ssf.day16_boardgame.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {

    @Autowired
    @Qualifier("redis-0")
    private RedisTemplate<String, String> template;
    
    // set abc123 gameJson
    public void insertGame(String key, String gameJson){
        ValueOperations<String, String> valueOps = template.opsForValue();
        valueOps.set(key, gameJson);
    }

    // get abc123
    public Optional<String> getGameById(String key){
        ValueOperations<String, String> valueOps = template.opsForValue();
        String game = valueOps.get(key);
        if (game == null)
            return Optional.empty();
        
        return Optional.of(game);
    }

    // incr updateCount
    public int incrementCount(String key){
        ValueOperations<String, String> valueOps = template.opsForValue();
        return valueOps.increment(key).intValue();
    }

}
