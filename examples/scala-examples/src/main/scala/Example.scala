package com.geldata.examples

import scala.concurrent.{ExecutionContext, Future}
import com.geldata.driver.GelClientPool

trait Example {
  def run(clientPool: GelClientPool)(implicit context: ExecutionContext): Future[Unit];
}