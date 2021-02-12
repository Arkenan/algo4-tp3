package edu.fiuba.fpfiuba43

import cats.effect.{ContextShift, ExitCode, IO, IOApp}
import edu.fiuba.fpfiuba43.http.{DB, Fpfiuba43Server}

import scala.concurrent.ExecutionContext

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {

    val db = DB()
    Fpfiuba43Server.stream[IO].compile.drain.as(ExitCode.Success)
  }
}
