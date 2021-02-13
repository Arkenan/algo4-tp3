package edu.fiuba.fpfiuba43

import cats.effect.IO
import edu.fiuba.fpfiuba43.models.{InputRow, Score}
import edu.fiuba.fpfiuba43.services.{ScoreService, ScoreServiceRest}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalamock.scalatest.MockFactory

import java.time.LocalDateTime

class ScoreServiceTest extends AnyFlatSpec with MockFactory {

  val cachedRow: InputRow = InputRow(1, LocalDateTime.now(), Some(1), Some(2), None, 1.5, 2.0, 3.0, "D",
    Some(1), None, None, "D", 150.0, 200.0, 50.23)

  val nonCachedRow: InputRow = InputRow(1, LocalDateTime.now(), Some(1), Some(2), None, 1337.314, 2.0, 3.0, "D",
    Some(1), None, None, "D", 150.0, 200.0, 50.23)

  behavior of "ScoreService"

  it should "return the cached score when the row is in cache" in {
    val cache: Cache[IO] = mock[Cache[IO]]
    val scorer: Scorer = mock[Scorer]
    val scoreService: ScoreService[IO] = new ScoreServiceRest(cache, scorer)
    val cachedScore = Score(75.0)

    (cache.getScoreFromCache _).expects(cachedRow.hashCode()).returning(IO.pure(Some(cachedScore)))
    assert(scoreService.getScore(cachedRow).unsafeRunSync() == cachedScore)
  }

  it should "calculate the value with the scorer when the row is not cached" in {
    val cache: Cache[IO] = mock[Cache[IO]]
    val scorer: Scorer = mock[Scorer]
    val scoreService: ScoreService[IO] = new ScoreServiceRest(cache, scorer)
    val calculatedScore = Score(15.0)

    (cache.getScoreFromCache _).expects(nonCachedRow.hashCode()).returning(IO.pure(None))
    (cache.saveScoreInCache _).expects(nonCachedRow.hashCode(), calculatedScore).returning(IO.pure())
    (scorer.score _).expects(nonCachedRow).returning(calculatedScore)
    assert(scoreService.getScore(nonCachedRow).unsafeRunSync() == calculatedScore)
  }
}
