package com.bernardoms.minesweeperapi.repository;

import com.bernardoms.minesweeperapi.model.Game;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game, ObjectId> {
  List<Game> findGamesByPlayer(String playerName);
}
